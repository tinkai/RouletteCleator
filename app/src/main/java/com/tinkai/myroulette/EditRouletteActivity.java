package com.tinkai.myroulette;

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
    private ArrayList<LinearLayout> itemList;
    private ArrayList<EditText> itemNameList;
    private ArrayList<EditText> itemRatioList;
    private final int MAX_ITEM_NUM = 16;
    private final int MIN_ITEM_NUM = 2;

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
                    addItemList();
                    this.itemNameList.get(this.itemNameList.size()-1).setText(itemName, TextView.BufferType.NORMAL);
                    this.itemRatioList.get(this.itemRatioList.size()-1).setText(itemRatio, TextView.BufferType.NORMAL);
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
        if (this.itemList.size() >= MAX_ITEM_NUM) return;

        LinearLayout itemListLayout = findViewById(R.id.item_list);

        LinearLayout itemLayout = new LinearLayout(this);
        itemLayout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        itemLayout.setOrientation(LinearLayout.HORIZONTAL);
        this.itemList.add(itemLayout);

        TextView numText = new TextView(this);
        String strNum = String.valueOf(this.itemList.size());
        numText.setText(strNum + ".");
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

        itemListLayout.addView(itemLayout);
    }

    protected void deleteItemList() {
        if (this.itemList.size() <= MIN_ITEM_NUM) return;
        int last = this.itemList.size()-1;
        this.itemNameList.remove(last);
        this.itemRatioList.remove(last);
        this.itemList.get(last).removeAllViews();
        this.itemList.remove(last);
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
        try {
            for (int i = 0; i < this.itemNameList.size(); i++) {
                String itemName = String.valueOf(this.itemNameList.get(i).getText());
                String itemRatio = String.valueOf(this.itemRatioList.get(i).getText());
                if (!itemName.equals("")) {
                    db.execSQL("insert into ROULETTE_ITEM_TABLE" + rouletteID + "(name, ratio) VALUES('" + itemName + "', '" + itemRatio + "')");
                }
            }
        } finally {
            db.close();
        }
    }

    private boolean isOkItemRatio() {
        if (isAllEmptyRatio()) return true;

        int sum = 0;
        for (EditText ratioText : this.itemRatioList) {
            String ratio = String.valueOf(ratioText.getText());
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
        for (EditText ratioText : this.itemRatioList) {
            String ratio = String.valueOf(ratioText.getText());
            if (!ratio.equals("")) return false;
        }
        return true;
    }

    private boolean isOkName() {
        EditText rouletteNameText = findViewById(R.id.roulette_name_edit);
        String rouletteName = String.valueOf(rouletteNameText.getText());
        if (rouletteName.equals("")) return false;

        for (EditText nameText : this.itemNameList) {
            String name = String.valueOf(nameText.getText());
            if (name.equals("")) return false;
        }

        return true;
    }

}
