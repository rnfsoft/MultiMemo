package com.rnfsoft.multimemo.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

import com.rnfsoft.multimemo.R;

/**
 * Created by taehee on 7/31/15.
 */
public class TitleBitmapButton extends Button {

    Context context;
    Paint paint;

    private int backgroundBitmapNormal = R.drawable.title_button;
    private int defaultColor = 0xffffffff;
    private float defaultScaleX = 1.0F;
    private float defaultSize = 18F;
    private Typeface defaultTypeface = Typeface.DEFAULT_BOLD;
    private boolean selected = false;
    private int iconStatus = 0;
    private boolean paintChanged = false;
    private int backgroundBitmapClicked = R.drawable.title_button_clicked;
    private Bitmap iconNormalBitmap;
    private Bitmap iconClickedBitmap;
    private int bitmapAlign = BITMAP_ALIGN_CENTER;
    public static final int BITMAP_ALIGN_CENTER = 0;
    public static final int BITMAP_ALIGN_LEFT = 1;
    public static final int BITMAP_ALIGN_RIGHT = 2;
    private int bitmapPadding = 10;
    private String titleText = "";


    public TitleBitmapButton(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public TitleBitmapButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        setBackgroundResource(backgroundBitmapNormal);
        paint = new Paint();
        paint.setColor(defaultColor);
        paint.setAntiAlias(true);
        paint.setTextScaleX(defaultScaleX);
        paint.setTextSize(defaultSize);
        paint.setTypeface(defaultTypeface);

        selected = false;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        int action = event.getAction();

        switch (action){
            case MotionEvent.ACTION_UP:

                if(selected){

                } else {
                    setBackgroundResource(backgroundBitmapNormal);
                    iconStatus = 0;
                    paintChanged = true;
                    defaultColor = Color.WHITE;

                }

                break;
            case MotionEvent.ACTION_DOWN:
                if(selected){

                } else {
                    setBackgroundResource(backgroundBitmapClicked);
                    iconStatus = 1;
                    paintChanged = true;
                    defaultColor = Color.BLACK;
                }

                break;
        }

        invalidate();;

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

            paintChanged = false;
        }

        Bitmap iconBitMap = iconNormalBitmap;
        if(iconStatus == 1){
            iconBitMap = iconClickedBitmap;
        }

        if(iconBitMap != null){
            int iconWidth = iconBitMap.getWidth();
            int iconHeight = iconBitMap.getHeight();
            int bitmapX = 0;
            if(bitmapAlign == BITMAP_ALIGN_CENTER){
                bitmapX = (curWidth - iconWidth)/2;
            } else if(bitmapAlign == BITMAP_ALIGN_LEFT){
                bitmapX = bitmapPadding;
            } else if(bitmapAlign == BITMAP_ALIGN_RIGHT){
                bitmapX = curWidth-bitmapPadding;
            }
            canvas.drawBitmap(iconBitMap,bitmapX,(curHeight-iconHeight)/2,paint);
        }

        Rect bounds = new Rect();
        paint.getTextBounds(titleText, 0, titleText.length(), bounds );
        float textWidth = ((float)curWidth - bounds.width())/2.0F;
        float textHeight = ((float)curHeight + bounds.height())/2.0F + 4.0F;

        canvas.drawText(titleText, textWidth, textHeight, paint);


    }




}
