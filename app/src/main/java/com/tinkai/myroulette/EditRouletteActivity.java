package com.tinkai.myroulette;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class EditRouletteActivity extends AppCompatActivity {
    RouletteOpenHelper helper = null;
    private boolean newFlag = false;
    private String id = "";
    private ArrayList<LinearLayout> itemList;
    private ArrayList<EditText> itemNameList;
    private ArrayList<EditText> itemRatioList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_roulette);

        // DB
        if (helper == null) {
            helper = new RouletteOpenHelper(EditRouletteActivity.this);
        }

        Intent intent = this.getIntent();
        id = intent.getStringExtra("id");

        if (id.equals("")) {
            // 新規作成
            newFlag = true;
        } else {
            // 編集
            SQLiteDatabase db = helper.getWritableDatabase();
            try {
                Cursor c = db.rawQuery("select name from ROULETTE_TABLE where uuid = '" + id + "'", null);
                boolean next = c.moveToFirst();
                while (next) {
                    String name = c.getString(0);
                    EditText nameEdit = findViewById(R.id.roulette_name_edit);
                    nameEdit.setText(name, TextView.BufferType.NORMAL);
                    next = c.moveToNext();
                }
            } finally {
                db.close();
            }
        }

        // itemList
        this.itemList = new ArrayList<>();
        this.itemNameList = new ArrayList<>();
        this.itemRatioList = new ArrayList<>();
        if (id.equals("")) {
            addItemList();
        } else {
            SQLiteDatabase db = helper.getWritableDatabase();
            try {
                Cursor c = db.rawQuery("select id from ROULETTE_TABLE where uuid = '" + id + "'", null);
                c.moveToFirst();
                String rouletteID = String.valueOf(c.getInt(0));
                c = db.rawQuery("select name, ratio from ROULETTE_ITEM_TABLE" + rouletteID, null);
                boolean next = c.moveToFirst();
                while (next) {
                    String itemName = c.getString(0);
                    String itemRatio = c.getString(1);
                    Log.d("log", "==================" + itemName);
                    Log.d("log", "==================" + itemRatio);
                    addItemList();
                    Log.d("log", "==================" + this.itemNameList.size() + " " + this.itemRatioList.size());
                    this.itemNameList.get(this.itemNameList.size()-1).setText(itemName);
                    this.itemRatioList.get(this.itemRatioList.size()-1).setText(itemRatio);
                    next = c.moveToNext();
                }
            } finally {
                db.close();
            }

        }


        // Button
        Button addItemButton = findViewById(R.id.add_item_button);
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemList();
            }
        });

        final Button registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nameEdit = findViewById(R.id.roulette_name_edit);
                String name = nameEdit.getText().toString();

                SQLiteDatabase db = helper.getWritableDatabase();
                try {
                    if (newFlag) {
                        id = UUID.randomUUID().toString();
                        db.execSQL("insert into ROULETTE_TABLE(uuid, name) VALUES('" + id + "', '" + name + "')");

                        Cursor c = db.rawQuery("select id from ROULETTE_TABLE where uuid = '" + id + "'", null);
                        c.moveToFirst();
                        String rouletteID = String.valueOf(c.getInt(0));
                        Log.d("log", "=================Roulette" + rouletteID);
                        db.execSQL("CREATE TABLE ROULETTE_ITEM_TABLE" + rouletteID + "(" +
                                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                "name TEXT, " +
                                "ratio TEXT)");
                        registerItemList(db);
                    } else {
                        db.execSQL("update ROULETTE_TABLE set name = '" + name + "' where uuid = '" + id + "'");
                        Cursor c = db.rawQuery("select id from ROULETTE_TABLE where uuid = '" + id + "'", null);
                        c.moveToFirst();
                        String rouletteID = String.valueOf(c.getInt(0));
                        Log.d("log", "=================Roulette" + rouletteID);
                        db.execSQL("delete from ROULETTE_ITEM_TABLE" + rouletteID);
                        Log.d("log", "=================1");
                        registerItemList(db);
                    }
                } finally {
                    db.close();
                }

                Intent intent = new Intent(EditRouletteActivity.this, com.tinkai.myroulette.RouletteListActivity.class);
                startActivity(intent);
            }
        });
    }

    private void addItemList() {
        LinearLayout itemListLayout = findViewById(R.id.item_list);

        LinearLayout itemLayout = new LinearLayout(this);
        itemLayout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        itemLayout.setOrientation(LinearLayout.HORIZONTAL);
        this.itemList.add(itemLayout);

        TextView numText = new TextView(this);
        String num = String.valueOf(this.itemNameList.size());
        numText.setText(String.valueOf(this.itemNameList.size()+1) + ".");
        itemLayout.addView(numText, new LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1
        ));

        EditText nameEditText = new EditText(this);
        nameEditText.setHint("Name");
        itemLayout.addView(nameEditText, new LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                6
        ));
        this.itemNameList.add(nameEditText);

        EditText ratioEditText = new EditText(this);
        ratioEditText.setHint("Ratio");
        itemLayout.addView(ratioEditText, new LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                2
        ));
        this.itemRatioList.add(ratioEditText);

        Button deleteButton = new Button(this);
        deleteButton.setText("Del");
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        itemLayout.addView(deleteButton, new LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                2
        ));

        itemListLayout.addView(itemLayout);

        /*
        for (int i = 0; i < this.itemNameList.size(); i++) {
            Log.d("log", String.valueOf(this.itemNameList.get(i).getText()));
            Log.d("log", String.valueOf(this.itemRatioList.get(i).getText()));
        }
        */
    }

    private void registerItemList(SQLiteDatabase db) {
        try {
            Log.d("log", "=================" + id);
            Cursor c = db.rawQuery("select id from ROULETTE_TABLE where uuid = '" + id + "'", null);

            c.moveToFirst();
            String rouletteID = String.valueOf(c.getInt(0));
            Log.d("log", "=================" + rouletteID);
            for (int i = 0; i < this.itemNameList.size(); i++) {
                String itemName = String.valueOf(this.itemNameList.get(i).getText());
                String itemRatio = String.valueOf(this.itemRatioList.get(i).getText());
                Log.d("log", "=================" + i);
                Log.d("log", "=================Roulette" + rouletteID);
                Log.d("log", "=================" + itemName + " " + itemRatio);
                db.execSQL("insert into ROULETTE_ITEM_TABLE" + rouletteID + "(name, ratio) VALUES('" + itemName + "', '" + itemRatio + "')");
            }
        } finally {
            db.close();
        }
    }

}
