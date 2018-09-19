package com.tinkai.myroulette;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {
    private final int MAX_VECTOR = 300;
    private GestureDetector gestureDetector; // フリックとか判定するやつ
    private RouletteView rouletteView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.gestureDetector = new GestureDetector(this, this.onGestureListener);

        LinearLayout rouletteLayout = findViewById(R.id.roulette_layout);
        this.rouletteView = new RouletteView(this, 3); // testように3個
        this.rouletteView.setBackgroundColor(Color.YELLOW);
        rouletteLayout.addView(this.rouletteView, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        Button settingButton = findViewById(R.id.setting_button);
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
            int vector = (int)(Math.sqrt(Math.pow(velocityX, 2) + Math.pow(velocityY, 2)));
            if (vector > MAX_VECTOR) vector = MAX_VECTOR;
            vector *= (int)vector*0.8;
            VectorRotateAnimation animation = new VectorRotateAnimation(rouletteView, vector);
            animation.setDuration(3000); // ミリ秒
            animation.setFillAfter(true);
            rouletteView.startAnimation(animation);
            return false;
        }
    };

}
