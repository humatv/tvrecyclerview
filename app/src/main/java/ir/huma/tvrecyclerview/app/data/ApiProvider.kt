package ir.huma.tvrecyclerview.app.data

import io.reactivex.Single
import ir.huma.tvrecyclerview.app.model.MoviesResponse
import retrofit2.http.GET

interface ApiProvider {
    @GET("movies")
    fun getMovies(): Single<MoviesResponse>
}