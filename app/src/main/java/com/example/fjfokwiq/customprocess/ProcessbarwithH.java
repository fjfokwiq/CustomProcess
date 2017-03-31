package com.example.fjfokwiq.customprocess;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

/**
 * Created by fjfokwiq on 2017/3/26.
 */

public class ProcessbarwithH extends ProgressBar {
    private static final int DEFAULT_UNREACH_COLOR = 0xFFFC00D1;
    private static final int DEFAULT_UNREACH_HEIGHT = 2;
    private static final int DEFAULT_REACH_COLOR = 0xFFD3D6DA;
    private static final int DEFAULT_REACH_HEIGHT = 2;
    private static final int DEFAULT_TEXT_SIZE = 10;
    private static final int DEFAULT_TEXT_COLOR = DEFAULT_REACH_COLOR;
    private static final int DEFAULT_TEXT_OFFSET = 10;


    private int mUnReachColor = DEFAULT_UNREACH_COLOR;
    private int mUnReachHeight = dpChanger(DEFAULT_UNREACH_HEIGHT);
    private int mReachColor = DEFAULT_REACH_COLOR;
    private int mReachHeight = dpChanger(DEFAULT_REACH_HEIGHT);
    private int mTextSize = dpChanger(DEFAULT_TEXT_SIZE);
    private int mTextColor = DEFAULT_TEXT_COLOR;
    private int mTextOffset = dpChanger(DEFAULT_TEXT_OFFSET);

    private Paint mPaint = new Paint();
    
    private  int mRealWidth;

    public ProcessbarwithH(Context context) {
        this(context, null);
    }

    public ProcessbarwithH(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProcessbarwithH(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        obtainCustomAttrs(attrs);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(getPaddingLeft(),getHeight()/2);
        boolean noNeedUnReach=false;
        float radio=getProgress()*1.0f/getMax();
        float processX=radio*mRealWidth;
        float endX=processX-mTextOffset/2;
        String text=getProgress()+"%";
        int textWidth = (int) mPaint.measureText(text);
        if (processX + textWidth >= mRealWidth) {
            processX=mRealWidth-textWidth;
            noNeedUnReach=true;
        }
        if (endX > 0) {
            mPaint.setColor(mReachColor);
            mPaint.setStrokeWidth(mReachHeight);
            canvas.drawLine(0,0,endX,0,mPaint);
        }

        mPaint.setColor(mTextColor);
       int y= (int) (-(mPaint.descent()+mPaint.ascent())/2);
        canvas.drawText(text,processX,y,mPaint);

        if (!noNeedUnReach) {
            float start=processX+textWidth+mTextOffset/2;
            mPaint.setColor(mUnReachColor);
            mPaint.setStrokeWidth(mUnReachHeight);
            canvas.drawLine(start,0,mRealWidth,0,mPaint);
        }
        canvas.restore();
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthVal = MeasureSpec.getSize(widthMeasureSpec);
        int heightVal=measureHeight(heightMeasureSpec);
        setMeasuredDimension(widthVal,heightVal);
        mRealWidth=getMeasuredWidth()-getPaddingLeft()-getPaddingRight();

    }


    private int measureHeight(int heightMeasureSpec) {
           int result=0;
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (heightMeasureSpec == MeasureSpec.EXACTLY) {
            result=height;
        }else {
            int textHeight= (int) (mPaint.descent()-mPaint.ascent());
            result = getTop() + getBottom() + Math.max(Math.max(mReachHeight, mUnReachHeight), Math.abs(textHeight));
        }
        if (heightMode == MeasureSpec.AT_MOST) {
            result = Math.min(result, height);
        }
        return result;
    }

    private void obtainCustomAttrs(AttributeSet attrs) {
        TypedArray ta=getContext().obtainStyledAttributes(attrs, R.styleable.ProcessbarwithH);
          mUnReachColor = ta.getColor(R.styleable.ProcessbarwithH_process_unreach_color, mUnReachColor);
          mUnReachHeight= (int) ta.getDimension(R.styleable.ProcessbarwithH_process_unreach_height,mUnReachHeight);
          mReachColor = ta.getColor(R.styleable.ProcessbarwithH_process_reach_color, mReachColor);
          mReachHeight = (int) ta.getDimension(R.styleable.ProcessbarwithH_process_reach_height, mReachHeight);
          mTextColor = ta.getColor(R.styleable.ProcessbarwithH_process_text_color, mTextColor);
          mTextSize= (int) ta.getDimension(R.styleable.ProcessbarwithH_process_text_size,mTextSize);
          mTextOffset= (int) ta.getDimension(R.styleable.ProcessbarwithH_process_text_offset,mTextOffset);
        ta.recycle();
    }

    private int dpChanger(int defValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                defValue,
                getResources().getDisplayMetrics());
    }
}
