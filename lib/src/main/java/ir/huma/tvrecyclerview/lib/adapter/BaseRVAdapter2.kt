package ir.huma.tvrecyclerview.lib.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ir.huma.tvrecyclerview.lib.interfaces.ViewTypeHandler
import java.util.HashMap

/**
 * Created by hamedgramzi on 2022-03-01.
 */
class BaseRVAdapter2<MODEL>() : RecyclerView.Adapter<BaseRVHolder<MODEL>>() {

    lateinit var context: Context
    val items: java.util.ArrayList<MODEL> = java.util.ArrayList()
    var viewTypeHandler: ViewTypeHandler? = null
    var holderMap = HashMap<Int, BaseViewHolderItem>()
        private set

    var objects: Array<Any>? = null
        private set

    companion object {
        const val TAG = "BaseRVAdapter2"

        inline fun <reified MODEL> create(
            context: Context,
            items: ArrayList<MODEL> = ArrayList()
        ): BaseRVAdapter2<MODEL> {

            var adapter = BaseRVAdapter2<MODEL>()
            adapter.context = context
            adapter.items.addAll(items)
            var holder =
                MODEL::class.java.annotations.find { it is BaseViewHolder } as BaseViewHolder
            for (item in holder.items) {
                if (item is BaseViewHolderItem) {
                    adapter.holderMap.put(item.type, item)
                }
            }
            return adapter
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRVHolder<MODEL> {
        val viewHolderAnnotation: BaseViewHolderItem = holderMap.get(viewType)
            ?: throw RuntimeException("you should add ViewHolder Annotation in your MODEL class")

        val view = LayoutInflater.from(parent.context)
            .inflate(viewHolderAnnotation!!.resLayout, parent, false)
        val holderInstance =
            viewHolderAnnotation.modelClass.java.getConstructor(
                View::class.java,
                Context::class.java
            ).newInstance(view, context)
        holderInstance.objects = objects
        holderInstance.adapter = this
        return holderInstance as BaseRVHolder<MODEL>
    }

    fun getItem(position: Int): MODEL? {
        return if (position < items.size) items[position] else null
    }

    override fun onBindViewHolder(holder: BaseRVHolder<MODEL>, position: Int) {
        holder.fill(items[position], position, getItemViewType(position))
    }

    override fun getItemViewType(position: Int): Int {
        if (viewTypeHandler != null)
            return viewTypeHandler!!.handleViewType(position, items.get(position))
        return super.getItemViewType(position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setObjects(vararg args: Any) {
        objects = args as Array<Any>
    }

    fun removeItem(model: MODEL): Boolean {
        val index = items.indexOf(model)
        if (index != -1) {
            items.remove(model)
            notifyItemRemoved(index)
            return true
        }
        return false
    }

    fun removeItem(pos: Int): MODEL? {
        if (pos < items.size) {
            val m = items.removeAt(pos)
            notifyItemRemoved(pos)
            return m
        }
        return null
    }

    fun addItem(model: MODEL) {
        items.add(model)
        notifyItemInserted(items.size - 1)
    }

    fun addItem(model: MODEL, position: Int) {
        items.add(position, model)
        notifyItemInserted(position - 1)
    }

    fun updateItem(model: MODEL): Boolean {
        var index: Int
        if (items.indexOf(model).also { index = it } != -1) {
            updateItem(model, index)
            return true
        }
        return false
    }

    fun updateItem(model: MODEL, pos: Int) {
        if (pos > items.size) {
            items.add(model)
        } else {
            items.removeAt(pos)
            items.add(pos, model)
        }
        notifyItemChanged(pos)
    }

    fun removeAll() {
        val size = items.size
        items.clear()
        notifyItemRangeRemoved(0, size)
    }

    fun addAll(list: List<MODEL>) {
        val size = items.size
        items.addAll(list)
        notifyItemRangeInserted(size, list.size)
    }

    fun addAll(vararg list: MODEL) {
        val size = items.size
        items.addAll(listOf(*list))
        notifyItemRangeInserted(size, list.size)
    }

    fun addAll(pos: Int, list: List<MODEL>) {
        items.addAll(pos, list)
        notifyItemRangeInserted(pos, list.size)
    }

    fun addAll(pos: Int, vararg list: MODEL) {
        items.addAll(pos, listOf(*list))
        notifyItemRangeInserted(pos, list.size)
    }


}