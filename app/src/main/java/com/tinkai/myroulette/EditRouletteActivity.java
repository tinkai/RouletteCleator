package com.tinkai.myroulette;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.UUID;

public class EditRouletteActivity extends AppCompatActivity {
    RouletteOpenHelper helper = null;
    private boolean newFlag = false;
    private String id = "";

    private LinearLayout itemListLayout;
    private RouletteItemList rouletteItemList;

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
                EditText nameEdit = findViewById(R.id.roulette_name_edit_text);
                nameEdit.setText(name, TextView.BufferType.NORMAL);
            } finally {
                db.close();
            }
        }

        // itemList
        this.itemListLayout = findViewById(R.id.item_list_layout);
        this.rouletteItemList = new RouletteItemList();

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
                    this.rouletteItemList.setItemText(this.rouletteItemList.getSize()-1, itemName, itemRatio);
                    next = c.moveToNext();
                }
            } finally {
                db.close();
            }
        }

        // Button
        Button addItemButton = findViewById(R.id.add_button);
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
                if (!isValidItemNum()) {
                    CautionDialog dialog = new CautionDialog(3);
                    dialog.show(getSupportFragmentManager(), "caution");
                    return;
                } else if (!isValidRouletteName()) {
                    CautionDialog dialog = new CautionDialog(0);
                    dialog.show(getSupportFragmentManager(), "caution");
                    return;
                } else if (!isValidItemName()) {
                    CautionDialog dialog = new CautionDialog(1);
                    dialog.show(getSupportFragmentManager(), "caution");
                    return;
                } else if (!isValidItemRatio()) {
                    CautionDialog dialog = new CautionDialog(2);
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
                if (!isValidItemNum()) {
                    CautionDialog dialog = new CautionDialog(3);
                    dialog.show(getSupportFragmentManager(), "caution");
                    return;
                } else if (!isValidRouletteName()) {
                    CautionDialog dialog = new CautionDialog(0);
                    dialog.show(getSupportFragmentManager(), "caution");
                    return;
                } else if (!isValidItemName()) {
                    CautionDialog dialog = new CautionDialog(1);
                    dialog.show(getSupportFragmentManager(), "caution");
                    return;
                } else if (!isValidItemRatio()) {
                    CautionDialog dialog = new CautionDialog(2);
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
        if (this.rouletteItemList.isOverMax()) return;

        View itemView = getLayoutInflater().inflate(R.layout.layout_item_row, null);

        EditText itemNameEditText = itemView.findViewById(R.id.item_name_edit_text);
        EditText itemRatioEditText = itemView.findViewById(R.id.item_ratio_edit_text);
        this.rouletteItemList.addItemList(itemNameEditText, itemRatioEditText);

        TextView itemNumTextView = itemView.findViewById(R.id.item_num_text_view);
        itemNumTextView.setText(String.valueOf(this.rouletteItemList.getSize()) + ".");

        this.itemListLayout.addView(itemView);
    }

    protected void deleteItemList() {
        if (this.rouletteItemList.isUnderMin()) return;

        this.rouletteItemList.removeItemList(this.rouletteItemList.getSize()-1);

        this.itemListLayout.removeViewAt(this.rouletteItemList.getSize());
    }

    protected void register() {
        EditText nameEdit = findViewById(R.id.roulette_name_edit_text);
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
            for (int i = 0; i < this.rouletteItemList.getSize(); i++) {
                String itemName = this.rouletteItemList.getStringName(i);
                String itemRatio = this.rouletteItemList.getStringRatio(i);
                if (!itemName.equals("")) {
                    db.execSQL("insert into ROULETTE_ITEM_TABLE" + rouletteID + "(name, ratio) VALUES('" + itemName + "', '" + itemRatio + "')");
                }
            }
        } finally {
            db.close();
        }
    }

    private boolean isValidItemRatio() {
        if (isAllEmptyRatio()) return true;

        int sumRatio = 0;
        for (int i = 0; i < this.rouletteItemList.getSize(); i++) {
            String ratio = this.rouletteItemList.getStringRatio(i);
            // 数字か
            try {
                int num = Integer.parseInt(ratio);
                if (num <= 0 || num >= 100) return false;

                sumRatio += num;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        // 合計が100か
        if (sumRatio != 100) return false;

        return true;
    }

    private boolean isAllEmptyRatio() {
        for (int i = 0; i < this.rouletteItemList.getSize(); i++) {
            String ratio = this.rouletteItemList.getStringRatio(i);
            if (!ratio.equals("")) return false;
        }
        return true;
    }

    private boolean isValidRouletteName() {
        EditText rouletteNameText = findViewById(R.id.roulette_name_edit_text);
        String rouletteName = String.valueOf(rouletteNameText.getText());
        if (rouletteName.equals("")) return false;
        return true;
    }

    private boolean isValidItemName() {
        for (int i = 0; i < this.rouletteItemList.getSize(); i++) {
            String name = this.rouletteItemList.getStringName(i);
            if (name.equals("")) return false;
        }
        return true;
    }

    private boolean isValidItemNum() {
        return this.rouletteItemList.getSize() >= 2;
    }
}
