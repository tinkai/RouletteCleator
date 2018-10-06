package com.tinkai.myroulette;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TwoLineListItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class RouletteListActivity extends AppCompatActivity {
    RouletteOpenHelper helper = null;
    RouletteListAdapter rouletteListAdapter;
    ArrayList<RouletteInfo> rouletteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roulette_list);

        if (helper == null) {
            helper = new RouletteOpenHelper(RouletteListActivity.this);
        }

        this.rouletteList = new ArrayList<>();
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            Cursor c = db.rawQuery("select * from ROULETTE_TABLE where id = 0", null);
            // デフォルトのさいころを作成
            if (!c.moveToFirst()) {
                createDefaultRoulette(db);
            }

            c = db.rawQuery("select uuid, name from ROULETTE_TABLE order by id", null);
            boolean next = c.moveToFirst();
            while (next) {
                RouletteInfo data = new RouletteInfo();
                String uuid = c.getString(0);
                String name = c.getString(1);
                data.setUuid(uuid);
                data.setName(name);
                rouletteList.add(data);
                next = c.moveToNext();
            }
        } finally {
            db.close();
        }

        ListView rouletteView = findViewById(R.id.roulette_list);
        this.rouletteListAdapter = new RouletteListAdapter(this, this, R.layout.layout_roulette_row, rouletteList);
        rouletteView.setAdapter(this.rouletteListAdapter);

        Button createButton = findViewById(R.id.create_button);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RouletteListActivity.this, com.tinkai.myroulette.EditRouletteActivity.class);
                intent.putExtra("id", "");
                startActivity(intent);
            }
        });
    }

    private void createDefaultRoulette(SQLiteDatabase db) {
        String uuid = UUID.randomUUID().toString();
        db.execSQL("insert into ROULETTE_TABLE(id, uuid, name, use) VALUES('0', '" + uuid + "', 'Default Dice', '1')");
        db.execSQL("CREATE TABLE ROULETTE_ITEM_TABLE0(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "ratio TEXT)");
        for (int i = 6; i >= 1; i--) {
            db.execSQL("insert into ROULETTE_ITEM_TABLE0(name, ratio) VALUES('" + String.valueOf(i) + "', '')");
        }
    }

    public void startEditRouletteActivity(RouletteInfo info) {
        Intent intent = new Intent(RouletteListActivity.this, com.tinkai.myroulette.EditRouletteActivity.class);

        String uuid = info.getUuid();

        intent.putExtra("id", uuid);
        startActivity(intent);
    }

    public void setUseRoulette(RouletteInfo info) {
        String uuid = info.getUuid();
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            db.execSQL("update ROULETTE_TABLE set use = '0'");
            db.execSQL("update ROULETTE_TABLE set use = '1' where uuid = '" + uuid + "'");
        } finally {
            db.close();
        }

        Intent intent = new Intent(RouletteListActivity.this, com.tinkai.myroulette.MainActivity.class);
        startActivity(intent);
    }

    public void deleteRoulette(RouletteInfo info, int pos) {
        String uuid = info.getUuid();
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            Cursor c = db.rawQuery("select id from ROULETTE_TABLE where uuid = '" + uuid + "'", null);
            c.moveToFirst();
            String rouletteID = String.valueOf(c.getInt(0));
            db.execSQL("DROP TABLE ROULETTE_ITEM_TABLE" + rouletteID);

            db.execSQL("DELETE FROM ROULETTE_TABLE WHERE uuid = '"+ uuid +"'");
        } finally {
            db.close();
        }

        this.rouletteList.remove(pos);
        this.rouletteListAdapter.notifyDataSetChanged();
    }
}
