package com.aliberdankrsy.coolzikirmatik;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ListViewActivity extends AppCompatActivity {

    private ArrayAdapter<String> adapter;
    private List<String> zikirListesi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        ListView listView = findViewById(R.id.listView);
        zikirListesi = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, zikirListesi);
        listView.setAdapter(adapter);

        updateZikirListesi();
    }

    private void updateZikirListesi() {


        adapter.notifyDataSetChanged();
    }
}
