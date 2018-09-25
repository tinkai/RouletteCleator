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

public class RouletteListActivity extends AppCompatActivity {
    RouletteOpenHelper helper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roulette_list);

        if (helper == null) {
            helper = new RouletteOpenHelper(RouletteListActivity.this);
        }

        final ArrayList<HashMap<String, String>> rouletteList = new ArrayList<>();
        SQLiteDatabase db = helper.getWritableDatabase();
        /* DB削除したい時用
        db.execSQL("DROP TABLE IF EXISTS ROULETTE_TABLE");
        for (int i = 0; i < 100; i++) {
            Log.d("log", "=============" + i);
            try {
                db.execSQL("DROP TABLE IF EXISTS ROULETTE_ITEM_TABLE" + i);
            } catch (Exception e) {
                continue;
            }
        }
        helper.onCreate(db);
        */
        try {
            Cursor c = db.rawQuery("select uuid, name, use from ROULETTE_TABLE order by id", null);
            boolean next = c.moveToFirst();
            while (next) {
                HashMap<String, String> data = new HashMap<>();
                String uuid = c.getString(0);
                String name = c.getString(1);
                data.put("name", name);
                data.put("id", uuid);
                rouletteList.add(data);
                next = c.moveToNext();
            }
        } finally {
            db.close();
        }

        final SimpleAdapter simpleAdapter = new SimpleAdapter(this,
                rouletteList,
                android.R.layout.simple_list_item_2,
                new String[]{"name", "id"},
                new int[]{android.R.id.text1, android.R.id.text2}
                );

        ListView rouletteView = findViewById(R.id.roulette_list);
        rouletteView.setAdapter(simpleAdapter);

        rouletteView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(RouletteListActivity.this, com.tinkai.myroulette.EditRouletteActivity.class);

                // 選択されたビューを取得 TwoLineListItemを取得した後、text2の値を取得する
                TwoLineListItem two = (TwoLineListItem)view;
                TextView idTextView = (TextView)two.getText2();
                String isStr = (String) idTextView.getText();
                // 値を引き渡す (識別名, 値)の順番で指定します
                intent.putExtra("id", isStr);
                // Activity起動
                startActivity(intent);
            }
        });

        rouletteView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TwoLineListItem two = (TwoLineListItem)view;
                TextView idTextView = two.getText2();
                String idStr = (String) idTextView.getText();

                SQLiteDatabase db = helper.getWritableDatabase();
                try {
                    Cursor c = db.rawQuery("select id from ROULETTE_TABLE where uuid = '" + idStr + "'", null);
                    c.moveToFirst();
                    String rouletteID = String.valueOf(c.getInt(0));
                    db.execSQL("DROP TABLE ROULETTE_ITEM_TABLE" + rouletteID);

                    db.execSQL("DELETE FROM ROULETTE_TABLE WHERE uuid = '"+ idStr +"'");
                } finally {
                    db.close();
                }

                rouletteList.remove(position);
                simpleAdapter.notifyDataSetChanged();
                return true;
            }
        });


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

}
