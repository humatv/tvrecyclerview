package ir.huma.tvrecyclerview.app.holders

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
import ir.huma.tvrecyclerview.app.model.Movie
import ir.huma.tvrecyclerview.app.model.Row
import ir.huma.tvrecyclerview.lib.TvRecyclerView
import ir.huma.tvrecyclerview.lib.adapter.BaseRVAdapter2
import ir.huma.tvrecyclerview.lib.adapter.BaseRVHolder
import ir.huma.tvrecyclerview.lib.interfaces.OnItemClickListener
import ir.huma.tvrecyclerview.lib.interfaces.OnItemLongClickListener
import ir.huma.tvrecyclerview.lib.interfaces.ViewTypeHandler

class RowHolder(itemView: View, context: Context) : BaseRVHolder<Row>(itemView, context) {
    private val TAG = "Row.MyHolder"
    override fun fill(t: Row, pos: Int, viewType: Int) {
        var moviesAdapter = BaseRVAdapter2.create<Movie>(context)
        var recyclerView = itemView.findViewById<TvRecyclerView>(R.id.horizontal_recycler_view)
        recyclerView.adapter = moviesAdapter
        itemView.findViewById<TextView>(R.id.titleTextView).text = "page ${t.page}"
        recyclerView.selectLastPosition()
        moviesAdapter.viewTypeHandler = object : ViewTypeHandler {
            override fun handleViewType(position: Int, item: Any?): Int {
                if (position % 2 == 0) {
                    return 1
                }
                return 0
            }
        }
        getMovies(moviesAdapter, t.page)

        recyclerView.onItemClickListener = object : OnItemClickListener {
            override fun onItemClick(
                position: Int,
                obj: Any?,
                v: RecyclerView.ViewHolder?,
                adapter: RecyclerView.Adapter<*>?
            ) {
                Toast.makeText(
                    context,
                    "click : row:${pos} column:${position}",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

        recyclerView.onItemLongClickListener = object : OnItemLongClickListener {
            override fun onItemLongClick(
                position: Int,
                obj: Any?,
                v: RecyclerView.ViewHolder?,
                adapter: RecyclerView.Adapter<*>?
            ) {
                Toast.makeText(
                    context,
                    "longClick : row:${pos} column:${position}",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

    }

    private fun getMovies(moviesAdapter: BaseRVAdapter2<Movie>, page: Int) {
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