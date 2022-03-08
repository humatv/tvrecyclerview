package ir.huma.tvrecyclerview.lib.interfaces

interface Adaptable<MODEL> :
    GetItemAdaptable<MODEL> {

    fun removeItem(model: MODEL): Boolean

    fun removeItem(pos: Int): MODEL?

    fun addItem(model: MODEL)

    fun addItem(model: MODEL, position: Int)

    fun updateItem(model: MODEL): Boolean

    fun updateItem(model: MODEL, pos: Int)

    fun removeAll()

    fun addAll(list: List<MODEL>)

    fun addAll(vararg list: MODEL)

    fun addAll(pos: Int, list: List<MODEL>)

    fun addAll(pos: Int, vararg list: MODEL)

}