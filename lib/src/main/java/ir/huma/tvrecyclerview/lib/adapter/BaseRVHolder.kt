package ir.huma.tvrecyclerview.lib.adapter

import android.content.Context
import android.view.View
import ir.huma.tvrecyclerview.lib.adapter.BaseRVHolder
import androidx.recyclerview.widget.RecyclerView
import ir.huma.tvrecyclerview.lib.adapter.BaseRVBindingHolder
import androidx.databinding.ViewDataBinding
import androidx.databinding.DataBindingUtil

/**
 * Created by hamedgramzi on 2016-08-17.
 */
abstract class BaseRVHolder<T>(itemView: View, val context: Context) :
    RecyclerView.ViewHolder(itemView) {

    var objects: Array<Any>? = null
    var adapter: RecyclerView.Adapter<*>? = null

    abstract fun fill(t: T, pos: Int, viewType: Int)
}