package com.tinkai.myroulette;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.ColorRes;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RouletteView extends View {
    private Paint paint;
    private Paint textPaint;
    private TextView resultView;
    private final int color[] = {Color.parseColor("#ff7a7a"), Color.parseColor("#7affff"), Color.parseColor("#ff7aff"), Color.parseColor("#7affbc"), Color.parseColor("#bc7aff"),
            Color.parseColor("#7aff7a"), Color.parseColor("#7a7aff"), Color.parseColor("#bcff7a"), Color.parseColor("#7abcff"), Color.parseColor("#ffff7a")};
    //private final int color[] = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.LTGRAY,
      //      Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.LTGRAY}; // 仮設定
    private String name[];
    private int ratio[];
    private int num; // 項目数
    private float angle[]; // 一つの項目の角度
    private float rotationAngle; // 回転した角度

    RouletteView(Context context, TextView resultView) {
        super(context);
        this.paint = new Paint();
        this.paint.setAntiAlias(true);
        this.textPaint = new Paint();
        this.textPaint.setAntiAlias(true);
        this.textPaint.setTextSize(70);
        this.resultView = resultView;
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
        float rotateAngle = this.rotationAngle;
        float sumAngle = (this.rotationAngle + 90) % 360;
        //boolean isResult = true;
        for (int i = 0; i < this.num; i++) {
            this.paint.setColor(color[i]);
            canvas.drawArc(rectF, rotateAngle, this.angle[i], true, this.paint);
            rotateAngle += this.angle[i];

            sumAngle += this.angle[i];
            if (sumAngle > 360) {
                this.resultView.setText(this.name[i]);
                sumAngle = sumAngle % 360;
            }
        }

        // 下矢印
        this.paint.setColor(Color.BLACK);
        Path path = new Path();
        path.moveTo(centerX, centerY - radius + radius*0.1f);
        path.lineTo(centerX - radius*0.1f,centerY - radius - radius*0.1f);
        path.lineTo(centerX + radius*0.1f,centerY - radius - radius*0.1f);
        path.close();
        canvas.drawPath(path,paint);

        // ルーレットアイテム名表示 分離させないと変な表示になる
        for (int i = 0; i < this.num; i++) {
            float textAngle;
            if(i == 0) {
                textAngle = this.angle[i] / 2 + this.rotationAngle + 90;
            } else{
                textAngle = (this.angle[i-1] + this.angle[i]) / 2;
            }
            canvas.rotate(textAngle, centerX, centerY);
            canvas.drawText(this.name[i], centerX - 30, centerY - radius/2, this.textPaint);
        }


    }

    public void addRotationAngle(float rotation) {
        this.rotationAngle += rotation;
    }

}
