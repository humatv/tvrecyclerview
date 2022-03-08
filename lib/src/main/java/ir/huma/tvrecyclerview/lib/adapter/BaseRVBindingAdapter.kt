package ir.huma.tvrecyclerview.lib.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * Created by hamedgramzi on 2016-08-17.
 */
class BaseRVBindingAdapter<T : BaseRVHolder<MODEL>, MODEL>() : AbstractBaseRVAdapter<MODEL>() {

    private lateinit var holder: Class<T>
    private lateinit var layout: IntArray

    constructor(
        context: Context,
        holder: Class<T>,
        items: ArrayList<MODEL>,
        vararg layout: Int
    ) : this() {
        this.context = context
        this.holder = holder
        this.items.addAll(items)
        this.layout = layout
    }

    constructor(context: Context, holder: Class<T>, vararg layout: Int) : this(
        context,
        holder,
        ArrayList<MODEL>(),
        *layout
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRVHolder<MODEL> {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            layout[viewType],
            parent,
            false
        )

        val holderInstance: T =
            holder.getConstructor(View::class.java, Context::class.java)
                .newInstance(binding.root, context)
        holderInstance.binding = binding
        holderInstance.objects = objects
        holderInstance.adapter = this
        return holderInstance

    }


    override fun onBindViewHolder(holder: BaseRVHolder<MODEL>, position: Int) {
        holder.fill(items[position], position, getItemViewType(position))
    }

}