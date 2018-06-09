package com.blackbelt.bindings.recyclerviewbindings

import android.content.Context
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SnapHelper
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import com.blackbelt.bindings.BaseBindableRecyclerView


class AndroidBindableRecyclerView(context: Context, attrs: AttributeSet?) : BaseBindableRecyclerView(context, attrs) {

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

    private inner class PageScrollListener internal constructor(private val mPageDescriptor: PageDescriptor?) : RecyclerView.OnScrollListener() {

        private var mVisiblePosition: IntArray? = null

        private var mPage = 1

        init {
            mPage = mPageDescriptor!!.getStartPage()
        }

        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            val pageDescriptor: PageDescriptor = mPageDescriptor ?: return
            val layoutManager: LayoutManager = recyclerView?.layoutManager ?: return
            val totalItemCount = layoutManager.itemCount
            val lastVisibleItem = getLastVisibleItemPosition(layoutManager)
            if (totalItemCount - lastVisibleItem <= pageDescriptor.getThreshold()) {
                if (pageDescriptor.getCurrentPage() < 1 + totalItemCount / pageDescriptor.getPageSize()) {
                    pageDescriptor.setCurrentPage(1 + totalItemCount / pageDescriptor.getPageSize())
                    if (mOnPageChangeListener != null) {
                        mOnPageChangeListener?.onPageChangeListener(pageDescriptor)
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
                mOnPageChangeListener?.onPageChangeListener(mPageDescriptor)
            }
        }
    }

    fun setOnPageChangeListener(pageChangeListener: OnPageChangeListener) {
        mOnPageChangeListener = pageChangeListener
        if (mPageScrollListener != null) {
            mPageScrollListener!!.setOnPageChangeListener(pageChangeListener)
        }
    }

    override fun getDataSet(): List<Any>? =
            (adapter as? AndroidBindableRecyclerViewAdapter)?.dataSet

    override fun getItemAtPosition(position: Int): Any? = getDataSet()?.get(position)
}
