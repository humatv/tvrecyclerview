package ir.huma.tvrecyclerview.app.data

class Repository(private val apiProvider: ApiProvider) {
    fun getMovies(page : Int = 1) = apiProvider.getMovies(page)
}