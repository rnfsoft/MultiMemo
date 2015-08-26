package com.rnfsoft.multimemo.common;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.Toast;

import com.rnfsoft.multimemo.R;

import java.util.Locale;

/**
 * Created by taehee on 7/31/15.
 */
public class TitleBackgroundButton extends Button {

    Context context;
    Paint paint;
    private int defaultColor = 0xff333333;
    private float defaultScaleX = 1.0F;
    private float defaultSize = 22F;
    private Typeface defaultTypeface = Typeface.DEFAULT_BOLD;
    private String titleText = "";
    private boolean paintChanged = false;


    public TitleBackgroundButton(Context context) {
        super(context);
        this.context = context;
        init();

    }



    public TitleBackgroundButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }


    private void init() {
        setBackgroundResource(R.drawable.title_background);
        paint = new Paint();
        paint.setColor(defaultColor);
        paint.setAntiAlias(true);
        paint.setTextScaleX(defaultScaleX);
        paint.setTextSize(defaultSize);
        paint.setTypeface(defaultTypeface);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        int action = event.getAction();

        switch (action){
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_DOWN:
                Toast.makeText(context, titleText, Toast.LENGTH_SHORT).show();
        }

        invalidate();


        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int curWidth = getWidth();
        int curHeight = getHeight();

        if(paintChanged){
            paint.setColor(defaultColor);
            paint.setTextScaleX(defaultScaleX);
            paint.setTextSize(defaultSize);
            paint.setTypeface(defaultTypeface);
        }

        Rect bounds = new Rect();
        paint.getTextBounds(titleText, 0, titleText.length(), bounds);
        float textWidth = ((float)curWidth - bounds.width())/2.0F;
        float textHeight = ((float)curHeight - bounds.height())/2.0F + bounds.height();

        canvas.drawText(titleText, textWidth, textHeight, paint);

    }

    public String getTitleText() {
        return titleText;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    public int getDefaultColor() {
        return defaultColor;
    }

    public void setDefaultColor(int defaultColor) {
        this.defaultColor = defaultColor;
        paintChanged = true;
    }

    public float getDefaultSize() {
        return defaultSize;
    }

    public void setDefaultSize(float defaultSize) {
        this.defaultSize = defaultSize;
        paintChanged = true;
    }

    public float getDefaultScaleX() {
        return defaultScaleX;
    }

    public void setDefaultScaleX(float defaultScaleX) {
        this.defaultScaleX = defaultScaleX;
        paintChanged = true;
    }

    public Typeface getDefaultTypeface() {
        return defaultTypeface;
    }

    public void setDefaultTypeface(Typeface defaultTypeface) {
        this.defaultTypeface = defaultTypeface;
        paintChanged = true;
    }



}
