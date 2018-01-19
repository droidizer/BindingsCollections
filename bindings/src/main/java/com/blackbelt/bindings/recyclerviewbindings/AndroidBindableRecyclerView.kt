package com.blackbelt.bindings.recyclerviewbindings

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent


class AndroidBindableRecyclerView(context: Context, attrs: AttributeSet?) : RecyclerView(context, attrs), RecyclerView.OnItemTouchListener {

    private var mRecyclerViewGestureListener: RecyclerViewGestureListener? = null

    private var mPageScrollListener: PageScrollListener? = null

    var pageDescriptor: PageDescriptor? = null
        set(pageDescriptor) {
            if (mPageScrollListener != null) {
                removeOnScrollListener(mPageScrollListener)
            }
            field = pageDescriptor
            mPageScrollListener = PageScrollListener(field)
            addOnScrollListener(mPageScrollListener)
        }

    private var mOnPageChangeListener: OnPageChangeListener? = null

    private var mGestureDetector: GestureDetector? = null

    val dataSet: List<Any>?
        get() = if (adapter is AndroidBindableRecyclerViewAdapter) {
            (adapter as AndroidBindableRecyclerViewAdapter).dataSet
        } else null


    private inner class PageScrollListener internal constructor(private val mPageDescriptor: PageDescriptor?) : RecyclerView.OnScrollListener() {

        private var mVisiblePosition: IntArray? = null

        private var mPage = 1

        init {
            mPage = mPageDescriptor!!.getStartPage()
        }

        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            if (recyclerView!!.adapter == null) {
                return
            }
            val layoutManager = recyclerView.layoutManager
            val totalItemCount = layoutManager.itemCount
            val lastVisibleItem = getLastVisibleItemPosition(layoutManager)
            if (totalItemCount - lastVisibleItem <= mPageDescriptor!!.getThreshold()) {
                if (mPageDescriptor.getCurrentPage() < 1 + totalItemCount / mPageDescriptor.getPageSize()) {
                    mPageDescriptor.setCurrentPage(1 + totalItemCount / mPageDescriptor.getPageSize())
                    if (mOnPageChangeListener != null) {
                        mOnPageChangeListener!!.onPageChangeListener(mPageDescriptor)
                    }
                }
            }
        }

        private fun getLastVisibleItemPosition(layoutManager: RecyclerView.LayoutManager): Int {
            if (layoutManager is LinearLayoutManager) {
                return layoutManager.findLastVisibleItemPosition()
            } else if (layoutManager is StaggeredGridLayoutManager) {
                if (mVisiblePosition == null) {
                    mVisiblePosition = IntArray(layoutManager.spanCount)
                }
                return layoutManager
                        .findLastVisibleItemPositions(mVisiblePosition)[0]
            }
            return 0
        }

        internal fun setOnPageChangeListener(onPageChangeListener: OnPageChangeListener) {
            mOnPageChangeListener = onPageChangeListener
            if (mPageDescriptor != null && mOnPageChangeListener != null) {
                mOnPageChangeListener!!.onPageChangeListener(mPageDescriptor)
            }
        }
    }

    fun setOnItemClickListener(l: ItemClickListener?) {
        if (l == null) {
            return
        }
        mRecyclerViewGestureListener = RecyclerViewGestureListener(this)
        mGestureDetector = GestureDetector(context, mRecyclerViewGestureListener)
        mRecyclerViewGestureListener!!.setRecyclerViewClickListener(l)
    }

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        return mGestureDetector != null && mGestureDetector!!.onTouchEvent(e)
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        addOnItemTouchListener(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeOnItemTouchListener(this)
    }


    fun setOnPageChangeListener(pageChangeListener: OnPageChangeListener) {
        mOnPageChangeListener = pageChangeListener
        if (mPageScrollListener != null) {
            mPageScrollListener!!.setOnPageChangeListener(pageChangeListener)
        }
    }
}
