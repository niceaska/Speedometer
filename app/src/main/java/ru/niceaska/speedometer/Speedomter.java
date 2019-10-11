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
    private int color_arrow;
    private int max_speed;
    private int color_low;
    private int color_mid;
    private int color_high;

    private Paint arrowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint arcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint innerArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FAKE_BOLD_TEXT_FLAG);
    private Rect textBoudsRect = new Rect();

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
        arrowPaint.setColor(color_arrow);
        textPaint.setTextSize(36f);
        textPaint.setColor(color_arrow);
        innerArcPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        innerArcPaint.setColor(Color.LTGRAY);

    }

    private void initArcPaint() {
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setColor(color_low);
        arcPaint.setStrokeWidth(STROKE_WIDTH);
    }

    private void obtainAttrs(Context context, AttributeSet attrs) {
        final Resources.Theme theme  = context.getTheme();
        final TypedArray typedArray = theme.obtainStyledAttributes(attrs, R.styleable.Speedomter,
                0, R.style.speedometer_default_style);
        try {
            speed = typedArray.getInteger( R.styleable.Speedomter_speed, 0);
            color_arrow = typedArray.getInteger( R.styleable.Speedomter_color_arrow, 0);
            max_speed = typedArray.getInteger( R.styleable.Speedomter_max_speed, 0);
            color_low = typedArray.getInteger( R.styleable.Speedomter_color_low, 0);
            color_mid = typedArray.getInteger( R.styleable.Speedomter_color_mid, 0);
            color_high = typedArray.getInteger( R.styleable.Speedomter_color_high, 0);
        } finally {
            typedArray.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(STROKE_WIDTH / 2, STROKE_WIDTH / 2);
        final float sweepAngle = speed * 1.0f / max_speed * MAX_ANGLE;
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
        textPaint.getTextBounds(str, 0, str.length(), textBoudsRect);
        float x = arcRect.width() / 2f - textBoudsRect.width() / 2f - textBoudsRect.left;
        float y = arcRect.height() / 2f + textBoudsRect.height() / 2f - textBoudsRect.bottom;
        canvas.drawText(str, x, y, textPaint);
    }

    private void setSpeedColor() {
        if (speed < max_speed / 3) {
            arcPaint.setColor(color_low);
        } else if (speed < max_speed / 2) {
            arcPaint.setColor(color_mid);
        } else {
            arcPaint.setColor(color_high);
        }
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = (speed <= 400) ? speed : 400;
        invalidate();
    }

    public int getColor_arrow() {
        return color_arrow;
    }

    public void setColor_arrow(int color_arrow) {
        this.color_arrow = color_arrow;
        invalidate();
    }


    public int getMax_speed() {
        return max_speed;
    }

    public void setMax_speed(int max_speed) {
        this.max_speed = max_speed;
    }

    public int getColor_low() {
        return color_low;
    }

    public void setColor_low(int color_low) {
        this.color_low = color_low;
        invalidate();
    }

    public int getColor_mid() {
        return color_mid;
    }

    public void setColor_mid(int color_mid) {
        this.color_mid = color_mid;
        invalidate();
    }

    public int getColor_high() {
        return color_high;
    }

    public void setColor_high(int color_high) {
        this.color_high = color_high;
        invalidate();
    }

}
