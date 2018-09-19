package com.tinkai.myroulette;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

public class MainActivity extends AppCompatActivity {
    private GestureDetector gestureDetector; // フリックとか判定するやつ
    private RouletteView rouletteView; // ルーレット本体

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.gestureDetector = new GestureDetector(this, this.onGestureListener);

        this.rouletteView = new RouletteView(this, 3); // testように3個
        setContentView(this.rouletteView);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.gestureDetector.onTouchEvent(event);
    }

    private final GestureDetector.SimpleOnGestureListener onGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
            RotateAnimation rotate = new RotateAnimation(0.0f, 360.0f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);

            // animation時間 msec
            rotate.setDuration(3000);
            // animationが終わったそのまま表示にする
            rotate.setFillAfter(true);

            rouletteView.startAnimation(rotate);
            return false;
        }
    };
}
