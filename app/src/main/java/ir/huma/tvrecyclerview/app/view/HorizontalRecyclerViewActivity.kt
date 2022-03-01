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
import ir.huma.tvrecyclerview.app.holders.MovieHolder
import ir.huma.tvrecyclerview.app.model.Movie
import ir.huma.tvrecyclerview.lib.TvRecyclerView
import ir.huma.tvrecyclerview.lib.adapter.BaseRVAdapter
import ir.huma.tvrecyclerview.lib.interfaces.OnItemClickListener

class HorizontalRecyclerViewActivity : AppCompatActivity() {
    private val TAG = "HorizontalRecyclerView"
    private lateinit var horizontalRecyclerViewActivity: TvRecyclerView
    private lateinit var repository: Repository
    private lateinit var moviesAdapter: BaseRVAdapter<MovieHolder, Movie>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_horizontal_recycler_view)
        init()
        initViews()
        getMovies(1)
    }

    var pageCount = 5

    private fun init() {
        repository = Repository(APIService.api)
    }

    private fun initViews() {
        horizontalRecyclerViewActivity = findViewById(R.id.horizontal_recycler_view)
        moviesAdapter = BaseRVAdapter(
            this@HorizontalRecyclerViewActivity,
            MovieHolder::class.java,
            R.layout.item_horizontal_recycler_view
        )
        horizontalRecyclerViewActivity.adapter = moviesAdapter

        horizontalRecyclerViewActivity.onItemClickListener = object : OnItemClickListener {
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