package com.blackbelt.bindings.recyclerviewbindings

import android.databinding.DataBindingUtil
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.databinding.ViewDataBinding
import android.support.v4.util.Pair
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

    private var mSetValuesDisposable = Disposables.disposed()

    val dataSet: ObservableList<Any> = ObservableArrayList()

    private val pendingUpdates = ArrayDeque<List<Any>>()

    init {
        if (items != null) {
            setDataSet(ArrayList(items))
        }
    }

    fun setDataSet(items: List<Any>?) {
        val oldItems = ArrayList(dataSet)
        mSetValuesDisposable.dispose()
        mSetValuesDisposable = Observable.just(items!!)
                .subscribeOn(Schedulers.computation())
                .map { newList ->
                    Pair<List<*>, DiffUtil.DiffResult>(newList,
                            calculateDiff(ItemSourceDiffCallback(oldItems, items)))
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::applyDiffResult, Throwable::printStackTrace)
    }

    private fun applyDiffResult(resultPair: Pair<List<*>, DiffUtil.DiffResult>) {
        var firstStart = true

        if (!pendingUpdates.isEmpty()) {
            pendingUpdates.remove()
        }

        if (dataSet.size > 0) {
            dataSet.clear()
            firstStart = false
        }

        if (resultPair.first != null) {
            dataSet.addAll(ArrayList(resultPair.first))
        }

        //if we call DiffUtil.DiffResult.dispatchUpdatesTo() on an empty adapter, it will crash - we have to call notifyDataSetChanged()!
        if (firstStart) {
            notifyDataSetChanged()
        } else {
            resultPair.second?.dispatchUpdatesTo(this)
        }

        if (pendingUpdates.size > 0) {
            setDataSet(pendingUpdates.peek())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindableViewHolder {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(parent.context), viewType, parent, false)
        return BindableViewHolder(binding)
    }

    override fun getItemViewType(position: Int): Int {
        val key = dataSet[position].javaClass
        val dataBinder: AndroidItemBinder = mItemBinder[key]
                ?: throw Exception("AndroidDataBinder not configured correctly $key")
        return dataBinder.layoutId
    }

    override fun onBindViewHolder(holder: BindableViewHolder, position: Int) {
        val item = dataSet[position]
        val itemBinder = mItemBinder[item.javaClass] ?: return
        holder.mViewDataBinding.setVariable(itemBinder.dataBindingVariable, item)
        holder.mViewDataBinding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}