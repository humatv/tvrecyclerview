package ir.huma.tvrecyclerview.app.data

class Repository(private val apiProvider: ApiProvider) {
    fun getMovies() = apiProvider.getMovies()
}