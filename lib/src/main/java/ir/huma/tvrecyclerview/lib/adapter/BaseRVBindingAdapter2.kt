package ir.huma.tvrecyclerview.lib.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.databinding.ViewDataBinding
import androidx.databinding.DataBindingUtil
import ir.huma.tvrecyclerview.lib.interfaces.Adaptable
import ir.huma.tvrecyclerview.lib.interfaces.ViewTypeHandler
import java.util.*

/**
 * Created by hamedgramzi on 2016-08-17.
 */
class BaseRVBindingAdapter2<MODEL>() :
    AbstractBaseRVAdapter<MODEL>() {

    var holderMap = HashMap<Int, BaseViewHolderItem>()
        private set

    companion object {
        const val TAG = "BaseRVAdapter2"

        inline fun <reified MODEL> create(
            context: Context,
            items: ArrayList<MODEL> = ArrayList()
        ): BaseRVBindingAdapter2<MODEL> {

            var adapter = BaseRVBindingAdapter2<MODEL>()
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
        val binding =
            DataBindingUtil.inflate<ViewDataBinding>(
                LayoutInflater.from(parent.context),
                viewHolderAnnotation!!.resLayout,
                parent,
                false
            )

        val holderInstance =
            viewHolderAnnotation.modelClass.java.getConstructor(
                View::class.java,
                Context::class.java
            ).newInstance(binding.root, context)
        holderInstance.binding = binding
        holderInstance.objects = objects
        holderInstance.adapter = this
        return holderInstance as BaseRVHolder<MODEL>
    }

    override fun getItem(position: Int): MODEL? {
        return if (position < items.size) items[position] else null
    }

    override fun onBindViewHolder(holder: BaseRVHolder<MODEL>, position: Int) {
        holder.fill(items[position], position, getItemViewType(position))
    }

}