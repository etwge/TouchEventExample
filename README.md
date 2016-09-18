对于ViewGroup，处理触摸事件有两个很重要的方法onIntercepteTouchEvent()和onTouchEvent()
1. onInterceptTouchEvent()，MotionEvent事件会最先传递到这里，然后根据返回值，是否需要拦截改事件
2. onTouchEvent()，根据需要是否要处理MotionEvent事件


###onIntercepteTouchEvent()
点开源码，看下注释。
```
     /**
     * Implement this method to intercept all touch screen motion events.  This
     * allows you to watch events as they are dispatched to your children, and
     * take ownership of the current gesture at any point.
     *
     * <p>Using this function takes some care, as it has a fairly complicated
     * interaction with {@link View#onTouchEvent(MotionEvent)
     * View.onTouchEvent(MotionEvent)}, and using it requires implementing
     * that method as well as this one in the correct way.  Events will be
     * received in the following order:
     *
     * <ol>
     * <li> You will receive the down event here.
     * <li> The down event will be handled either by a child of this view
     * group, or given to your own onTouchEvent() method to handle; this means
     * you should implement onTouchEvent() to return true, so you will
     * continue to see the rest of the gesture (instead of looking for
     * a parent view to handle it).  Also, by returning true from
     * onTouchEvent(), you will not receive any following
     * events in onInterceptTouchEvent() and all touch processing must
     * happen in onTouchEvent() like normal.
     * <li> For as long as you return false from this function, each following
     * event (up to and including the final up) will be delivered first here
     * and then to the target's onTouchEvent().
     * <li> If you return true from here, you will not receive any
     * following events: the target view will receive the same event but
     * with the action {@link MotionEvent#ACTION_CANCEL}, and all further
     * events will be delivered to your onTouchEvent() method and no longer
     * appear here.
     * </ol>
     *
     * @param ev The motion event being dispatched down the hierarchy.
     * @return Return true to steal motion events from the children and have
     * them dispatched to this ViewGroup through onTouchEvent().
     * The current target will receive an ACTION_CANCEL event, and no further
     * messages will be delivered here.
     */
```
抠脚大汉的英语水平简单翻译一下：


实现这个方法是用来拦截所有触摸屏幕事件，这个方法允许你监控这些事件当它们被分派到子View
并且拥有当前全部点的动作的所有权。

使用这个函数需要特别小心，因为有非常复杂的联系跟onTouchEvent()，使用这个方法一样要采用用正确的方式，事件接收根据以下规则：

你将先收到down event在这里
这个down事件可以被一个子View处理，或者被自身的onTouchEvent()事件处理；意味你可以在实现 onTouchEvent()的方法中返回true, 你将会
继续收到该手势的后续的事件（而不是继续寻找父View去处理它），同样，在onTouchEvent()事件返回true ，你将不会接收到任何的后续事件
在onInterceptEvent()方法中，像正常那样，所有的事件处理必须写在onTouchEvent()中
如果在这个方法中你返回了false，每一个的后续事件（直到最后一次的up 事件）先传到这里，然后在传递到目标的onTouchEvent()
如果在这个方法中你返回了true，除了MotionEvent#ACTION_CANCEL这个，你将不会收到任何的后续事件。所有的将来时间将直接传递到
onTouchEvent()中，不会出现在这个方法里了

@param:分派过来的触摸事件
@return 返回true偷取这个事件从子View中，并且派遣到自身的onTouchEvent()
当前目标将受到ACTION_CANCEL 的时间，将来的后续事件不会再传到这里。

来总结下：
1. 如果在{@link MotionEvent#ACTION_DOWN}这里返回了true,后续的其他事件如{@link MotionEvent#ACTION_MOVE
   MotionEvent#ACTION_UP}便不会往下传，也不会返回到这方法，
   直接传到自身的onTouchEvent();
2. 如果返回false，后续事件会优先传到这个方法中，然后再传给子View

*
注：如果没有任何子view处理这个事件，最终也会传到自身的onTouchEvent()方法中;如果该事件没有被处理，后续事件也不会传到此方法中;
还有个需要注意的是子View可以调用父类的requestDisallowInterceptTouchEvent()方法，传true指的是要求父类不要拦截事件，listView，scrollView中就调了这个方法，在下拉刷新这种情况时，父类可以屏蔽这个方法，就是继承这个方法，啥也不做，空实现，v4中的SwipeRefreshLayout
就是这样做的。
*


###onTouchEvent()

```
     /**
     * Implement this method to handle touch screen motion events.
     * <p>
     * If this method is used to detect click actions, it is recommended that
     * the actions be performed by implementing and calling
     * {@link #performClick()}. This will ensure consistent system behavior,
     * including:
     * <ul>
     * <li>obeying click sound preferences
     * <li>dispatching OnClickListener calls
     * <li>handling {@link AccessibilityNodeInfo#ACTION_CLICK ACTION_CLICK} when
     * accessibility features are enabled
     * </ul>
     *
     * @param event The motion event.
     * @return True if the event was handled, false otherwise.
     */
```

实现这个方法来处理触摸屏幕的事件

如果这个方法之前被用来检测点击动作，建议改为实现和调用performClick(), 这样确保包含系统的行为，包含
点击声音
分派点击回调事件
处理@link AccessibilityNodeInfo#ACTION_CLICK ACTION_CLICK}当一些可访问的功能可用时

这个文档仅仅说了下这个方法是干啥的，是用来处理触摸事件的。

###后记



1. 一般认为ACTION_DOWN是一个触摸事件集合的开始，然后到ACTION_UP 或者ACTION_CANCEL代表事件结束。移动时会产生有ACTION_MOVE事件，多点时另一个点按下时会产生ACTION_POINTER_DOWN，其中一个点离开时产生ACTION_POINTER_UP；

2. 如果在ACTION_DOWN事件时，在onTouchEvent()中返回true表示处理了该事件，其他的后续事件就会传递到这里；

3. 在ACTION_DOWN事件中，如果在目标View的onTouchEvent()方法中返回true，表示处理了这个事件，将不会往下传递，后续的其他事件都会传递到这里



**多点时对于ACTION_MOVE始终的event，getActionIndex() 总是0的疑惑？**
- ACTION_MOVE属于单点的event的action，头两位不会带索引信息的，所以始终是返回0,要想获取其他的点，需要每个点遍历，根据pointIndex;
从另外一个角度看，多个点共有一个ACTION_MOVE,可以减少事件的分发的频率，减少内存的开销，因为有可能很多个点同时移动
- 除了ACTION_MOVE，以外的ACTION_POINTER_DOWN，ACTION_POINTER_UP，这两个多点时才有的事件，会带有索引信息,ACTION_UP也会有索引信息，调用getActionIndex()则可以直接获取到对应的pointerIndex


最好自己写个demo来试下，就很容易理解了，不要脸的附上自己写的( ▼-▼ )
https://github.com/etwge/TouchEventExample



### 参考
建议看下SwipeRefreshView和ScrollView的中的onTouchEvent和onInterceptEvent代码
https://developer.android.com/training/gestures/viewgroup.html
http://blog.csdn.net/xyz_lmn/article/details/12517911


