package com.example.expand.test.testfeature.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Author: lhy
 * Date: 2016/9/1
 */
public class MyView extends View {
    public MyView(Context context) {
        this(context, null);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        printActionInfo(event.getActionMasked(), "MyView", event);
        return false;
    }

    private void printX(MotionEvent ev, String eventFromWhere) {
        for (int i = 0; i < ev.getPointerCount(); i++) {
            int pointIndex = i;
            int pointerId = ev.getPointerId(i);
            Log.i("xxx", eventFromWhere + "|" + pointIndex + "|" + pointerId + "|" + ev.getX(i));
        }
    }

    private void printActionInfo(int action, String eventFromWhere, MotionEvent ev) {
        switch (action){
            case MotionEvent.ACTION_DOWN:
                printX(ev, eventFromWhere);
                Log.i("xxx", eventFromWhere + ": down event" + "|" + ev.getActionIndex() + "|" + ev.getPointerId(ev.getActionIndex()));
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i("xxx", eventFromWhere + ": move event" + "|" + ev.getActionIndex() + "|" + ev.getPointerId(ev.getActionIndex()));
                break;
            case MotionEvent.ACTION_CANCEL:
                printX(ev, eventFromWhere);
                Log.i("xxx", eventFromWhere + ": cancel event"+ "|" + ev.getActionIndex() + "|" + ev.getPointerId(ev.getActionIndex()));
                break;
            case MotionEvent.ACTION_UP:
                printX(ev, eventFromWhere);
                Log.i("xxx", eventFromWhere + ": up event"+ "|" + ev.getActionIndex()+ "|" + ev.getPointerId(ev.getActionIndex()));
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                printX(ev, eventFromWhere);
                Log.i("xxx", eventFromWhere + ": pointer down event"+ "|" + ev.getActionIndex()+ "|" + ev.getPointerId(ev.getActionIndex()));
                break;
            case MotionEvent.ACTION_POINTER_UP:
                printX(ev, eventFromWhere);
                Log.i("xxx", eventFromWhere + ": pointer up event"+ "|" + ev.getActionIndex()+ "|" + ev.getPointerId(ev.getActionIndex()));
                break;

        }
    }

}
