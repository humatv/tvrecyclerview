package ir.huma.tvrecyclerview.app.data

import io.reactivex.Single
import ir.huma.tvrecyclerview.app.model.MoviesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiProvider {
    @GET("movies")
    fun getMovies(@Query("page")page : Int =1): Single<MoviesResponse>
}