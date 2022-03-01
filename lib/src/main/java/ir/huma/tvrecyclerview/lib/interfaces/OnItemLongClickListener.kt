package ir.huma.tvrecyclerview.lib.interfaces

import androidx.recyclerview.widget.RecyclerView

interface OnItemLongClickListener {
    fun onItemLongClick(position: Int, obj: Any?, v: RecyclerView.ViewHolder?, adapter: RecyclerView.Adapter<*>?)
}