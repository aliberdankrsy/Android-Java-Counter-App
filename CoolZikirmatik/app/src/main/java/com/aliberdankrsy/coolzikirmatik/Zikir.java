package com.aliberdankrsy.coolzikirmatik;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "zikirler")
public class Zikir {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String zikirMetni;
    private String tarihSaat;

    public Zikir(String zikirMetni, String tarihSaat) {
        this.zikirMetni = zikirMetni;
        this.tarihSaat = tarihSaat;
    }

    public String getTarihSaat() {
        return tarihSaat;
    }

    public String getZikirMetni() {
        return zikirMetni;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Diğer getter ve setter metotları buraya eklenecek
}