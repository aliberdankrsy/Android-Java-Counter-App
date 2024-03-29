package com.aliberdankrsy.coolzikirmatik;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ZikirDao {
    @Insert
    void addZikir(Zikir zikir);

    @Query("SELECT * FROM zikirler ORDER BY id DESC")
    List<Zikir> getAllZikirler();

    @Query("DELETE FROM zikirler")
    void deleteAllZikirler();
}
