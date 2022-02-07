package ir.huma.tvrecyclerview.app.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import ir.huma.tvrecyclerview.app.R
import ir.huma.tvrecyclerview.app.adapter.MoviesAdapter
import ir.huma.tvrecyclerview.app.data.APIService
import ir.huma.tvrecyclerview.app.data.Repository
import ir.huma.tvrecyclerview.app.utils.Constants
import ir.huma.tvrecyclerview.lib.MyHorizontalRecyclerView
import ir.huma.tvrecyclerview.lib.MyVerticalRecyclerView

class HorizontalRecyclerViewActivity : AppCompatActivity() {
    private val TAG = "HorizontalRecyclerView"
    private lateinit var horizontalRecyclerViewActivity: MyHorizontalRecyclerView
    private lateinit var repository: Repository
    private lateinit var moviesAdapter: MoviesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_horizontal_recycler_view)
        init()
        initViews()
        getMovies()
    }
    private fun init(){
        repository = Repository(APIService.api)
    }

    private fun initViews() {
        horizontalRecyclerViewActivity = findViewById(R.id.horizontal_recycler_view)
        horizontalRecyclerViewActivity.rowCount = 2
    }

    private fun getMovies() {
        repository.getMovies()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    moviesAdapter = MoviesAdapter(this,it.data,Constants.HORIZONTAL_RECYCLER_VIEW_TYPE)
                    horizontalRecyclerViewActivity.adapter = moviesAdapter
                },
                onError = {
                    Log.e(TAG, "getMovies: ${it.message}")
                    Toast.makeText(this, "Network Problem!", Toast.LENGTH_SHORT).show()
                },
            )
    }
}