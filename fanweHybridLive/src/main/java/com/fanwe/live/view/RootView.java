package com.fanwe.live.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class RootView extends RelativeLayout {
    public RootView(Context context) {
        super(context);
    }

    public RootView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RootView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    int x=0;
    int y=0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                x= (int) event.getRawX();
                y = (int) event.getRawY();

                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) event.getRawX();
                int moveY = (int) event.getRawY();
                int deffX=moveX-x;
                int deffY=moveY-y;

                int top = getTop()+deffY;
                int left = getLeft()+deffX;
                int bottom=getBottom()+deffY;
                int right = getRight()+deffX;

                layout(left,top,right,bottom);

                x= (int) event.getRawX();
                y= (int) event.getRawY();

                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(event);
    }
}
