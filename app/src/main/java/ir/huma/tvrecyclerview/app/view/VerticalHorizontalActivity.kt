package ir.huma.tvrecyclerview.app.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ir.huma.tvrecyclerview.app.R
import ir.huma.tvrecyclerview.app.data.APIService
import ir.huma.tvrecyclerview.app.data.Repository
import ir.huma.tvrecyclerview.app.model.Row
import ir.huma.tvrecyclerview.lib.TvRecyclerView
import ir.huma.tvrecyclerview.lib.adapter.BaseRVAdapter
import ir.huma.tvrecyclerview.lib.listener.OnItemSelectedListener

class VerticalHorizontalActivity : AppCompatActivity() {

    private val TAG = "VerticalRecyclerView"
    private lateinit var verticalRecyclerViewActivity: TvRecyclerView
    private lateinit var repository: Repository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vertical_horizontal)
        init()
        initViews()
    }

    private fun init() {
        repository = Repository(APIService.api)
    }

    private fun initViews() {
        verticalRecyclerViewActivity = findViewById(R.id.vertical_recycler_view)
        var adapter =
            BaseRVAdapter<Row.MyHolder, Row>(
                this, Row.MyHolder::class.java,
                arrayListOf(Row(1), Row(2), Row(3),Row(4), Row(5), Row(6)), R.layout.row_horizontal_recycler_view
            )
        adapter.setObjects(repository)
        verticalRecyclerViewActivity.adapter = adapter
        (verticalRecyclerViewActivity.layoutManager as GridLayoutManager).initialPrefetchItemCount = 10
        verticalRecyclerViewActivity.onItemSelectedListener = object : OnItemSelectedListener{
            override fun onItemSelected(
                position: Int,
                obj: Any?,
                v: RecyclerView.ViewHolder?,
                adapter: RecyclerView.Adapter<*>?
            ) {
                v?.itemView?.requestFocus()
            }

        }

    }

}