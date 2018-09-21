package com.tinkai.myroulette;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

        ArrayList<HashMap<String, String>> rouletteList = new ArrayList<>();
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            Cursor c = db.rawQuery("select uuid, name from ROULETTE_TABLE order by id", null);
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

        // 仮データ
        /*
        ArrayList<HashMap<String, String>> tmpList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            HashMap<String, String> data = new HashMap<>();
            data.put("name", "サンプルデータ"+i);
            data.put("id", "sampleID"+i);
            tmpList.add(data);
        }
        */

        SimpleAdapter simpleAdapter = new SimpleAdapter(this,
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
