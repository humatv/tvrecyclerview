package ir.huma.tvrecyclerview.lib.listener

import androidx.recyclerview.widget.RecyclerView

interface OnItemSelectedWithoutFocusListener {
    fun onItemSelectedWithoutFocus(position: Int, obj: Any?, v: RecyclerView.ViewHolder?, adapter: RecyclerView.Adapter<*>?)
}