package com.zhongtianli.nebula.view.draglayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;


public class MainLayout extends RelativeLayout {
    private DragLayout mDragLayout;

    public MainLayout(Context context) {
        super(context);
    }

    public MainLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MainLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setDragLayout(DragLayout dl) {
        this.mDragLayout = dl;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (mDragLayout.getmStatus() != DragLayout.Status.Close) {
            return true;
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mDragLayout.getmStatus() != DragLayout.Status.Close) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                mDragLayout.close();
            }
            return true;
        }
        return super.onTouchEvent(event);
    }

}
