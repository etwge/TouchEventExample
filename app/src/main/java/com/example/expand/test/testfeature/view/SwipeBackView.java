//package com.example.expand.test.testfeature.view;
//
//import android.animation.AnimatorListenerAdapter;
//import android.content.Context;
//import android.support.v4.view.MotionEventCompat;
//import android.support.v4.view.ViewCompat;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewConfiguration;
//import android.view.ViewGroup;
//import android.view.animation.Animation;
//import android.view.animation.Transformation;
//import android.widget.FrameLayout;
//import android.widget.ScrollView;
//
//import com.example.expand.test.testfeature.R;
//
//
///**
// * Author: lhy
// * Date: 2016/8/26
// */
//public class SwipeBackView extends FrameLayout {
//    private final int mTouchSlop;
//    private ScrollView mScrollView;
//    private int mLastY;
//    private boolean mIsScrolling;
//    private int topDistance;
//
//    public SwipeBackView(Context context) {
//        this(context, null);
//    }
//
//    public SwipeBackView(Context context, AttributeSet attrs) {
//        this(context, attrs, 0);
//    }
//
//    public SwipeBackView(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        ViewConfiguration vc = ViewConfiguration.get(getContext());
//        mTouchSlop = vc.getScaledTouchSlop();
//    }
//
//    @Override
//    protected void onFinishInflate() {
//        super.onFinishInflate();
//        getScrollView();
//    }
//
//    private void init() {
//        getScrollView();
//    }
//
//    private void getScrollView() {
//        mScrollView = (ScrollView) findViewById(R.id.scroll_view);
//    }
//
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        ensureTarget();
//
//        final int action = MotionEventCompat.getActionMasked(ev);
//
//        if(mReturningToStart && action == MotionEvent.ACTION_DOWN) {
//            mReturningToStart = false;
//        }
//
//        if(!isEnabled() || mReturningToStart || canChildScrollUp() || mRefreshing){
//            return false;
//        }
//        switch (action){
//            case MotionEvent.ACTION_DOWN:
//                setTargetOffsetTopAndBottom();
//                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
//        }
//    }
//
//    private boolean canScrollUp() {
//       return ViewCompat.canScrollVertically(mScrollView, -1);
////        return mScrollView.getScrollY() != 0;
//    }
//
////    @Override
////    public boolean onTouchEvent(MotionEvent event) {
////        switch (event.getAction()){
////            case MotionEvent.ACTION_DOWN:
////                mLastY = (int) event.getY();
////                return true;
////            case MotionEvent.ACTION_MOVE:
////                final int yDiff = calculateDistanceY(event);
////
////                // Touch slop should be calculated using ViewConfiguration
////                // constants.
////                if (yDiff > mTouchSlop) {
////                    // Start scrolling!
////                    mIsScrolling = true;
////                    Log.i("xxx", "yDiff:" + yDiff);
////                    int move = yDiff - topDistance;
////                    topDistance += move;
////                    mScrollView.offsetTopAndBottom(move);
////                    return true;
////                }
////                break;
////            case MotionEvent.ACTION_CANCEL:
////            case MotionEvent.ACTION_UP:
////                mIsScrolling = false;
////                mScrollView.offsetTopAndBottom(-topDistance);
////                topDistance = 0;
////                return true;
////        }
////        return super.onTouchEvent(event);
////    }
////
////    private int calculateDistanceY(MotionEvent event) {
////        return (int) (event.getY() - mLastY);
////    }
//
//    View mTarget;
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        if(mTarget == null){
//            ensureTarget();
//        }
//        if(mTarget == null){
//            return;
//        }
//        mTarget.getTop()
//        mTarget.measure(MeasureSpec.makeMeasureSpec(
//                getMeasuredWidth() - getPaddingLeft() - getPaddingRight(),
//                MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(
//                getMeasuredWidth() - getPaddingTop() - getPaddingBottom(), MeasureSpec.EXACTLY));
//    }
//    private final Animation mAnimateToStartPosition = new Animation() {
//        @Override
//        public void applyTransformation(float interpolatedTime, Transformation t) {
//            int targetTop = 0;
//            if (mFrom != mOriginalOffsetTop) {
//                targetTop = (mFrom + (int)((mOriginalOffsetTop - mFrom) * interpolatedTime));
//            }
//            int offset = targetTop - mTarget.getTop();
//            final int currentTop = mTarget.getTop();
//            if (offset + currentTop < 0) {
//                offset = 0 - currentTop;
//            }
//            setTargetOffsetTopAndBottom(offset);
//        }
//    };
//    @Override
//    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        final int width = getMeasuredWidth();
//        final int height = getMeasuredHeight();
//        if(getChildCount() == 0){
//            return;
//        }
//        if(mTarget == null){
//            return;
//        }
//        final View child = mTarget;
//        final int childLeft = getPaddingLeft();
//        final int childTop = getPaddingTop();
//        final int childWidth = width - getPaddingLeft() - getPaddingRight();
//        final int childHeight = height - getPaddingTop() - getPaddingBottom();
//        child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
//    }
//}
//
