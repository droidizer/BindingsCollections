package com.blackbelt.bindings.recyclerviewbindings


import android.graphics.PointF
import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import java.lang.ref.WeakReference

open class RecyclerViewGestureListener(bindableRecyclerView: AndroidBindableRecyclerView) : GestureDetector.SimpleOnGestureListener() {

    private var mRecyclerView: WeakReference<AndroidBindableRecyclerView>? = null

    private var mRecyclerViewClickListener: ItemClickListener? = null

    private val recyclerView: AndroidBindableRecyclerView?
        get() = if (mRecyclerView == null) {
            null
        } else mRecyclerView!!.get()

    init {
        mRecyclerView = WeakReference(bindableRecyclerView)
    }

    protected fun getChildAt(event: MotionEvent): View? {
        val recyclerView = recyclerView
        return recyclerView?.findChildViewUnder(event.x, event.y)
    }

    override fun onSingleTapConfirmed(event: MotionEvent): Boolean {
        val recyclerView = recyclerView ?: return false
        val childView = getChildAt(event) ?: return false

        val position = recyclerView.getChildAdapterPosition(childView)
        val childAt = getChildOfAt(childView, event.x.toInt(), event.y.toInt())

        if (childAt != null && ViewCompat.hasOnClickListeners(childAt)) {
            return true
        }

        if (position == RecyclerView.NO_POSITION) {
            return true
        }

        if (recyclerView.dataSet == null) {
            // Adapter is not BindableRecyclerViewAdapter or setItemSource hasn't been called
            return true
        }

        val listener = mRecyclerViewClickListener ?: return false
        val dataSet: List<Any>? = recyclerView.dataSet ?: return false
        val clickedObject: Any = dataSet?.get(position) ?: return false
        listener.onItemClicked(childView, clickedObject)
        return true
    }

    protected fun getChildOfAt(view: View, x: Int, y: Int): View? {
        return hit(view, x - view.x, y - view.y)
    }

    protected fun pointInView(view: View, localX: Float, localY: Float): Boolean {
        return (localX >= 0 && localX < view.right - view.left
                && localY >= 0 && localY < view.bottom - view.top)
    }

    protected fun isHittable(view: View): Boolean {
        return view.visibility == View.VISIBLE && view.alpha >= 0.001f
    }

    protected fun isTransformedPointInView(
            parent: ViewGroup?,
            child: View?,
            x: Float,
            y: Float, outLocalPoint: PointF?): Boolean {

        if (parent == null || child == null) {
            return false
        }

        val localX = x + parent.scrollX - child.left
        val localY = y + parent.scrollY - child.top

        val isInView = pointInView(child, localX, localY)
        if (isInView && outLocalPoint != null) {
            outLocalPoint.set(localX, localY)
        }
        return isInView
    }

    protected fun hit(
            view: View,
            x: Float,
            y: Float): View? {

        if (!isHittable(view)) {
            return null
        }

        if (!pointInView(view, x, y)) {
            return null
        }

        if (view !is ViewGroup) {
            return view
        }

        if (view.childCount > 0) {
            val localPoint = PointF()

            for (i in view.childCount - 1 downTo 0) {
                val child = view.getChildAt(i)

                if (isTransformedPointInView(view, child, x, y, localPoint)) {
                    val childResult = hit(
                            child,
                            localPoint.x,
                            localPoint.y)

                    if (childResult != null) {
                        return childResult
                    }
                }
            }
        }

        return view
    }

    internal fun setRecyclerViewClickListener(recyclerViewClickListener: ItemClickListener) {
        mRecyclerViewClickListener = recyclerViewClickListener
    }
}