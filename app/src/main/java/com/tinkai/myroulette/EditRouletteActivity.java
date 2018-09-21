package com.tinkai.myroulette;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.UUID;

public class EditRouletteActivity extends AppCompatActivity {
    RouletteOpenHelper helper = null;
    boolean newFlag = false;
    private String id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_roulette);

        if (helper == null) {
            helper = new RouletteOpenHelper(EditRouletteActivity.this);
        }

        Intent intent = this.getIntent();
        this.id = intent.getStringExtra("id");
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

        Button registerButton = findViewById(R.id.register_button);
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
                    } else {
                        db.execSQL("update ROULETTE_TABLE set name = '" + name + "' where uuid = '" + id + "'");
                    }
                } finally {
                    db.close();
                }

                Intent intent = new Intent(EditRouletteActivity.this, com.tinkai.myroulette.RouletteListActivity.class);
                startActivity(intent);
            }
        });
    }

}
