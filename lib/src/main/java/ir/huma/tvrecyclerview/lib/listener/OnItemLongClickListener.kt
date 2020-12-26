package ir.huma.tvrecyclerview.lib.listener

import androidx.recyclerview.widget.RecyclerView

interface OnItemLongClickListener {
    fun onItemLongClick(position: Int, obj: Any?, v: RecyclerView.ViewHolder?, adapter: RecyclerView.Adapter<*>?)
}