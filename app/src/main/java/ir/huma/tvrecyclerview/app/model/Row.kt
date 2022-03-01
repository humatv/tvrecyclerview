package ir.huma.tvrecyclerview.app.model

import ir.huma.tvrecyclerview.app.R
import ir.huma.tvrecyclerview.app.holders.RowHolder
import ir.huma.tvrecyclerview.lib.adapter.BaseViewHolder
import ir.huma.tvrecyclerview.lib.adapter.BaseViewHolderItem

@BaseViewHolder(BaseViewHolderItem(R.layout.row_horizontal_recycler_view, RowHolder::class, 0))
class Row(val page: Int)