package ir.huma.tvrecyclerview.lib.adapter

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.databinding.ViewDataBinding

/**
 * Created by hamedgramzi on 2016-08-17.
 */
abstract class BaseRVHolder<T>(itemView: View, val context: Context) :
    RecyclerView.ViewHolder(itemView) {

    var binding: ViewDataBinding? = null
    var objects: Array<Any>? = null
    var adapter: RecyclerView.Adapter<*>? = null

    abstract fun fill(t: T, pos: Int, viewType: Int)
}