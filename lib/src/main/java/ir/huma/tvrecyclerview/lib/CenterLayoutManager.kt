package ir.huma.tvrecyclerview.lib

import android.content.Context
import android.util.DisplayMetrics
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SmoothScroller
import androidx.recyclerview.widget.LinearSmoothScroller

class CenterLayoutManager(
    context: Context?,
    spanCount: Int,
    orientation: Int,
    reverseLayout: Boolean
) : GridLayoutManager(context, spanCount, orientation, reverseLayout) {

    private var millisecondPerInch = 35f // (bigger = slower)

    fun setMillisecondPerInch(millisecondPerInch: Float) {
        this.millisecondPerInch = millisecondPerInch
    }

    override fun smoothScrollToPosition(
        recyclerView: RecyclerView,
        state: RecyclerView.State,
        position: Int
    ) {
        val smoothScroller: SmoothScroller =
            CenterSmoothScroller(recyclerView.context, millisecondPerInch)
        smoothScroller.targetPosition = position
        startSmoothScroll(smoothScroller)
    }

    private class CenterSmoothScroller constructor(
        context: Context?,
        var millisecondPerInch: Float
    ) : LinearSmoothScroller(context) {
        override fun calculateDtToFit(
            viewStart: Int,
            viewEnd: Int,
            boxStart: Int,
            boxEnd: Int,
            snapPreference: Int
        ): Int {
            return boxStart + (boxEnd - boxStart) / 2 - (viewStart + (viewEnd - viewStart) / 2)
        }

        override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
            return millisecondPerInch / displayMetrics.densityDpi
        }
    }
}