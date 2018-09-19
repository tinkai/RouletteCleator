package com.tinkai.myroulette;

import android.view.animation.Animation;
import android.view.animation.Transformation;

public class VectorRotateAnimation extends Animation {
    private RouletteView rouletteView;
    private int velocity;

    VectorRotateAnimation(RouletteView rouletteView, int velocity) {
        this.rouletteView = rouletteView;
        this.velocity = velocity;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation transformation) {
        this.velocity = (int)(this.velocity * (1.0f - interpolatedTime));

        this.rouletteView.addRotationAngle(this.velocity);
        this.rouletteView.requestLayout();

        if(this.velocity == 0) cancel();
    }

}
