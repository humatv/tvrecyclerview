package ir.huma.tvrecyclerview.lib.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.databinding.ViewDataBinding
import androidx.databinding.DataBindingUtil
import java.util.*

/**
 * Created by hamedgramzi on 2016-08-17.
 */
class BaseRVBindingAdapter<T : BaseRVBindingHolder<MODEL>, MODEL>(
    private val context: Context,
    private val holder: Class<T>,
    val items: ArrayList<MODEL>,
    private vararg val layout: Int
) : RecyclerView.Adapter<BaseRVBindingHolder<MODEL>>() {

    var objects: Array<Any>? = null
        private set

    constructor(context: Context, holder: Class<T>, vararg layout: Int) : this(
        context,
        holder,
        ArrayList<MODEL>(),
        *layout
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRVBindingHolder<MODEL> {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            layout[viewType],
            parent,
            false
        )

        val holderInstance: T =
            holder.getConstructor(ViewDataBinding::class.java, Context::class.java)
                .newInstance(binding, context)
        holderInstance.objects = objects
        holderInstance.adapter = this
        return holderInstance

    }

    fun getItem(position: Int): MODEL? {
        return if (position < items.size) items[position] else null
    }

    override fun onBindViewHolder(holder: BaseRVBindingHolder<MODEL>, position: Int) {
        holder.fill(items[position], position, getItemViewType(position))
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setObjects(vararg args: Any) {
        objects = arrayOf(args)
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
        notifyItemRangeRemoved(0,size)
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