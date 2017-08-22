package com.mad.blackbox.View;

/**
 * this class create a custom view for therometer.
 * create by tomat
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.mad.blackbox.R;

public class TempView extends View {

    private String temp;
    private float fireTem;
    private float iceTem;
    private Paint currentTmp;
    private Paint mPaint;
    private Paint textPaint;
    private Paint paintCircle;
    private Paint paintLine;
    private Bitmap bitmaplv;
    private Bitmap bitmaplan;
    private Bitmap bitmapred;
    private Bitmap fire;
    private Bitmap ice;
    private Context context;
    private int m;
    private Paint mPaintOther;

    public TempView(Context context, AttributeSet attrs) {
        super(context, attrs);
        currentTmp = new Paint();
        currentTmp.setAntiAlias(true);
        currentTmp.setTextSize(20);
        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#F2DED7"));
        // mPaint.setColor(Color.WHITE);
        mPaint.setAntiAlias(true);
        textPaint = new TextPaint();
        textPaint.setColor(Color.BLACK);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(18);
        paintCircle = new Paint();
        // paintCircle.setColor(Color.parseColor("#61BEE7"));
        paintCircle.setAntiAlias(true);
        paintCircle.setTextSize(45);
        Paint p = new Paint();
        p.setColor(Color.BLACK);
        p.setAntiAlias(true);
        paintLine = new Paint();
        paintLine.setStrokeWidth(2.5f);
        paintLine.setColor(Color.BLUE);
        bitmaplv = BitmapFactory.decodeResource(getResources(),
                R.drawable.kedu_s);
        bitmaplan = BitmapFactory.decodeResource(getResources(),
                R.drawable.kedu_lan_small);
        bitmapred = BitmapFactory.decodeResource(getResources(),
                R.drawable.kedu_red_small);
        Paint rightPaint = new Paint();
        rightPaint.setAntiAlias(true);
        fire = BitmapFactory.decodeResource(getResources(), R.drawable.fire);
        ice = BitmapFactory.decodeResource(getResources(),
                R.drawable.snow);
        mPaintOther = new Paint();
        mPaintOther.setColor(Color.parseColor("#030102"));
        mPaintOther.setAntiAlias(true);
        mPaintOther.setStrokeWidth(1);

    }


    public void setTemp(String temp) {
        this.temp = temp;
        this.fireTem = 19f;
        this.iceTem = 5f;
        this.m = 1000;
        this.invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, m + 120);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        @SuppressWarnings("deprecation")
        // help width of the screen
                int width = getWidth() / 2;
        // int screenHeight = getHeight();
        // int m = (int) (MyUtils.px2dp(context, screenHeight) * 1.1 + 30);
        // current temperature
        float tempCurrent = Float.parseFloat(temp);
        // get the current hight based on the temperature
        int yAxis = m - (int) ((tempCurrent - (0))* 50);
        int mFireHight = m - (int) ((fireTem - (0)) * 50);
//        Log.e("TAG", mFireHight + "????");
        textPaint.setColor(Color.parseColor("#f9847b"));
        // fire alarm
        canvas.drawBitmap(fire, width - 35 - 7 - 70 - 115,
                mFireHight - fire.getHeight() / 2 - 4, mPaint);
        //if the length of the temperature is bigger than 4
        if (String.valueOf(fireTem).length() > 4) {
            canvas.drawText(String.valueOf(fireTem).substring(0, 4) + "°C",
                    width - 35 - 7 - 150, mFireHight + 4, textPaint);
        } else {
            canvas.drawText(String.valueOf(fireTem) + "°C",
                    width - 35 - 7 - 150, mFireHight + 4, textPaint);
        }
        int mLowHight = m - (int) ((iceTem - (0)) * 50);
        textPaint.setColor(Color.parseColor("#479aed"));
        // ice alarm
        canvas.drawBitmap(ice, width - 35 - 7 - 70 - 115,
                mLowHight - ice.getHeight() / 2 - 4, mPaint);
        if (String.valueOf(iceTem).length() > 4) {
            canvas.drawText(String.valueOf(iceTem).substring(0, 4) + "°C",
                    width - 35 - 7 - 150, mLowHight + 4, textPaint);
        } else {
            canvas.drawText(String.valueOf(iceTem) + "°C",
                    width - 35 - 7 - 150, mLowHight + 4, textPaint);
        }
        // thermometer rectangle
        canvas.drawRect(width - 35, 0, width + 35, m, mPaint);
        // right hand side triangle
        // temperature too slow
        if (tempCurrent >= 0 && tempCurrent < iceTem) {
            currentTmp.setColor(Color.parseColor("#497aed"));
            paintCircle.setColor(Color.parseColor("#497aed"));
            //draw the rectangle that represent the temperature in the thermometer rectangle.
            canvas.drawRect(width - 35, yAxis, width + 35, m, paintCircle);
            // the bottom circle
            canvas.drawCircle(width, m + 50, 60, paintCircle);
            canvas.drawBitmap(bitmaplan, width + 40, yAxis - bitmaplan.getHeight()
                    / 2, mPaint);
            canvas.drawText(temp + "°C",
                    width + 40 + bitmaplan.getWidth() + 15,
                    yAxis + bitmaplan.getHeight() / 4, paintCircle);
//            Log.e("TAG", temp + "temp");
            canvas.drawText("Temperature", width + 40 + bitmaplan.getWidth() + 15 + 5,
                    yAxis + bitmaplan.getHeight() / 2 + 10 + 5, currentTmp);
            //this situation is for the temperature that is in normal
        } else if (tempCurrent >= iceTem && tempCurrent < fireTem) {
            currentTmp.setColor(Color.parseColor("#3DB475"));
            paintCircle.setColor(Color.parseColor("#3DB475"));
            canvas.drawRect(width - 35, yAxis, width + 35, m, paintCircle);
            canvas.drawCircle(width, m + 50, 60, paintCircle);
            canvas.drawBitmap(bitmaplv, width + 40, yAxis - bitmaplv.getHeight()
                    / 2, mPaint);
            canvas.drawText(temp + "°C", width + 40 + bitmaplv.getWidth() + 15,
                    yAxis + bitmaplv.getHeight() / 4, paintCircle);
            canvas.drawText("Temperature", width + 40 + bitmaplv.getWidth() + 15 + 5,
                    yAxis + bitmaplv.getHeight() / 2 + 10 + 5, currentTmp);

            //this case is for the fire alarm ( the temperature is too high)
        } else if ((tempCurrent + 0.1) >= fireTem) {
            paintCircle.setColor(Color.parseColor("#F65402"));
            currentTmp.setColor(Color.parseColor("#F65402"));
            canvas.drawRect(width - 35, yAxis, width + 35, m, paintCircle);
            canvas.drawCircle(width, m + 50, 60, paintCircle);
            canvas.drawBitmap(bitmapred, width + 40, yAxis - bitmaplv.getHeight()
                    / 2, mPaint);
            canvas.drawText(temp + "°C",
                    width + 40 + bitmapred.getWidth() + 15,
                    yAxis + bitmapred.getHeight() / 4, paintCircle);
            canvas.drawText("Temperature", width + 40 + bitmapred.getWidth() + 15 + 5,
                    yAxis + bitmapred.getHeight() / 2 + 10 + 5, currentTmp);
        } else if (tempCurrent < 0) {
            currentTmp.setColor(Color.parseColor("#497aed"));
            paintCircle.setColor(Color.parseColor("#497aed"));
            canvas.drawRect(width - 35, yAxis, width + 35, m, paintCircle);
            canvas.drawCircle(width, m + 50, 60, paintCircle);
            canvas.drawBitmap(bitmaplan, width + 40, m - bitmaplan.getHeight()
                    / 2, mPaint);
            canvas.drawText(temp + "°C",
                    width + 40 + bitmaplan.getWidth() + 15,
                    m + bitmaplan.getHeight() / 4, paintCircle);
//            Log.e("TAG", temp + "temp");
            canvas.drawText("Temperature", width + 40 + bitmaplan.getWidth() + 15 + 5 + 25,
                    m + bitmaplan.getHeight() / 2 + 10 + 5, currentTmp);
        }
        int ydegree = m;
//        Log.e("TAG", ydegree + "");
        float tem = 0;

        // this while loop generates the thermometer.
        while (ydegree > 15) {
            canvas.drawLine(width - 35 - 10, ydegree, width - 35, ydegree, mPaintOther);
            if (ydegree % 10 == 0) {
                canvas.drawLine(width - 35 - 18, ydegree, width - 35, ydegree,
                        paintLine);
                textPaint.setColor(Color.parseColor("#FFFFFF"));
                canvas.drawText(tem + "°C", width - 35 - 7 - 90, ydegree + 4,
                        textPaint);
                tem++;

            }
            ydegree = ydegree - 25;
        }
    }
}
