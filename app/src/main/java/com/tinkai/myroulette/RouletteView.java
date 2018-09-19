package com.tinkai.myroulette;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

public class RouletteView extends View {
    private Paint paint;
    private RectF rectF;

    private int color[] = {Color.RED, Color.GREEN, Color.BLUE}; // 仮設定

    private int num; // 項目数
    private int angle; // 一つの項目の角度

    private int rotationAngle; // 回転している角度

    // 最終的にsettingクラスから設定を行うコンストラクタを作成する
    RouletteView(Context context, int num) {
        super(context);
        this.paint = new Paint();
        this.paint.setAntiAlias(true);
        this.num = num;
        this.angle = 360 / num;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float centerX = canvas.getWidth() / 2;
        float centerY = canvas.getHeight() / 2;
        float radius = canvas.getWidth() / 2 * 0.9f;

        RectF rectF = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);

        for (int i = 0; i < this.num; i++) {
            this.paint.setColor(color[i]);
            canvas.drawArc(rectF, (i * this.angle) + this.rotationAngle, this.angle, true, this.paint);
        }
    }

    public void addRotationAngle(int rotation) {
        this.rotationAngle += rotation;
    }

}
