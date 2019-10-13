package ru.niceaska.speedometer;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class Speedomter extends View {

    private int speed;
    private int colorArrow;
    private int maxSpeed;
    private int colorLow;
    private int colorMid;
    private int colorHigh;

    private Paint arrowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint arcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint innerArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FAKE_BOLD_TEXT_FLAG);
    private Rect textBoundsRect = new Rect();

    private RectF arcRect = new RectF(0, 0, 500, 500);


    private final float STROKE_WIDTH = 48f;
    private final int MAX_ANGLE = 180;
    private final int LINE_LINEAGE = (int)(arcRect.width() / 2);

    public Speedomter(Context context) {
        super(context);
        init(context, null);

    }

    public Speedomter(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        obtainAttrs(context, attrs);
        initArcPaint();
        final float textSize = getContext().getResources()
                .getDimension(R.dimen.speedometr_text_size);
        arrowPaint.setColor(colorArrow);
        textPaint.setTextSize(textSize);
        textPaint.setColor(colorArrow);
        innerArcPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        innerArcPaint.setColor(Color.LTGRAY);

    }

    private void initArcPaint() {
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setColor(colorLow);
        arcPaint.setStrokeWidth(STROKE_WIDTH);
    }

    private void obtainAttrs(Context context, AttributeSet attrs) {
        final Resources.Theme theme  = context.getTheme();
        final TypedArray typedArray = theme.obtainStyledAttributes(attrs, R.styleable.Speedomter,
                0, R.style.speedometer_default_style);
        try {
            speed = typedArray.getInteger( R.styleable.Speedomter_speed, 0);
            colorArrow = typedArray.getInteger( R.styleable.Speedomter_color_arrow, 0);
            maxSpeed = typedArray.getInteger( R.styleable.Speedomter_max_speed, 0);
            colorLow = typedArray.getInteger( R.styleable.Speedomter_color_low, 0);
            colorMid = typedArray.getInteger( R.styleable.Speedomter_color_mid, 0);
            colorHigh = typedArray.getInteger( R.styleable.Speedomter_color_high, 0);
        } finally {
            typedArray.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(STROKE_WIDTH / 2, STROKE_WIDTH / 2);
        final float sweepAngle = speed * 1.0f / maxSpeed * MAX_ANGLE;
        final float startAngle = -180f;

        setSpeedColor();
        canvas.drawArc(arcRect, startAngle, sweepAngle, false, arcPaint);
        canvas.save();
        canvas.rotate(sweepAngle + startAngle, arcRect.width() / 2, arcRect.width() / 2);
        canvas.drawLine(arcRect.width() / 2, arcRect.width() / 2,
                arcRect.width() / 2 + LINE_LINEAGE, arcRect.width() / 2, arrowPaint);
        canvas.restore();

        canvas.drawCircle(arcRect.width() / 2, arcRect.height() / 2, arcRect.width() / 4, innerArcPaint);
        String str = String.format(getResources().getString(R.string.format_string), speed);
        textPaint.getTextBounds(str, 0, str.length(), textBoundsRect);
        float x = arcRect.width() / 2f - textBoundsRect.width() / 2f - textBoundsRect.left;
        float y = arcRect.height() / 2f + textBoundsRect.height() / 2f - textBoundsRect.bottom;
        canvas.drawText(str, x, y, textPaint);
    }

    private void setSpeedColor() {
        if (speed < maxSpeed / 3) {
            arcPaint.setColor(colorLow);
        } else if (speed < maxSpeed / 2) {
            arcPaint.setColor(colorMid);
        } else {
            arcPaint.setColor(colorHigh);
        }
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = (speed <= maxSpeed) ? speed : maxSpeed;
        invalidate();
    }

    public int getColorArrow() {
        return colorArrow;
    }

    public void setColorArrow(int colorArrow) {
        this.colorArrow = colorArrow;
        invalidate();
    }


    public int getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public int getColorLow() {
        return colorLow;
    }

    public void setColorLow(int colorLow) {
        this.colorLow = colorLow;
        invalidate();
    }

    public int getColorMid() {
        return colorMid;
    }

    public void setColorMid(int colorMid) {
        this.colorMid = colorMid;
        invalidate();
    }

    public int getColorHigh() {
        return colorHigh;
    }

    public void setColorHigh(int colorHigh) {
        this.colorHigh = colorHigh;
        invalidate();
    }

}
