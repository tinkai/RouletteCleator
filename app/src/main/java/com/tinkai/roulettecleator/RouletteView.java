package com.tinkai.roulettecleator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class RouletteView extends View {
    private Paint paint;
    private Paint textPaint;
    private TextView resultView;
    private final int color[] = { // 基本12色
            Color.parseColor("#e60012"), Color.parseColor("#f39800"), Color.parseColor("#fff100"), Color.parseColor("#8fc31f"),
            Color.parseColor("#009944"), Color.parseColor("#009e96"), Color.parseColor("#00a0e9"), Color.parseColor("#0068b7"),
            Color.parseColor("#1d2088"), Color.parseColor("#920783"), Color.parseColor("#e4007f"), Color.parseColor("#e5004f")
    };
    private String name[];
    private float ratio[];
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
        this.ratio = new float[this.num];
        this.angle = new float[this.num];
        for (int i = 0; i < this.num; i++) {
            if (ratioArray[i].equals("")) {
                this.ratio[i] = 100 / this.num;
                this.angle[i] = 360 / (float)this.num;
            } else {
                this.ratio[i] = Float.parseFloat(ratioArray[i]);
                this.angle[i] = 360 * this.ratio[i] / 100;
            }
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

        // ルーレットアイテム名表示
        for (int i = 0; i < this.num; i++) {
            float textAngle;
            if(i == 0) {
                textAngle = this.angle[i] / 2 + this.rotationAngle + 90;
            } else{
                textAngle = (this.angle[i-1] + this.angle[i]) / 2;
            }
            canvas.rotate(textAngle, centerX, centerY);

            int halfSize = getStringLength(this.name[i]) / 2;
            canvas.drawText(this.name[i], centerX - 40 * halfSize, centerY - 3*radius/5, this.textPaint);
        }
    }

    public void addRotationAngle(float rotation) {
        this.rotationAngle += rotation;
    }

    public static int getStringLength(String str) {
        int length = 0;

        //全角半角判定
        char[] c = str.toCharArray();
        for(int i=0;i<c.length;i++) {
            if(String.valueOf(c[i]).getBytes().length <= 1){
                length += 1; //半角文字なら＋１
            }else{
                length += 2; //全角文字なら＋２
            }
        }
        return length;
    }
}
