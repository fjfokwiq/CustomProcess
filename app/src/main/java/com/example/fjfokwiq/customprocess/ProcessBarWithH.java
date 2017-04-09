package com.example.fjfokwiq.customprocess;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;


public class ProcessBarWithH extends ProgressBar {
    private static final int STYLE_RECT = 0;
    private static final int STYLE_CIRCLE = 1;

    private int mRadius = dpChanger(20);
    private int mUnReachColor = 0xFFFC00D1;
    private int mUnReachHeight = dpChanger(2);
    private int mReachColor = 0xFFD3D6DA;
    private int mReachHeight = dpChanger(2);
    private int mTextSize = dpChanger(10);
    private int mTextColor = 0xFFD3D6DA;
    private int mTextOffset = dpChanger(10);
    private Styles shape = Styles.RECT;

    private Paint mPaint = new Paint();

    private int mRealWidth;

    private int mPaintMaxWidth;

    public ProcessBarWithH(Context context) {
        this(context, null);
    }

    public ProcessBarWithH(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProcessBarWithH(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        obtainCustomAttrs(attrs);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        canvas.save();
        if (shape == Styles.RECT) {
            drawProgressForRect(canvas);
        } else if (shape == Styles.CIRCLE) {
            drawProgressForCircle(canvas);
        }
        canvas.restore();
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (shape == Styles.CIRCLE) {
            mPaintMaxWidth = Math.min(mUnReachHeight, mReachHeight);
            int expect = mRadius * 2 + mPaintMaxWidth + getPaddingLeft() + getPaddingRight();
            int widthVal = measureWidthForCircle(expect, widthMeasureSpec);
            int heigthVal = measureHeightForCircle(expect, heightMeasureSpec);
            int radiusWidth = Math.min(widthVal, heigthVal);
            mRadius = (radiusWidth - getPaddingRight() - getPaddingLeft() - mPaintMaxWidth) / 2;
            setMeasuredDimension(radiusWidth, radiusWidth);

        } else if (shape == Styles.RECT) {
            int widthVal = measureWidthForRect(widthMeasureSpec);
            int heightVal = measureHeightForRect(heightMeasureSpec);
            setMeasuredDimension(widthVal, heightVal);
            mRealWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        }
    }

    private int measureWidthForRect(int widthMeasureSPec) {
        int widthExpect = dpChanger(200);
        return resolveSize(widthExpect, widthMeasureSPec);
    }

    private int measureHeightForRect(int heightMeasureSpec) {
        int heightExpect = dpChanger(30);
        return resolveSize(heightExpect, heightMeasureSpec);
    }

    private int measureWidthForCircle(int widthMeasureSPec, int expect) {
        return resolveSize(expect, widthMeasureSPec);
    }

    private int measureHeightForCircle(int heightMeasureSpec, int expect) {
        return resolveSize(expect, heightMeasureSpec);
    }


    private void obtainCustomAttrs(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.ProcessBarWithH);

        mUnReachColor = ta.getColor(R.styleable.ProcessBarWithH_process_unreach_color, mUnReachColor);
        mUnReachHeight = (int) ta.getDimension(R.styleable.ProcessBarWithH_process_unreach_height, mUnReachHeight);
        mReachColor = ta.getColor(R.styleable.ProcessBarWithH_process_reach_color, mReachColor);
        mReachHeight = (int) ta.getDimension(R.styleable.ProcessBarWithH_process_reach_height, mReachHeight);
        mTextColor = ta.getColor(R.styleable.ProcessBarWithH_process_text_color, mTextColor);
        mTextSize = (int) ta.getDimension(R.styleable.ProcessBarWithH_process_text_size, mTextSize);
        mTextOffset = (int) ta.getDimension(R.styleable.ProcessBarWithH_process_text_offset, mTextOffset);
        int style = ta.getInt(R.styleable.ProcessBarWithH_progress_style, STYLE_RECT);
        switch (style) {
            case STYLE_RECT:
                shape = Styles.RECT;
                break;
            case STYLE_CIRCLE:
                shape = Styles.CIRCLE;
                break;
        }
        mRadius = ta.getInt(R.styleable.ProcessBarWithH_progress_radius, mRadius);
        ta.recycle();
    }

    private int dpChanger(int defValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                defValue,
                getResources().getDisplayMetrics());
    }

    private void drawProgressForRect(Canvas canvas) {
        canvas.translate(getPaddingLeft(), getHeight() / 2);
        boolean noNeedUnReach = false;
        float radio = getProgress() * 1.0f / getMax();
        float processX = radio * mRealWidth;
        float endX = processX - mTextOffset / 2;
        String text = getProgress() + "%";
        int textWidth = (int) mPaint.measureText(text);
        if (processX + textWidth >= mRealWidth) {
            processX = mRealWidth - textWidth;
            noNeedUnReach = true;
        }
        if (endX > 0) {
            mPaint.setColor(mReachColor);
            mPaint.setStrokeWidth(mReachHeight);
            canvas.drawLine(0, 0, endX, 0, mPaint);
        }

        mPaint.setColor(mTextColor);
        int y = (int) (-(mPaint.descent() + mPaint.ascent()) / 2);
        canvas.drawText(text, processX, y, mPaint);

        if (!noNeedUnReach) {
            float start = processX + textWidth + mTextOffset / 2;
            mPaint.setColor(mUnReachColor);
            mPaint.setStrokeWidth(mUnReachHeight);
            canvas.drawLine(start, 0, mRealWidth, 0, mPaint);
        }

    }

    private void drawProgressForCircle(Canvas canvas) {
        canvas.translate(getPaddingLeft()+mPaintMaxWidth/2,getPaddingTop()+mPaintMaxWidth/2);
        mReachHeight = (int) (mUnReachHeight * 2.5f);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        String text = getProgress() + "%";
        float textWidth = mPaint.measureText(text);
        float textHeight = mPaint.descent() + mPaint.ascent();

        mPaint.setColor(mUnReachColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mUnReachHeight);
        canvas.drawCircle(mRadius,mRadius,mRadius,mPaint);

        mPaint.setColor(mReachColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mReachHeight);
        float sweepAngle=getProgress()*1.0f/getMax()*360;
        canvas.drawArc(new RectF(0,0,mRadius*2,mRadius*2),0,sweepAngle,false,mPaint);

        mPaint.setColor(mTextColor);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawText(text,mRadius-textWidth/2,mRadius-textHeight/2,mPaint);

    }

    public enum Styles {
        RECT, CIRCLE
    }
}
