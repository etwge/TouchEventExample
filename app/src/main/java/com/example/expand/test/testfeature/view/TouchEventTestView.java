package com.example.expand.test.testfeature.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

/**
 * Author: lhy
 * Date: 2016/8/30
 */
public class TouchEventTestView extends ViewGroup {


    private View mChildView;

    /**
     * Simple constructor to use when creating TouchEventTestView's instance from code
     * @param context
     */
    public TouchEventTestView(Context context) {
        this(context, null);
    }

    /**
     * Constructor that is called when inflating from XML
     * @param context
     * @param attrs
     */
    public TouchEventTestView(Context context, AttributeSet attrs) {
        super(context, attrs);
//        addChildView();
    }

    private void addChildView() {
        ScrollView scrollView = new ScrollView(getContext());
        scrollView.setBackgroundColor(Color.YELLOW);
        mChildView = scrollView;
        addView(scrollView);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ensureChildView();
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        if(mChildView == null){
            return;
        }
        mChildView.measure(
                MeasureSpec.makeMeasureSpec(
                        width - getPaddingLeft() - getPaddingRight(),MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(
                        height - getPaddingTop() - getPaddingBottom(), MeasureSpec.EXACTLY));
    }

    private void ensureChildView() {
        if (getChildCount() > 0){
            mChildView = getChildAt(0);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        if(mChildView == null){
            return;
        }
        int left = getPaddingLeft();
        int top = getPaddingTop();
        int childWidth = width - getPaddingLeft() - getPaddingRight();
        int childHeight = height - getPaddingTop() - getPaddingBottom();
        mChildView.layout(left, top, childWidth, childHeight);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    /**
     * note:
     * 1.如果在{@link MotionEvent#ACTION_DOWN}
     * 这里返回了true.
     * 后续的其他事件如{@link MotionEvent#ACTION_MOVE,
     * MotionEvent#ACTION_UP}便不会往下传，也不会返回到这方法，
     * 直接传到自身的onTouchEvent();
     * 如果返回false，后续事件会优先传到这个方法中，然后再传给子View
     *
     *
     * 注：如果没有任何子view处理这个事件，最终也会传到自身的onTouchEvent()方法中
     *    如果该事件没有被处理，后续事件也不会传到此方法中。
     *
     *
     *  建议直接看文档，更好理解，解释的最清晰
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        int action = ev.getAction();
        printActionInfo(action, "onInterceptEvent1", ev);

        int action2 = ev.getActionMasked();
        printActionInfo(action2, "onInterceptEvent2", ev);

        return false;
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

    private void printX(MotionEvent ev, String eventFromWhere) {
        for (int i = 0; i < ev.getPointerCount(); i++) {
            int pointIndex = i;
            int pointerId = ev.getPointerId(i);
            Log.i("xxx", eventFromWhere + "|" + pointIndex + "|" + pointerId + "|" + ev.getX(i));
        }
    }

    /**
     *
     *  note
     *  1.一般认为ACTION_DOWN是一个触摸事件集合的开始，然后到ACTION_UP 或者ACTION_CANCEL代表事件结束。移动时会产生有ACTION_MOVE事件，
     *  多点时另一个点按下时会产生ACTION_POINTER_DOWN，其中一个点离开时产生ACTION_POINTER_UP
     *
     *  2.在ACTION_DOWN事件中，如果在目标View的onTouchEvent()方法中返回true，表示处理了这个事件，将不会往下传递，后续的其他事件都会传递到这里
     *
     *  3.多点时对于ACTION_MOVE始终的event，getActionIndex() 总是0的疑惑？
     *  1) ACTION_MOVE属于单点的event的action，头两位不会带索引信息的，所以始终是返回0,
     *  要想获取其他的点，需要每个点遍历，根据pointIndex，
     *  获取相应的pointId来获取对应的信息，
     *  从另外一个角度看，多个点共有一个ACTION_MOVE,可以减少事件的分发的频率，减少内存的开销，因为有可能很多个点同时移动
     *  2) 除了ACTION_MOVE，以外的ACTION_POINTER_DOWN，ACTION_POINTER_UP，这两个多点时才有的事件，会带有索引信息,
     *  注：ACTION_UP也会有索引信息
     *  调用getActionIndex()则可以直接获取到对应的pointerIndex,
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        printActionInfo(event.getAction(), "onTouchEvent1", event);
        printActionInfo(event.getActionMasked(), "onTouchEvent2", event);
        return true;
    }
}
