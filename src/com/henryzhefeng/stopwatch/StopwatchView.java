package com.henryzhefeng.stopwatch;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by 哲 on 12/22/2014.
 */
public class StopwatchView extends View {
    private Paint markerPaint;
    private Paint textPaint;
    private Paint secClockCirclePaint;
    private Paint secClockJointPaint;
    private Paint secClockPointerPaint;

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
        // use 1/4 second as unit
        double delta = (Math.PI / 30) / 2 / 2;
        for (int i = 0; i < 2 * Math.PI / delta; i++) {
            float startX = (float) (centerX + radiusMarker * Math.sin(angle));
            float startY = (float) (centerY - radiusMarker * Math.cos(angle));
            float endX = (float) (centerX + (radiusMarker - markerLen) * Math.sin(angle));
            float endY = (float) (centerY - (radiusMarker - markerLen) * Math.cos(angle));
            canvas.drawLine(startX, startY, endX, endY, markerPaint);
            angle += delta;
        }

        // Draw time text
        String timeText = "00:00.0";
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
        canvas.drawLine(innerCenterX, innerCenterY - radiusJoint, innerCenterX, innerCenterY - radiusSec, secClockPointerPaint);

    }
}
