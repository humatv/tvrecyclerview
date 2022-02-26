package ir.huma.tvrecyclerview.app.model

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import ir.huma.tvrecyclerview.app.R
import ir.huma.tvrecyclerview.app.data.Repository
import ir.huma.tvrecyclerview.lib.TvRecyclerView
import ir.huma.tvrecyclerview.lib.adapter.BaseRVAdapter
import ir.huma.tvrecyclerview.lib.adapter.BaseRVHolder
import ir.huma.tvrecyclerview.lib.listener.OnItemClickListener

class Row(val page : Int) {

    class MyHolder(itemView: View, context: Context) : BaseRVHolder<Row>(itemView, context) {
        private  val TAG = "Row.MyHolder"
        override fun fill(t: Row, pos: Int, viewType: Int) {
            var moviesAdapter = BaseRVAdapter(
                context,
                Movie.MovieHolder::class.java,
                R.layout.item_vertical_recycler_view
            )
            var recyclerView = itemView.findViewById<TvRecyclerView>(R.id.horizontal_recycler_view)
            recyclerView.adapter = moviesAdapter
            itemView.findViewById<TextView>(R.id.titleTextView).text = "page ${t.page}"
            recyclerView.selectLastPosition()

            getMovies(moviesAdapter,t.page)
        }

        private fun getMovies(moviesAdapter: BaseRVAdapter<*,Movie>,page :Int) {
            (objects?.get(0) as Repository).getMovies(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                    onSuccess = {
                        moviesAdapter.addAll(it.data)
                        Log.d(TAG, "getMovies: success data : ${it.data.size}")

                    },
                    onError = {
                        Log.e(TAG, "getMovies: ${it.message}")
                        Toast.makeText(context, "Network Problem!", Toast.LENGTH_SHORT).show()
                    },
                )
        }
    }
}