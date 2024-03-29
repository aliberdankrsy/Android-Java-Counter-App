package com.aliberdankrsy.coolzikirmatik;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Zikir.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ZikirDao zikirDao();
}