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
    private Paint circlePaint;

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

    private void initial() {
        Resources resources = getResources();
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(resources.getColor(R.color.circle_color));
//        circlePaint.setStyle(Paint.Style.STROKE);
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
        int px = width / 2;
        int py = height / 2;
        int radius = Math.min(px, py);

        canvas.drawCircle(px, py, radius, circlePaint);
    }
}
