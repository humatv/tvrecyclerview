package ir.huma.tvrecyclerview.lib.interfaces

import android.view.View

/**
 * Created by hamed on 10/1/2016.
 */
interface RecyclerClickListener {
    fun onClick(view: View?, position: Int)
    fun onLongClick(view: View?, position: Int)
}