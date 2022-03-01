package ir.huma.tvrecyclerview.lib.adapter

import kotlin.reflect.KClass

@Target(AnnotationTarget.PROPERTY)
annotation class BaseViewHolderItem(
    val resLayout: Int,
    val modelClass: KClass<out BaseRVHolder<*>>,
    val type: Int = 0
)

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class BaseViewHolder(vararg val items: BaseViewHolderItem)
