package ir.huma.tvrecyclerview.lib.adapter

import android.content.Context
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by hamedgramzi on 2016-08-17.
 */
abstract class BaseRVBindingHolder<T>(val binding: ViewDataBinding, val context: Context) :
    RecyclerView.ViewHolder(binding.root) {

    var itemView: View = binding.root
    var objects: Array<Any>? = null
    var adapter: RecyclerView.Adapter<*>? = null

    abstract fun fill(t: T, pos: Int, viewType: Int)

}