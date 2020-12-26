package ir.huma.tvrecyclerview.lib.listener

import androidx.recyclerview.widget.RecyclerView

interface OnItemClickListener {
    fun onItemClick(position: Int, obj: Any?, v: RecyclerView.ViewHolder?, adapter: RecyclerView.Adapter<*>?)

}