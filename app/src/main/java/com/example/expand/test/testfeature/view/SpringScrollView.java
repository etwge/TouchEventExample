package com.example.expand.test.testfeature.view;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;

/**
 * Author: lhy
 * Date: 2016/9/1
 * Swipe the ScrollView to the Top or Bottom, can continue scroll up or scroll down a little distance,
 * and up finger auto return to original position
 */
public class SpringScrollView extends ViewGroup{

    public static final String TAG = SpringScrollView.class.getSimpleName();
    private static final float RATE = .5f;

    private View mTarget;
    private float mInitialDownY = -1;
    private int mActivePointerId;
    private boolean mIsBeingDragged;
    private int mTouchSlop;
    private float mInitialMotionY;
    private int mMediumAnimationDuration;
    private int mFrom;
    private DecelerateInterpolator decelerateInterpolator;
    private boolean mReturningToStart;

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
        mMediumAnimationDuration = getContext().getResources().getInteger(android.R.integer.config_mediumAnimTime);
        decelerateInterpolator = new DecelerateInterpolator(2f);
        Log.i(TAG, "mTouchSlop:" + mTouchSlop);
    }

    private Animation animToStartPosition = new Animation() {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            int targetTop = 0;
            if (mFrom != 0) {
                targetTop = (mFrom + (int)((0 - mFrom) * interpolatedTime));
            }
            int offset = targetTop - 0;
//            final int currentTop = mTarget.getTop();
//            if (offset + currentTop < 0) {
//                offset = 0 - currentTop;
//            }
            Log.i(TAG, "interpolatedTime:" + interpolatedTime);
            Log.i(TAG, "targetTop:" + offset);
            setTargetOffsetTopAndBottom(offset);
        }
    };

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

        if(!isEnabled() || mReturningToStart /*|| canScrollUp() || canScrollDown()*/){
            return false;
        }

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
                if(mActivePointerId == -1){
                    Log.e(TAG, "invalid pointer id");
                    return false;
                }
                final float y = getMotionY(ev, mActivePointerId);
                if(y == -1){
                    return false;
                }
                float yDiff = y - mInitialDownY;
                if(yDiff > mTouchSlop && !mIsBeingDragged && !canScrollUp()){
                    mIsBeingDragged = true;
                    mInitialMotionY = mInitialDownY + mTouchSlop;
                }else if (-yDiff > mTouchSlop && !mIsBeingDragged && !canScrollDown()){
                    mIsBeingDragged = true;
                    mInitialMotionY = mInitialDownY - mTouchSlop;
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mIsBeingDragged = false;
                mInitialDownY = -1;
                mActivePointerId = -1;
                break;

        }
        return mIsBeingDragged;
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean b) {
        // Nope.
    }

    private boolean canScrollUp() {
        return ViewCompat.canScrollVertically(mTarget, -1);
    }

    private boolean canScrollDown() {
        return ViewCompat.canScrollVertically(mTarget, 1);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
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
                if(mActivePointerId == -1){
                    Log.e(TAG, "invalid pointer id");
                    return false;
                }
                final float y = getMotionY(ev, mActivePointerId);
                if(y == -1){
                    return false;
                }
                float yDiff = y - mInitialDownY;
                boolean isScrollUp = false;
                if(yDiff > mTouchSlop && !mIsBeingDragged){
                    mIsBeingDragged = true;
                    mInitialMotionY = mInitialDownY + mTouchSlop;
                }else if (-yDiff > mTouchSlop && !mIsBeingDragged){
                    mIsBeingDragged = true;
                    mInitialMotionY = mInitialDownY - mTouchSlop;
                    isScrollUp = true;
                }
                Log.i(TAG, "mInitialMotionY" + mInitialMotionY);
                if(mIsBeingDragged){
                    float targetY = (y - mInitialMotionY) * RATE;
//                    if(targetY > )
                    if(isScrollUp){
                        targetY = (mInitialMotionY - y) * RATE;
                    }
                    setTargetOffsetTopAndBottom(targetY);
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                onSecondaryPointerDown(ev);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                animOffsetToStartPosition(mTarget.getTop(), animToStartPositionLister);
                mIsBeingDragged = false;
                mInitialDownY = -1;
                mActivePointerId = -1;
                break;
        }

        return true;
    }

    private void animOffsetToStartPosition(int from, Animation.AnimationListener listener) {
        mReturningToStart = true;
        mFrom = from;
        animToStartPosition.reset();
        animToStartPosition.setDuration(mMediumAnimationDuration);
        animToStartPosition.setAnimationListener(listener);
        animToStartPosition.setInterpolator(decelerateInterpolator);
        this.startAnimation(animToStartPosition);
    }

    Animation.AnimationListener animToStartPositionLister = new BaseAnimationListener(){
        @Override
        public void onAnimationEnd(Animation animation) {
            super.onAnimationEnd(animation);
            mReturningToStart = false;
        }
    };

    private void onSecondaryPointerDown(MotionEvent ev) {
        mActivePointerId = ev.getPointerId(ev.getActionIndex());
    }

    private void setTargetOffsetTopAndBottom(float yDiff) {
        Log.i(TAG, "yDiff:" + yDiff);
//        if(yDiff < 0){
//            yDiff = 0;
//        }

        mTarget.offsetTopAndBottom((int) yDiff - mTarget.getTop());
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        int pointerIndex = MotionEventCompat.getPointerId(ev, MotionEventCompat.getActionIndex(ev));
        int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
        if(pointerId == mActivePointerId){
            mActivePointerId = pointerId == 0 ? 0 : 1;
        }
    }

    private float getMotionY(MotionEvent ev, int mActivePointerId) {
        final int index = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
        if(index == -1){
            return -1;
        }
        return MotionEventCompat.getY(ev, index);
    }

    /**
     * Simple AnimationListener to avoid having to implement unneeded methods in
     * AnimationListeners.
     */
    private class BaseAnimationListener implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }
    }
}
