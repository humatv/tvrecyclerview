package ir.huma.tvrecyclerview.lib.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ir.huma.tvrecyclerview.lib.interfaces.Adaptable
import ir.huma.tvrecyclerview.lib.interfaces.GetItemAdaptable
import ir.huma.tvrecyclerview.lib.interfaces.ViewTypeHandler
import java.util.HashMap

/**
 * Created by hamedgramzi on 2022-03-01.
 */
class BaseRVAdapter2<MODEL>() : AbstractBaseRVAdapter<MODEL>() {

    var holderMap = HashMap<Int, BaseViewHolderItem>()
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

    override fun onBindViewHolder(holder: BaseRVHolder<MODEL>, position: Int) {
        holder.fill(items[position], position, getItemViewType(position))
    }

}