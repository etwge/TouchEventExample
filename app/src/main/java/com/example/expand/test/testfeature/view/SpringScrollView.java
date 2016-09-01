package com.example.expand.test.testfeature.view;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

/**
 * Author: lhy
 * Date: 2016/9/1
 */
public class SpringScrollView extends ViewGroup{

    public static final String TAG = SpringScrollView.class.getSimpleName();

    private View mTarget;
    private float mInitialDownY = -1;
    private int mActivePointerId;
    private boolean mIsBeingDragged;
    private int mTouchSlop;
    private float mInitialMotionY;

    /**
     * Simple constructor to use when creating instance from code
     * @param context
     */
    public SpringScrollView(Context context) {
        this(context, null);
    }

    /**
     * Constructor that called when inflating from XML
     * @param context
     * @param attrs
     */
    public SpringScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        Log.i(TAG, "mTouchSlop:" + mTouchSlop);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ensureTarget();
        if (mTarget == null) {
            return;
        }
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        mTarget.measure(
                MeasureSpec.makeMeasureSpec(width - getPaddingLeft() - getPaddingRight(),
                        MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(height - getPaddingTop() - getPaddingBottom(),
                        MeasureSpec.EXACTLY));
    }

    private void ensureTarget() {
        if(getChildCount() == 0){
            Log.e("error", "no found target view");
            return;
        }
        if(mTarget == null){
            mTarget = getChildAt(0);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if(mTarget == null){
            return;
        }
        int left = getPaddingLeft();
        int width = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        int top = getPaddingTop();
        int height = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        mTarget.layout(left, top, width, height);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = MotionEventCompat.getActionMasked(ev);
        switch (action){
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                mIsBeingDragged = false;
                final float initialDownY = getMotionY(ev, mActivePointerId);
                if(initialDownY == -1){
                    return false;
                }
                mInitialDownY = initialDownY;
                break;
            case MotionEvent.ACTION_MOVE:
                final float y = getMotionY(ev, mActivePointerId);
                if(y == -1){
                    return false;
                }
                float yDiff = y - mInitialDownY;
                if(yDiff > mTouchSlop && !mIsBeingDragged){
                    mIsBeingDragged = true;
                    mInitialMotionY = mInitialDownY + mTouchSlop;
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mIsBeingDragged = false;
                mInitialDownY = -1;
                mActivePointerId = -1;
                break;

        }


        return mIsBeingDragged;
    }

    private float getMotionY(MotionEvent ev, int mActivePointerId) {
        final int index = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
        if(index == -1){
            return -1;
        }
        return MotionEventCompat.getY(ev, index);
    }
}
