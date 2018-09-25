package com.tinkai.myroulette;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RouletteView extends View {
    private Paint paint;
    private Paint textPaint;
    private TextView resultView;
    private int color[] = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.LTGRAY,
            Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.LTGRAY}; // 仮設定
    private String name[];
    private int ratio[];
    private int num; // 項目数
    private float angle[]; // 一つの項目の角度
    private float rotationAngle; // 回転速度

    RouletteView(Context context, TextView resultView) {
        super(context);
        this.paint = new Paint();
        this.paint.setAntiAlias(true);
        this.textPaint = new Paint();
        this.textPaint.setAntiAlias(true);
        this.textPaint.setTextSize(70);
        this.resultView = resultView;
    }

    // 最終的にsettingクラスから設定を行うコンストラクタを作成する
    RouletteView(Context context, TextView resultView, int num) {
        this(context, resultView);
        this.name = new String[3];
        this.name[0] = "aaa";
        this.name[1] = "bbb";
        this.name[2] = "ccc";
        this.num = num;
        this.angle[0] = 360 / num;
        this.angle[1] = 360 / num;
        this.angle[2] = 360 / num;
    }

    RouletteView(Context context, TextView resultView, String[] nameArray, String[] ratioArray) {
        this(context, resultView);
        this.num = nameArray.length;
        this.name = nameArray;
        this.ratio = new int[this.num];
        this.angle = new float[this.num];
        for (int i = 0; i < this.num; i++) {
            this.ratio[i] = Integer.parseInt(ratioArray[i]);
            this.angle[i] = 360 * Float.parseFloat(ratioArray[i]) / 100;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float centerX = canvas.getWidth() / 2;
        float centerY = canvas.getHeight() / 2;
        float radius = centerX * 0.9f;

        RectF rectF = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);

        // ルーレット描写
        float sumAngle = 0.0f;
        for (int i = 0; i < this.num; i++) {
            this.paint.setColor(color[i]);
            canvas.drawArc(rectF, sumAngle + this.rotationAngle, this.angle[i], true, this.paint);
            sumAngle += this.angle[i];
        }
        // ルーレットアイテム名表示 分離させないと変な表示になる
        float canvasRotateValue = 0.0f;
        for (int i = 0; i < this.num; i++) {
            float textAngle;
            if(i == 0) {
                textAngle = this.angle[i] / 2 + this.rotationAngle + 90;
            } else{
                textAngle = (this.angle[i-1] + this.angle[i]) / 2;
            }
            canvas.rotate(textAngle, centerX, centerY);
            canvas.drawText(this.name[i], centerX - 30, centerY - radius/2, this.textPaint);
            /*
            canvasRotateValue += this.angle[i];
            if ((int)canvasRotateValue > 359) {
                this.resultView.setText(this.name[i]);
            }
            */
        }


    }

    public void addRotationAngle(float rotation) {
        this.rotationAngle += rotation;
    }

}
