package com.tinkai.myroulette;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RouletteOpenHelper extends SQLiteOpenHelper {
    static final private String DBName = "ROULETTE_DB";
    static final private int VERSION = 1;

    public RouletteOpenHelper(Context context) {
        super(context, DBName, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE ROULETTE_TABLE (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "uuid TEXT, " +
                "name TEXT, " +
                "use INTEGER )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS ROULETTE_TABLE");
        onCreate(db);
    }

    public void deleteAll(SQLiteDatabase db) {
        //DB削除したい時用
        db.execSQL("DROP TABLE IF EXISTS ROULETTE_TABLE");
        for (int i = 0; i < 100; i++) {
            try {
                db.execSQL("DROP TABLE IF EXISTS ROULETTE_ITEM_TABLE" + i);
            } catch (Exception e) {
                continue;
            }
        }
        onCreate(db);
    }

}
