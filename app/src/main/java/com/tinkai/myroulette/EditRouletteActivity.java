package com.tinkai.myroulette;

import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
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
    private ArrayList<ItemInfo> itemList;
    private ItemListAdapter itemListAdapter;

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
                c.moveToFirst();
                String name = c.getString(0);
                EditText nameEdit = findViewById(R.id.roulette_name_edit);
                nameEdit.setText(name, TextView.BufferType.NORMAL);
            } finally {
                db.close();
            }
        }

        // itemList
        ListView itemListView = findViewById(R.id.item_list_view);

        this.itemList = new ArrayList<>();
        this.itemListAdapter = new ItemListAdapter(this);
        this.itemListAdapter.setItemList(this.itemList);
        itemListView.setAdapter(this.itemListAdapter);

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
                    addItemList();
                    ItemInfo item = this.itemList.get(this.itemList.size()-1);
                    item.setName(itemName);
                    item.setRatio(itemRatio);
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

        Button deleteItemButton = findViewById(R.id.delete_button);
        deleteItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItemList();
            }
        });

        Button registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isOkName()) {
                    NameCautionDialog dialog = new NameCautionDialog();
                    dialog.show(getSupportFragmentManager(), "caution");
                    return;
                }

                if (!isOkItemRatio()) {
                    RatioCautionDialog dialog = new RatioCautionDialog();
                    dialog.show(getSupportFragmentManager(), "caution");
                    return;
                }

                register();

                Intent intent = new Intent(EditRouletteActivity.this, com.tinkai.myroulette.RouletteListActivity.class);
                startActivity(intent);
            }
        });

        Button useButton = findViewById(R.id.use_button);
        useButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isOkName()) {
                    NameCautionDialog dialog = new NameCautionDialog();
                    dialog.show(getSupportFragmentManager(), "caution");
                    return;
                }

                if (!isOkItemRatio()) {
                    RatioCautionDialog dialog = new RatioCautionDialog();
                    dialog.show(getSupportFragmentManager(), "caution");
                    return;
                }

                register();

                Intent intent = new Intent(EditRouletteActivity.this, com.tinkai.myroulette.MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void addItemList() {
        if (this.itemList.size() >= 10) return;

        ItemInfo item = new ItemInfo();
        this.itemList.add(item);

        this.itemListAdapter.notifyDataSetChanged();
    }

    protected void deleteItemList() {
        if (this.itemList.size() <= 2) return;

        this.itemList.remove(this.itemList.size()-1);
        this.itemListAdapter.notifyDataSetChanged();
    }

    protected void register() {
        EditText nameEdit = findViewById(R.id.roulette_name_edit);
        String name = nameEdit.getText().toString();

        // DB登録
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            if (newFlag) {
                db.execSQL("update ROULETTE_TABLE set use = '0'");
                id = UUID.randomUUID().toString();
                db.execSQL("insert into ROULETTE_TABLE(uuid, name, use) VALUES('" + id + "', '" + name + "', '1')");

                Cursor c = db.rawQuery("select id from ROULETTE_TABLE where uuid = '" + id + "'", null);
                c.moveToFirst();
                String rouletteID = String.valueOf(c.getInt(0));

                db.execSQL("CREATE TABLE ROULETTE_ITEM_TABLE" + rouletteID + "(" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT, " +
                        "ratio TEXT)");
                registerItemTable(db, rouletteID);
            } else {
                db.execSQL("update ROULETTE_TABLE set use = '0'");
                db.execSQL("update ROULETTE_TABLE set name = '" + name + "', use = '1' where uuid = '" + id + "'");

                Cursor c = db.rawQuery("select id from ROULETTE_TABLE where uuid = '" + id + "'", null);
                c.moveToFirst();
                String rouletteID = String.valueOf(c.getInt(0));

                db.execSQL("delete from ROULETTE_ITEM_TABLE" + rouletteID);
                registerItemTable(db, rouletteID);
            }
        } finally {
            db.close();
        }
    }

    private void registerItemTable(SQLiteDatabase db, String rouletteID) {
        for (ItemInfo item : this.itemList) {
            String name = item.getName();
            String ratio = item.getRatio();
            db.execSQL("insert into ROULETTE_ITEM_TABLE" + rouletteID + "(name, ratio) VALUES('" + name + "', '" + ratio + "')");
        }
    }

    private boolean isOkItemRatio() {
        if (isAllEmptyRatio()) return true;

        int sum = 0;
        for (ItemInfo item : this.itemList) {
            String ratio = item.getRatio();
            // 数字か
            try {
                int num = Integer.parseInt(ratio);
                if (num <= 0 || num >= 100) return false;

                sum += num;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        // 合計が100か
        if (sum != 100) return false;
        return true;
    }

    private boolean isAllEmptyRatio() {
        for (ItemInfo item : this.itemList) {
            String ratio = item.getRatio();
            if (!ratio.equals("")) return false;
        }
        return true;
    }

    private boolean isOkName() {
        EditText rouletteNameText = findViewById(R.id.roulette_name_edit);
        String rouletteName = String.valueOf(rouletteNameText.getText());
        if (rouletteName.equals("")) return false;

        for (ItemInfo item : this.itemList) {
            String name = item.getName();
            if (name.equals("")) return false;
        }
        return true;
    }
}
