package ir.huma.tvrecyclerview.app.holders

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import ir.huma.tvrecyclerview.app.R
import ir.huma.tvrecyclerview.app.model.Movie
import ir.huma.tvrecyclerview.lib.adapter.BaseRVHolder


class MovieHolder(itemView: View, context: Context) : BaseRVHolder<Movie>(itemView, context) {
    private val movieImage: ImageView = itemView.findViewById(R.id.item_recycler_view_image)
    private val movieTitle: TextView = itemView.findViewById(R.id.item_recycler_view_title)
    private val movieImdb: TextView = itemView.findViewById(R.id.item_recycler_view_imdb)
    private val movieYear: TextView = itemView.findViewById(R.id.item_recycler_view_year)

    override fun fill(movie: Movie, pos: Int, viewType: Int) {
        Picasso.get()
            .load(movie.poster)
            .into(movieImage)
        movieTitle.text = movie.title
        movieImdb.text = "${movie.imdb_rating}/10"
        movieYear.text = movie.year
    }
}