package com.mad.blackbox.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.View;

import com.mad.blackbox.R;

/**
 * this class draws a custom view for level UI
 * Created by tomat on 2017-05-29.
 */

public class LevelView extends View {
    public LevelView(Context context) {
        super(context);
    }

    private float mLimitRadius = 0;
    private float mBubbleRadius;
    private int mLimitColor;
    private float mLimitCircleWidth;
    private int mBubbleRuleColor;
    private float mBubbleRuleWidth;
    private float mBubbleRuleRadius;
    private int mHorizontalColor;
    private int mBubbleColor;

    private Paint mBubblePaint;
    private Paint mLimitPaint;
    private Paint mBubbleRulePaint;
    private PointF centerPnt = new PointF();
    private PointF bubblePoint;
    private double pitchAngle = -90;
    private double rollAngle = -90;
    private Vibrator vibrator;

    public LevelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public LevelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.LevelView, defStyle, 0);

        mBubbleRuleColor = a.getColor(R.styleable.LevelView_bubbleRuleColor, mBubbleRuleColor);
        mBubbleColor = a.getColor(R.styleable.LevelView_bubbleColor, mBubbleColor);
        mLimitColor = a.getColor(R.styleable.LevelView_limitColor, mLimitColor);
        mHorizontalColor = a.getColor(R.styleable.LevelView_horizontalColor, mHorizontalColor);
        mLimitRadius = a.getDimension(R.styleable.LevelView_limitRadius, mLimitRadius);
        mBubbleRadius = a.getDimension(R.styleable.LevelView_bubbleRadius, mBubbleRadius);
        mLimitCircleWidth = a.getDimension(R.styleable.LevelView_limitCircleWidth, mLimitCircleWidth);
        mBubbleRuleWidth = a.getDimension(R.styleable.LevelView_bubbleRuleWidth, mBubbleRuleWidth);
        mBubbleRuleRadius = a.getDimension(R.styleable.LevelView_bubbleRuleRadius, mBubbleRuleRadius);

        a.recycle();

        mBubblePaint = new Paint();

        mBubblePaint.setColor(mBubbleColor);
        mBubblePaint.setStyle(Paint.Style.FILL);
        mBubblePaint.setAntiAlias(true);

        mLimitPaint = new Paint();

        mLimitPaint.setStyle(Paint.Style.STROKE);
        mLimitPaint.setColor(mLimitColor);
        mLimitPaint.setStrokeWidth(mLimitCircleWidth);
        mLimitPaint.setAntiAlias(true);

        mBubbleRulePaint = new Paint();
        mBubbleRulePaint.setColor(mBubbleRuleColor);
        mBubbleRulePaint.setStyle(Paint.Style.STROKE);
        mBubbleRulePaint.setStrokeWidth(mBubbleRuleWidth);
        mBubbleRulePaint.setAntiAlias(true);

        vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        calculateCenter(widthMeasureSpec, heightMeasureSpec);
    }

    private void calculateCenter(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.makeMeasureSpec(widthMeasureSpec, MeasureSpec.UNSPECIFIED);
        int height = MeasureSpec.makeMeasureSpec(heightMeasureSpec, MeasureSpec.UNSPECIFIED);
        int center = Math.min(width, height) / 2;
        centerPnt.set(center, center);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        boolean isCenter = isCenter(bubblePoint);
        int limitCircleColor = isCenter ? mHorizontalColor : mLimitColor;
        int bubbleColor = isCenter ? mHorizontalColor : mBubbleColor;

        mBubblePaint.setColor(bubbleColor);
        mLimitPaint.setColor(limitCircleColor);

        canvas.drawCircle(centerPnt.x, centerPnt.y, mBubbleRuleRadius, mBubbleRulePaint);
        canvas.drawCircle(centerPnt.x, centerPnt.y, mLimitRadius, mLimitPaint);

        drawBubble(canvas);

    }

    private boolean isCenter(PointF bubblePoint){

        if(bubblePoint == null){
            return false;
        }

        return Math.abs(bubblePoint.x - centerPnt.x) < 1 && Math.abs(bubblePoint.y - centerPnt.y) < 1;
    }

    private void drawBubble(Canvas canvas) {
        if(bubblePoint != null){
            canvas.drawCircle(bubblePoint.x, bubblePoint.y, mBubbleRadius, mBubblePaint);
        }
    }

    /**
     * Convert angle to screen coordinate point.
     * @param rollAngle
     * @param pitchAngle
     * @return
     */
    private PointF convertCoordinate(double rollAngle, double pitchAngle, double radius){
        double scale = radius / Math.toRadians(90);

        //the center of the circle as the origin, use the radians to represent the coordinates
        double x0 = -(rollAngle * scale);
        double y0 = -(pitchAngle * scale);

        //Use the screen coordinates to indicate the bubble point
        double x = centerPnt.x - x0;
        double y = centerPnt.y - y0;

        return new PointF((float)x, (float)y);
    }

    /**
     *
     * @param pitchAngle
     * @param rollAngle
     */
    public void setAngle(double rollAngle, double pitchAngle) {

        this.pitchAngle = pitchAngle;
        this.rollAngle = rollAngle;

        //Consider that the bubble boundary does not exceed the limit circle, where the radius of the bubble is subtracted as the final limit radius
        float limitRadius = mLimitRadius - mBubbleRadius;

        bubblePoint = convertCoordinate(rollAngle, pitchAngle, mLimitRadius);
        outLimit(bubblePoint, limitRadius);

        //Coordinates beyond the maximum circle, take the point to the circle
        if(outLimit(bubblePoint, limitRadius)){
            onCirclePoint(bubblePoint, limitRadius);
        }

        invalidate();
    }

    /**
     * test if the bubble point is out of limit {@link #mLimitRadius}
     * @param bubblePnt
     * @return
     */
    private boolean outLimit(PointF bubblePnt, float limitRadius){

        float cSqrt = (bubblePnt.x - centerPnt.x)*(bubblePnt.x - centerPnt.x)
                + (centerPnt.y - bubblePnt.y) * + (centerPnt.y - bubblePnt.y);


        if(cSqrt - limitRadius * limitRadius > 0){
            return true;
        }
        return false;
    }

    /**
     * calculate the coordinates from the center of the circle to the bubble point
     * and the limit coordinates when out of the circle
     * @param bubblePnt
     * @param limitRadius
     * @return
     */
    private PointF onCirclePoint(PointF bubblePnt, double limitRadius) {
        double azimuth = Math.atan2((bubblePnt.y - centerPnt.y), (bubblePnt.x - centerPnt.x));
        azimuth = azimuth < 0 ? 2 * Math.PI + azimuth : azimuth;

        double x1 = centerPnt.x + limitRadius * Math.cos(azimuth);
        double y1 = centerPnt.y + limitRadius * Math.sin(azimuth);

        bubblePnt.set((float) x1, (float) y1);

        return bubblePnt;
    }

    public double getPitchAngle(){
        return this.pitchAngle;
    }

    public double getRollAngle(){
        return this.rollAngle;
    }

}
