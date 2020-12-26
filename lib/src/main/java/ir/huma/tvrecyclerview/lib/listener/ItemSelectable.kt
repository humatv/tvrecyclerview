package ir.huma.tvrecyclerview.lib.listener

interface ItemSelectable {
    fun changeSelected(isSelected: Boolean, focus: Boolean, pos: Int, obj: Any?)
}