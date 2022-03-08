package ir.huma.tvrecyclerview.lib.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ir.huma.tvrecyclerview.lib.interfaces.Adaptable
import ir.huma.tvrecyclerview.lib.interfaces.ViewTypeHandler

/**
 * Created by hamedgramzi on 2022-03-01.
 */
abstract class AbstractBaseRVAdapter<MODEL>() : RecyclerView.Adapter<BaseRVHolder<MODEL>>(),Adaptable<MODEL> {

    lateinit var context: Context
    val items: java.util.ArrayList<MODEL> = java.util.ArrayList()
    var viewTypeHandler: ViewTypeHandler? = null

    var objects: Array<Any>? = null
        private set

    abstract override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseRVHolder<MODEL>

    override fun getItem(position: Int): MODEL? {
        return if (position < items.size) items[position] else null
    }

    abstract override fun onBindViewHolder(holder: BaseRVHolder<MODEL>, position: Int)

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

    override fun removeItem(model: MODEL): Boolean {
        val index = items.indexOf(model)
        if (index != -1) {
            items.remove(model)
            notifyItemRemoved(index)
            return true
        }
        return false
    }

    override fun removeItem(pos: Int): MODEL? {
        if (pos < items.size) {
            val m = items.removeAt(pos)
            notifyItemRemoved(pos)
            return m
        }
        return null
    }

    override fun addItem(model: MODEL) {
        items.add(model)
        notifyItemInserted(items.size - 1)
    }

    override fun addItem(model: MODEL, position: Int) {
        items.add(position, model)
        notifyItemInserted(position - 1)
    }

    override fun updateItem(model: MODEL): Boolean {
        var index: Int
        if (items.indexOf(model).also { index = it } != -1) {
            updateItem(model, index)
            return true
        }
        return false
    }

    override fun updateItem(model: MODEL, pos: Int) {
        if (pos > items.size) {
            items.add(model)
        } else {
            items.removeAt(pos)
            items.add(pos, model)
        }
        notifyItemChanged(pos)
    }

    override fun removeAll() {
        val size = items.size
        items.clear()
        notifyItemRangeRemoved(0, size)
    }

    override fun addAll(list: List<MODEL>) {
        val size = items.size
        items.addAll(list)
        notifyItemRangeInserted(size, list.size)
    }

    override fun addAll(vararg list: MODEL) {
        val size = items.size
        items.addAll(listOf(*list))
        notifyItemRangeInserted(size, list.size)
    }

    override fun addAll(pos: Int, list: List<MODEL>) {
        items.addAll(pos, list)
        notifyItemRangeInserted(pos, list.size)
    }

    override fun addAll(pos: Int, vararg list: MODEL) {
        items.addAll(pos, listOf(*list))
        notifyItemRangeInserted(pos, list.size)
    }


}