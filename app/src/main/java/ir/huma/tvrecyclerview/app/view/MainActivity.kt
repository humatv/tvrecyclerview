package ir.huma.tvrecyclerview.app.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView
import ir.huma.tvrecyclerview.app.R

class MainActivity : AppCompatActivity() {
    private lateinit var verticalRecyclerViewItem : CardView
    private lateinit var horizontalRecyclerViewItem : CardView
    private lateinit var verticalhorizontalRecyclerViewItem : CardView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
    }

    private fun initViews() {
        verticalRecyclerViewItem = findViewById(R.id.vertical_recycler_view_item_layout)
        horizontalRecyclerViewItem = findViewById(R.id.horizontal_recycler_view_item_layout)
        verticalhorizontalRecyclerViewItem = findViewById(R.id.vertical_horizontal_recycler_view_item_layout)
        initListeners()
    }

    private fun initListeners() {
        verticalRecyclerViewItem.setOnClickListener {
            startActivity(Intent(this,VerticalRecyclerViewActivity::class.java))
        }
        horizontalRecyclerViewItem.setOnClickListener {
            startActivity(Intent(this,HorizontalRecyclerViewActivity::class.java))
        }
        verticalhorizontalRecyclerViewItem.setOnClickListener {
            startActivity(Intent(this,VerticalHorizontalActivity::class.java))
        }
    }
}