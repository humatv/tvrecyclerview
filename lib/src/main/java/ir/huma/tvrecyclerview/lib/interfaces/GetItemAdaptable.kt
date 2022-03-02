package ir.huma.tvrecyclerview.lib.interfaces

interface GetItemAdaptable<MODEL> {
    fun getItem(position: Int): MODEL?
}