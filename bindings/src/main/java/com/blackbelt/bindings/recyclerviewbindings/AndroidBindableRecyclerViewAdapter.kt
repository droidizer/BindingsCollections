package com.blackbelt.bindings.recyclerviewbindings

import android.databinding.DataBindingUtil
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.databinding.ViewDataBinding
import android.support.v4.util.Pair
import android.support.v7.recyclerview.extensions.AsyncListDiffer
import android.support.v7.util.DiffUtil
import android.support.v7.util.DiffUtil.calculateDiff
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import java.util.*


class AndroidBindableRecyclerViewAdapter internal constructor(private val mItemBinder: Map<Class<*>, AndroidItemBinder>,
                                                                 items: List<Any>?) : RecyclerView.Adapter<BindableViewHolder>() {

    val dataSet = AsyncListDiffer(this, callBack)

    init {
        if (items != null) {
            setDataSet(ArrayList(items))
        }
    }

    fun setDataSet(items: List<Any>?) {
        dataSet.submitList(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindableViewHolder {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(parent.context), viewType, parent, false)
        return BindableViewHolder(binding)
    }

    override fun getItemViewType(position: Int): Int {
        val key = dataSet.currentList[position].javaClass
        val dataBinder: AndroidItemBinder = mItemBinder[key]
                ?: throw Exception("AndroidDataBinder not configured correctly $key")
        return dataBinder.layoutId
    }

    override fun onBindViewHolder(holder: BindableViewHolder, position: Int) {
        val item = dataSet.currentList[position]
        val itemBinder = mItemBinder[item.javaClass] ?: return
        holder.mViewDataBinding.setVariable(itemBinder.dataBindingVariable, item)
        holder.mViewDataBinding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return dataSet.currentList.size
    }

    companion object {
        val callBack = object : DiffUtil.ItemCallback<Any>() {

            override fun areItemsTheSame(oldItem: Any?, newItem: Any?): Boolean = oldItem == newItem

            override fun areContentsTheSame(oldItem: Any?, newItem: Any?): Boolean = oldItem == newItem
        }
    }
}