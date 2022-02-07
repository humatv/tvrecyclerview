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
import ir.huma.tvrecyclerview.app.data.ApiProvider
import ir.huma.tvrecyclerview.app.data.Repository
import ir.huma.tvrecyclerview.app.utils.Constants
import ir.huma.tvrecyclerview.lib.MyVerticalRecyclerView

class VerticalRecyclerViewActivity : AppCompatActivity() {
    private val TAG = "VerticalRecyclerView"
    private lateinit var verticalRecyclerViewActivity: MyVerticalRecyclerView
    private lateinit var repository: Repository
    private lateinit var moviesAdapter: MoviesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vertical_recycler_view)
        init()
        initViews()
        getMovies()
    }

    private fun init(){
        repository = Repository(APIService.api)
    }

    private fun initViews() {
        verticalRecyclerViewActivity = findViewById(R.id.vertical_recycler_view)
        verticalRecyclerViewActivity.rowCount = 4
    }

    private fun getMovies() {
        repository.getMovies()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    moviesAdapter = MoviesAdapter(this,it.data,Constants.VERTICAL_RECYCLER_VIEW_TYPE)
                    verticalRecyclerViewActivity.adapter = moviesAdapter
                },
                onError = {
                    Log.e(TAG, "getMovies: ${it.message}")
                    Toast.makeText(this, "Network Problem!", Toast.LENGTH_SHORT).show()
                },
            )
    }
}