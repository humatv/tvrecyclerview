package ir.huma.tvrecyclerview.app.model

import com.google.gson.annotations.SerializedName

data class MoviesResponse(
    @SerializedName("data") var data:ArrayList<Movie>,
)
