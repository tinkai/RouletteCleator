package com.tinkai.myroulette;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

public class RouletteView extends View {
    private Paint paint;

    private int color[] = {Color.RED, Color.GREEN, Color.BLUE}; // 仮設定

    private int num; // 項目数
    private int angle; // 一つの項目の角度
    private int pos;

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

        RectF rectF = new RectF(100, centerY-centerX+100, (float)canvas.getWidth()-100, centerY+centerX-100);

        for (int i = 0; i < this.num; i++) {
            this.paint.setColor(color[i]);
            canvas.drawArc(rectF, (i * this.angle) + this.pos, this.angle, true, this.paint);
        }
    }

    public void addPos(int move) {
        this.pos += move;
    }

}
