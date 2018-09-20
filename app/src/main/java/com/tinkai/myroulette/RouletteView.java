package com.tinkai.myroulette;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

public class RouletteView extends View {
    private Paint paint;
    private Paint textPaint;
    private int color[] = {Color.RED, Color.GREEN, Color.BLUE}; // 仮設定
    private String name[] = {"あああああ", "いいいいい", "ううううう"};
    private int num; // 項目数
    private int angle; // 一つの項目の角度
    private float rotationAngle; // 回転速度

    // 最終的にsettingクラスから設定を行うコンストラクタを作成する
    RouletteView(Context context, int num) {
        super(context);
        this.paint = new Paint();
        this.paint.setAntiAlias(true);
        this.textPaint = new Paint();
        this.textPaint.setAntiAlias(true);
        this.textPaint.setTextSize(70);
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
        // 分離させないと変な表示になる
        for (int i = 0; i < this.num; i++) {
            int textAngle;
            if(i == 0) {
                textAngle = this.angle / 2 + (int)this.rotationAngle;
            } else{
                textAngle = this.angle;
            }
            canvas.rotate(textAngle, centerX, centerY);
            canvas.drawText(name[i], centerX + radius/4, centerY + 20, this.textPaint);
        }


    }

    public void addRotationAngle(float rotation) {
        this.rotationAngle += rotation;
    }

}
