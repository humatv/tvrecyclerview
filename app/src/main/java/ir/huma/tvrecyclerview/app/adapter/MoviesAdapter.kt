package ir.huma.tvrecyclerview.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ir.huma.tvrecyclerview.app.R
import ir.huma.tvrecyclerview.app.model.Movie
import ir.huma.tvrecyclerview.app.utils.Constants

class MoviesAdapter(private val context: Context, private var movies: List<Movie>,val type:Int) :
    RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder =
        MoviesViewHolder(
            if (type == Constants.VERTICAL_RECYCLER_VIEW_TYPE)
                LayoutInflater
                .from(context)
                .inflate(R.layout.item_vertical_recycler_view, parent, false)
            else
                LayoutInflater
                    .from(context)
                    .inflate(R.layout.item_horizontal_recycler_view, parent, false)
        )

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) = holder.bind(movies[position])

    override fun getItemCount(): Int = movies.size

    class MoviesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val movieImage: ImageView = itemView.findViewById(R.id.item_recycler_view_image)
        private val movieTitle: TextView = itemView.findViewById(R.id.item_recycler_view_title)
        private val movieImdb: TextView = itemView.findViewById(R.id.item_recycler_view_imdb)
        private val movieYear: TextView = itemView.findViewById(R.id.item_recycler_view_year)
        fun bind(movie: Movie) {
            Picasso.get()
                .load(movie.poster)
                .into(movieImage)
            movieTitle.text = movie.title
            movieImdb.text = "${movie.imdb_rating}/10"
            movieYear.text = movie.year
        }
    }

}