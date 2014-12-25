package com.henryzhefeng.stopwatch;

import android.animation.Animator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by 哲 on 12/22/2014.
 */
public class StopwatchView extends View {

    // Painters
    private Paint markerPaint;
    private Paint textPaint;
    private Paint secClockCirclePaint;
    private Paint secClockJointPaint;
    private Paint secClockPointerPaint;
    private Paint trianglePaint;

    private double innerAngle;
    private double outerAngle;
    private double oldOuterAngle;
    private int tenthOfSec;
    private int seconds;
    private int minutes;

    private ValueAnimator animator;

    // initialize private resources
    private void initial() {
        Resources resources = getResources();
        markerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        markerPaint.setColor(resources.getColor(R.color.marker_color));
        markerPaint.setStyle(Paint.Style.STROKE);
        markerPaint.setStrokeWidth(3.0f);
        markerPaint.setAlpha(140);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(resources.getColor(R.color.time_text_color));
        textPaint.setTextSize(resources.getInteger(R.integer.text_size));

        secClockCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        secClockCirclePaint.setColor(resources.getColor(R.color.sec_clock_circle_color));
        secClockCirclePaint.setStyle(Paint.Style.STROKE);
        secClockCirclePaint.setStrokeWidth(3.0f);
        secClockCirclePaint.setAlpha(140);

        secClockJointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        secClockJointPaint.setColor(resources.getColor(R.color.sec_clock_inner_color));
        secClockJointPaint.setStyle(Paint.Style.STROKE);
        secClockJointPaint.setStrokeWidth(4.0f);

        secClockPointerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        secClockPointerPaint.setColor(resources.getColor(R.color.sec_clock_inner_color));
        secClockPointerPaint.setStyle(Paint.Style.STROKE);
        secClockPointerPaint.setStrokeWidth(2.0f);

        trianglePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        trianglePaint.setColor(resources.getColor(R.color.triangle_indicator_color));
    }

    public StopwatchView(Context context) {
        super(context);
        initial();
    }

    public StopwatchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initial();
    }

    public StopwatchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initial();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredWidth = getMeasuredValue(widthMeasureSpec);
        int measuredHeight = getMeasuredValue(heightMeasureSpec);
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    // helper function which calculates width and height.
    private int getMeasuredValue(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        int result;
        if (specMode == MeasureSpec.AT_MOST) {
            // parent wish us be as big as possible
            result = specSize;
        } else if (specMode == MeasureSpec.EXACTLY) {
            // parent wish exactly layout this control.
            result = specSize;
        } else {
            // not specified
            result = 200;
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        float centerX = width / 2;
        float centerY = height / 2;

        // Draw markers.
        double angle = 0.0;
        // draw params which can be adjusted.
        float radiusMarker = Math.min(centerX, centerY) * 2 / 3;
        float markerLen = 50.0f;
        // use 1/4 second as marker unit
        double delta = (Math.PI / 30) / 2 / 2;
        for (int i = 0; i < 2 * Math.PI / delta; i++) {
            float startX = (float) (centerX + radiusMarker * Math.sin(angle));
            float startY = (float) (centerY - radiusMarker * Math.cos(angle));
            float endX = (float) (centerX + (radiusMarker - markerLen) * Math.sin(angle));
            float endY = (float) (centerY - (radiusMarker - markerLen) * Math.cos(angle));
            canvas.drawLine(startX, startY, endX, endY, markerPaint);
            angle += delta;
        }

        // Draw the triangle indicator
        float tgLen = 40.0f;
        float tgRadius = radiusMarker + 10.0f;
        Path tgPath = new Path();
        float pX = (float) (centerX + tgRadius * Math.sin(outerAngle));
        float pY = (float) (centerY - tgRadius * Math.cos(outerAngle));
        tgPath.moveTo(pX, pY);
        pX = (float) (pX + tgLen * Math.sin(outerAngle - Math.PI / 6));
        pY = (float) (pY - tgLen * Math.cos(outerAngle - Math.PI / 6));
        tgPath.lineTo(pX, pY);
        pX = (float) (pX + tgLen * Math.cos(outerAngle));
        pY = (float) (pY + tgLen * Math.sin(outerAngle));
        tgPath.lineTo(pX, pY);
        canvas.drawPath(tgPath, trianglePaint);

        // Draw time text
        String timeText = String.format("%02d:%02d.%01d", minutes, seconds, tenthOfSec);
        float textWidth = textPaint.measureText(timeText);
        canvas.drawText(timeText, centerX - textWidth / 2, centerY, textPaint);

        // Draw small clock for seconds
        float innerCenterX = centerX;
        float innerCenterY = centerY + radiusMarker / 2;
        float radiusSec = radiusMarker / 6;
        // Draw outline circle
        canvas.drawCircle(innerCenterX, innerCenterY, radiusSec, secClockCirclePaint);
        // Draw inner joint
        float radiusJoint = radiusSec / 10;
        canvas.drawCircle(innerCenterX, innerCenterY, radiusJoint, secClockJointPaint);
        // Draw pointer
        float startX = (float) (innerCenterX + radiusJoint * Math.sin(innerAngle));
        float startY = (float) (innerCenterY - radiusJoint * Math.cos(innerAngle));
        float endX = (float) (innerCenterX + radiusSec * Math.sin(innerAngle));
        float endY = (float) (innerCenterY - radiusSec * Math.cos(innerAngle));
        canvas.drawLine(startX, startY, endX, endY, secClockPointerPaint);
    }

    public void start() {
        TypeEvaluator<Double> evaluator = new TypeEvaluator<Double>() {
            @Override
            public Double evaluate(float fraction, Double startValue, Double endValue) {
                return startValue + (endValue - startValue) * fraction;
            }
        };
        animator = ValueAnimator.ofObject(evaluator, 0.0, 2 * Math.PI);
        animator.setDuration(1000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                innerAngle = (Double) animation.getAnimatedValue();
                outerAngle = oldOuterAngle + innerAngle / 60;
                // to get the right tenthOfSec
                float fraction = animation.getAnimatedFraction();
                tenthOfSec = (int) (fraction * 10);

                // force to re-draw stopwatch
                StopwatchView.this.invalidate();
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
                                 @Override
                                 public void onAnimationStart(Animator animation) {
                                     oldOuterAngle = outerAngle;
                                 }

                                 @Override
                                 public void onAnimationEnd(Animator animation) {
                                     // reset
                                     tenthOfSec = 0;
                                     seconds = 0;
                                     minutes = 0;
                                     innerAngle = 0;
                                     StopwatchView.this.invalidate();
                                 }

                                 @Override
                                 public void onAnimationCancel(Animator animation) {
                                 }

                                 @Override
                                 public void onAnimationRepeat(Animator animation) {
                                     ++seconds;
                                     if (seconds >= 60) {
                                         ++minutes;
                                         seconds = 0;
                                     }
                                     innerAngle = 0;
                                     tenthOfSec = 0;
                                     oldOuterAngle = outerAngle % (2 * Math.PI);
                                     StopwatchView.this.invalidate();
                                 }
                             }
        );
        animator.start();
    }

    public void pause() {
        if (animator != null) {
            animator.pause();
        }
    }

    public void resume() {
        if (animator != null) {
            animator.resume();
        }
    }

    public void reset() {
        if (animator != null) {
            animator.end();
        }
    }


}
