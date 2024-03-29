package com.aliberdankrsy.coolzikirmatik;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RecordActivity extends AppCompatActivity {

    private MyAdapter adapter; // Değişiklik burada
    private ArrayList<String> zikirListesi;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);



        ListView listView = findViewById(R.id.listView);
        zikirListesi = new ArrayList<>();

        // MyAdapter sınıfını kullanırken yerel bir değişken olarak tanımladığınız adapter değişkenini sınıf düzeyindeki adapter değişkenine atayın
        adapter = new MyAdapter(zikirListesi, this);
        listView.setAdapter(adapter); // ListView'a adapter'ı set edin

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "zikir-database").build();

        // Diğer işlemler...
        getAndShowZikirListesi();
        // Kayıt yapılacak mı bilgisini al
        boolean recordRequired = getIntent().getBooleanExtra("recordRequired", true);

        if (recordRequired) {
            // Kayıt yapılacaksa zikir sayısını ve tarih-saat bilgisini kaydet
            saveZikir();
        }

        ImageButton btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // MainActivity'de bir XML dosyasını göstermek için intent oluştur
                playClickSound();
                Intent intent = new Intent(RecordActivity.this, MainActivity.class);
                intent.putExtra("showXml", "main_activity_layout"); // Gösterilecek XML dosyasının adını ekleyebilirsiniz
                startActivity(intent);
            }
        });

        ImageButton btnDeleteRecords = findViewById(R.id.btnDeleteRecords);

        btnDeleteRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playClickSound();
                // Tüm kayıtları silmek için bir AlertDialog göster
                AlertDialog.Builder builder = new AlertDialog.Builder(RecordActivity.this);
                builder.setTitle("Tüm Kayıtları Sil");
                builder.setMessage("Tüm kayıtları silmek istediğinize emin misiniz? Bu işlem geri alınamaz.");

                builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Tüm kayıtları silmek için veritabanı işlemlerini gerçekleştir
                        deleteAllRecords();
                    }
                });

                builder.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Kullanıcı işlemi iptal ettiğinde herhangi bir işlem yapma
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void saveZikir() {
        // Zikir sayısını ve tarih-saat bilgisini al
        int zikirSayisi = getIntent().getIntExtra("zikirCount", 0);
        String tarihSaat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        // Veritabanı işlemlerini arka plan thread'inde yap
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Zikir bilgisini veritabanına kaydet
                Zikir zikir = new Zikir("Zikir Sayısı: " + zikirSayisi, tarihSaat);
                db.zikirDao().addZikir(zikir);

                // Kaydedilen zikirleri yeniden yükle
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getAndShowZikirListesi();
                    }
                });
            }
        }).start();
    }

    public void getAndShowZikirListesi() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Zikir> zikirler = db.zikirDao().getAllZikirler();
                zikirListesi.clear();
                for (Zikir zikir : zikirler) {
                    zikirListesi.add(zikir.getZikirMetni() + " - " + zikir.getTarihSaat());
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }
    // Tüm kayıtları silmek için veritabanı işlemlerini gerçekleştiren metod
    private void deleteAllRecords() {
        // Veritabanı işlemlerini arka planda yapmak için bir Thread oluştur
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Tüm kayıtları sil
                db.zikirDao().deleteAllZikirler();

                // Zikir listesini temizle
                zikirListesi.clear();

                // ListView'ı güncelle
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });

                // Kayıtların silindiğini kullanıcıya bildir
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RecordActivity.this, "Tüm kayıtlar silindi.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }
    private void playClickSound() {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.tapsound); // Ses dosyasını yükle
        mediaPlayer.start(); // Sesi çal
    }
}