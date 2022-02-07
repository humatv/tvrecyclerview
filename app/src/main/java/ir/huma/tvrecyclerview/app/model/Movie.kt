package ir.huma.tvrecyclerview.app.model

import com.google.gson.annotations.SerializedName

data class Movie(
    @SerializedName("title") var title:String,
    @SerializedName("poster") var poster:String,
    @SerializedName("year") var year:String,
    @SerializedName("imdb_rating") var imdb_rating:String,
)
