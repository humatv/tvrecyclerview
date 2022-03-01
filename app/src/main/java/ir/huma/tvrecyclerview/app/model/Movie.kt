package ir.huma.tvrecyclerview.app.model

import com.google.gson.annotations.SerializedName
import ir.huma.tvrecyclerview.app.R
import ir.huma.tvrecyclerview.app.holders.MovieHolder
import ir.huma.tvrecyclerview.lib.adapter.BaseViewHolder
import ir.huma.tvrecyclerview.lib.adapter.BaseViewHolderItem

@BaseViewHolder(
    BaseViewHolderItem(R.layout.item_vertical_recycler_view, MovieHolder::class, 0),
    BaseViewHolderItem(R.layout.item_horizontal_recycler_view, MovieHolder::class, 1),
)
data class Movie(
    @SerializedName("title") var title: String,
    @SerializedName("poster") var poster: String,
    @SerializedName("year") var year: String,
    @SerializedName("imdb_rating") var imdb_rating: String,
)
