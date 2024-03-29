package com.aliberdankrsy.coolzikirmatik;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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

public class MainActivity extends AppCompatActivity {
    private Button buttonZikir;
    private Button buttonRecord;
    private Button buttonReset;
    private int zikirCount = 0;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "zikir-database").build();

        buttonZikir = findViewById(R.id.buttonZikir);
        buttonRecord = findViewById(R.id.buttonRecord);
        buttonReset = findViewById(R.id.buttonReset);


        TextView linkTextView = findViewById(R.id.linkTextView);
        linkTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playClickSound();
                openLink("https://realteknolojinews.blogspot.com/2024/03/nur-nefes-zikirmatik-privacy-policy.html");
            }
        });
        /*buttonZikir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zikirCount++;
                // Burada zikir sayısını göstermek için gerekli işlemleri yapabilirsiniz.
            }
        });*/

        buttonZikir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zikirCount++;
                updateZikirCountTextView(); // Zikir sayısını gösteren TextView'i güncelle
            }
        });


        buttonRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playClickSound();
                // Eğer zikir sayısı 0 ise mesaj göster
                if (zikirCount == 0) {
                    // Mesajı göster
                    Toast.makeText(MainActivity.this, "Zikir sayınız 0'dan büyük olmalı!", Toast.LENGTH_SHORT).show();
                } else {
                    // Zikir değeri 0 değilse RecordActivity'yi başlat
                    Intent intent = new Intent(MainActivity.this, RecordActivity.class);
                    intent.putExtra("zikirCount", zikirCount);
                    startActivity(intent);
                }
            }
        });

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                playClickSound();
                showResetConfirmationDialog();
            }
        });

        // Yeni butonun referansını al
        Button buttonOpenRecordPage = findViewById(R.id.buttonOpenRecordPage);
        // Butona tıklanınca zikir kayıt sayfasını aç
        buttonOpenRecordPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (zikirCount > 0) {
                    // Eğer zikir sayısı 0'dan büyükse kullanıcıya uyarı mesajı göster
                    playClickSound();
                    showZikirCountWarning();
                } else {
                    // Zikir sayısı 0 veya daha küçükse, RecordActivity'yi başlat
                    playClickSound();
                    startRecordActivity();
                }
            }
        });





    }
    // Zikir sayısını gösteren TextView'i güncelleyen yardımcı metot
    // Kullanıcıya zikir sayısını kaydetmediği konusunda bir uyarı mesajı gösteren metod
    private void showZikirCountWarning() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Zikir sayınızı kaydedin!");
        builder.setMessage("Zikir sayınız kaydedilmemiş devam ederseniz sıfırlanacak!\nDevam etmek istiyor musunuz?");

        builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Kullanıcı "Evet" derse RecordActivity'yi başlat

                startRecordActivity();
            }
        });

        builder.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Kullanıcı uyarıyı kabul etmezse herhangi bir işlem yapma
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    // RecordActivity'yi başlatan metod
    private void startRecordActivity() {
        Intent intent = new Intent(MainActivity.this, RecordActivity.class);
        // Zikir sayısı ve kayıt yapılacak mı bilgisini aktar
        intent.putExtra("zikirCount", zikirCount);
        intent.putExtra("recordRequired", false); // Kayıt yapılacak mı bilgisi false olarak ayarlanır
        startActivity(intent);
    }
    private void updateZikirCountTextView() {
        TextView zikirCountTextView = findViewById(R.id.textViewZikirCount);
        zikirCountTextView.setText("Zikir Sayısı: " + zikirCount);
    }
    private void showResetConfirmationDialog() {
        if (zikirCount == 0) {
            Toast.makeText(MainActivity.this, "Zikir sayınız zaten 0!", Toast.LENGTH_SHORT).show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Sıfırlama Onayı");
            builder.setMessage("Zikir sayınızı sıfırlamak istediğinizden emin misiniz?");
            builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    zikirCount = 0;
                    updateZikirCountTextView();
                    Toast.makeText(MainActivity.this, "Zikir sayınız sıfırlandı!", Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("Hayır", null);
            builder.show();
        }
    }

    public void openLink(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    // Ses çalma metodunu tanımla
    private void playClickSound() {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.tapsound); // Ses dosyasını yükle
        mediaPlayer.start(); // Sesi çal
    }
}