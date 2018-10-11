package com.tinkai.roulettecleator;

import android.view.animation.Animation;
import android.view.animation.Transformation;

public class VectorRotateAnimation extends Animation {
    private RouletteView rouletteView;
    private float velocity;

    VectorRotateAnimation(RouletteView rouletteView, float velocity) {
        this.rouletteView = rouletteView;
        this.velocity = velocity;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation transformation) {
        this.velocity = (this.velocity * 0.98f);
        if (this.velocity < 0.01f) this.velocity = 0.0f;

        this.rouletteView.addRotationAngle(this.velocity);
        this.rouletteView.requestLayout();

        if(this.velocity == 0) cancel();
    }

}
