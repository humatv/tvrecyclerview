package ir.huma.tvrecyclerview.app.view

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import ir.huma.tvrecyclerview.app.R
import ir.huma.tvrecyclerview.app.data.APIService
import ir.huma.tvrecyclerview.app.data.Repository
import ir.huma.tvrecyclerview.app.model.Movie
import ir.huma.tvrecyclerview.lib.TvRecyclerView
import ir.huma.tvrecyclerview.lib.adapter.BaseRVAdapter
import ir.huma.tvrecyclerview.lib.listener.OnItemClickListener

class VerticalRecyclerViewActivity : AppCompatActivity() {
    private val TAG = "VerticalRecyclerView"
    private lateinit var verticalRecyclerViewActivity: TvRecyclerView
    private lateinit var repository: Repository
    private lateinit var moviesAdapter: BaseRVAdapter<Movie.MovieHolder, Movie>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vertical_recycler_view)
        init()
        initViews()
        getMovies(1)
    }

    var pageCount = 5

    private fun init() {
        repository = Repository(APIService.api)
    }

    private fun initViews() {
        verticalRecyclerViewActivity = findViewById(R.id.vertical_recycler_view)
        moviesAdapter = BaseRVAdapter(
            this@VerticalRecyclerViewActivity,
            Movie.MovieHolder::class.java,
            R.layout.item_vertical_recycler_view
        )
        verticalRecyclerViewActivity.adapter = moviesAdapter

        verticalRecyclerViewActivity.onItemClickListener = object : OnItemClickListener {
            override fun onItemClick(
                position: Int,
                obj: Any?,
                v: RecyclerView.ViewHolder?,
                adapter: RecyclerView.Adapter<*>?
            ) {
                moviesAdapter.removeItem(position)
            }
        }
    }

    private fun getMovies(page: Int) {
        repository.getMovies(page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    moviesAdapter.addAll(it.data)
                    Log.d(TAG, "getMovies: success data : ${it.data.size}")

                    if (page < pageCount) {
                        getMovies(page + 1)
                    }
                },
                onError = {
                    Log.e(TAG, "getMovies: ${it.message}")
                    Toast.makeText(this, "Network Problem!", Toast.LENGTH_SHORT).show()
                },
            )
    }
}