package ir.huma.tvrecyclerview.lib.interfaces

interface ViewTypeHandler {
    fun handleViewType(position: Int, item: Any?) : Int
}