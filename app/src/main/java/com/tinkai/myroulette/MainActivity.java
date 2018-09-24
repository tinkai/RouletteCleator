package com.tinkai.myroulette;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private GestureDetector gestureDetector; // フリックとか判定するやつ
    private RouletteView rouletteView;
    RouletteOpenHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.gestureDetector = new GestureDetector(this, this.onGestureListener);

        LinearLayout rouletteLayout = findViewById(R.id.roulette_layout);

        if (helper == null) {
            helper = new RouletteOpenHelper(MainActivity.this);
        }
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            Cursor c = db.rawQuery("select id, uuid, name from ROULETTE_TABLE where use = 1", null);
            boolean next = c.moveToFirst();
            if (!next) {
                this.rouletteView = new RouletteView(this, 3); // testように3個
            } else {
                String name = c.getString(2);
                TextView rouletteNameView = findViewById(R.id.roulette_name_view);
                rouletteNameView.setText(name);

                String uuid = c.getString(1);
                ArrayList<String> nameList = new ArrayList<>();
                //String itemName[] = new

                String rouletteID = String.valueOf(c.getInt(0));
                c = db.rawQuery("select name, ratio from ROULETTE_ITEM_TABLE" + rouletteID, null);
                boolean nextItem = c.moveToFirst();
                while (nextItem) {
                    String itemName = c.getString(0);
                    String itemRatio = c.getString(1);
                    nameList.add(itemName);
                    nextItem = c.moveToNext();
                }
                this.rouletteView = new RouletteView(this, nameList);
            }
        } finally {
            db.close();
        }

        rouletteLayout.addView(this.rouletteView, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        Button settingButton = findViewById(R.id.setting_button);
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, com.tinkai.myroulette.RouletteListActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.gestureDetector.onTouchEvent(event);
    }

    private final GestureDetector.SimpleOnGestureListener onGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
            float vector = (float)(Math.sqrt(Math.pow(velocityX, 2) + Math.pow(velocityY, 2)));

            VectorRotateAnimation animation = new VectorRotateAnimation(rouletteView, vector);
            animation.setDuration(10000); // ミリ秒 10秒
            animation.setFillAfter(true);
            rouletteView.startAnimation(animation);
            return false;
        }
    };

}
