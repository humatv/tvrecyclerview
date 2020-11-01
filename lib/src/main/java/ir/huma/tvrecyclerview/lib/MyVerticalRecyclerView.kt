package ir.huma.tvrecyclerview.lib

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewConfiguration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ir.atitec.everythingmanager.adapter.recyclerview.BaseRVAdapter

class MyVerticalRecyclerView : RecyclerView {
    var onItemClickListener : OnItemClickListener? = null
    var onItemLongClickListener : OnItemLongClickListener? = null
    var onItemSelectedListener : OnItemSelectedListener? = null

    var selectedPos = 0
    var lastNotifyChange = 0;
    var rowCount = 5
        set(value) {
            field = value
            super.setLayoutManager(CenterLayoutManager(context, field, GridLayoutManager.VERTICAL, false))
//            setOnKeyListener(onkeyListener)
            super.setOnFocusChangeListener(focusChangeListener)
            super.addOnScrollListener(onMyScrollListener)
//            addOnUnhandledKeyEventListener(unhandledKeyEventListener)
        }

    var onMyScrollListener = object : OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            when (newState) {
                SCROLL_STATE_IDLE -> doScroll(selectedPos)                   //we reached the target position
            }
        }
    }


//    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
//
//        return super.onKeyDown(keyCode, event);
//
//
////        var b =
////        Log.d("recyclerView"," keydown rowCount ${rowCount} ${b}")
////        return true
//    }
    var longPress = false;
    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
//        Log.d(MyHorizontalRecyclerView::class.java.name, "dispatchKeyEvent : ${event.toString()}")
        if (event?.action == KeyEvent.ACTION_DOWN) {
            if (event?.keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                if (selectedPos - rowCount >= 0) {
                    smoothScrollToPosition(selectedPos - rowCount)
                    doScroll(selectedPos - rowCount)
//                    Log.d(MyHorizontalRecyclerView::class.java.name, "dpadRight")
                }
                return true
            } else if (event?.keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                if (selectedPos + rowCount < adapter!!.itemCount) {
                    smoothScrollToPosition(selectedPos + rowCount)
                    doScroll(selectedPos + rowCount)
//                    Log.d(MyHorizontalRecyclerView::class.java.name, "dpadLeft")
                }
                return true
            } else if (event?.keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                if ((selectedPos + 1) % rowCount != 0 && selectedPos + 1 < adapter!!.itemCount) {
                    doScroll(selectedPos + 1)
                    temp = true
                    return true
                }
            } else if (event?.keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                if ((selectedPos - 1) % rowCount < rowCount - 1 && selectedPos - 1 >= 0) {
                    doScroll(selectedPos - 1)
                    temp = true
                    return true
                }
            } else if (event.keyCode == KeyEvent.KEYCODE_ENTER || event.keyCode == KeyEvent.KEYCODE_NUMPAD_ENTER || event.keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
                val eventDuration = event.eventTime - event.downTime
                if (eventDuration > ViewConfiguration.getLongPressTimeout()) {
                    if (!longPress) {
                        Log.d("MyVerticalGridView", "onKeyLongClick")
                        try {
                            if (onItemLongClickListener != null) {
                                onItemLongClickListener?.onItemLongClick(selectedPos, (adapter as BaseRVAdapter<*,*>).getItem(selectedPos), findViewHolderForLayoutPosition(selectedPos), adapter)
                            } else if (onItemClickListener != null) {
                                onItemClickListener?.onItemClick(selectedPos, (adapter as BaseRVAdapter<*,*>).getItem(selectedPos), findViewHolderForLayoutPosition(selectedPos), adapter)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    longPress = true
                    return true;
                }
            }
        } else if (event?.action == KeyEvent.ACTION_UP) {
            if(event.keyCode == KeyEvent.KEYCODE_ENTER || event.keyCode == KeyEvent.KEYCODE_NUMPAD_ENTER || event.keyCode == KeyEvent.KEYCODE_DPAD_CENTER){
                return if (longPress) {
                    longPress = false
                    temp = false;
                    return true
                } else {
                    Log.d("MyVerticalGridView", "onKeyClick")
                    try {
                        onItemClickListener?.onItemClick(selectedPos, (adapter as BaseRVAdapter<*,*>).getItem(selectedPos), findViewHolderForLayoutPosition(selectedPos), adapter)
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                    temp = false;
                    return true
                }
            }
            if (temp) {
                temp = false;
                return true
            }
        }

        return super.dispatchKeyEvent(event)
    }

//    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
//        Log.d(MyHorizontalRecyclerView::class.java.name, "keyUp : "+event?.action)
//
//        if (temp) {
//            temp = false;
//            return true
//        }
//
//        return super.onKeyUp(keyCode, event)
//    }

    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun setLayoutManager(layout: LayoutManager?) {
//        super.setLayoutManager(layout);
        throw RuntimeException("please set rowCount , dont need setLayoutManager...")
    }

    var temp = false;
//    var onkeyListener = OnKeyListener { v, keyCode, event ->
//        if (event?.action == KeyEvent.ACTION_DOWN) {
//
//        } else if (event?.action == KeyEvent.ACTION_UP) {
//
//        }
//        false
//    }

    override fun setOnFocusChangeListener(l: OnFocusChangeListener?) {
        myfocusChangeListener = l;
    }

    var myfocusChangeListener: OnFocusChangeListener? = null
    var focusChangeListener = OnFocusChangeListener { view: View, focus: Boolean ->
        if (adapter is BaseRVAdapter<*, *>) {
            var holder = findViewHolderForAdapterPosition(selectedPos)
            if (holder is ItemSelectable) {
                (holder as ItemSelectable).changeSelected(focus, selectedPos, (adapter as BaseRVAdapter<*, *>).getItem(selectedPos))
            }
        }
        myfocusChangeListener?.onFocusChange(view, focus)
    }

//
//    var unhandledKeyEventListener = OnUnhandledKeyEventListener{ view: View, event: KeyEvent ->
//        if(event?.action == KeyEvent.ACTION_UP){
//            if(temp){
//                temp = false;
//                true
//            }
//        }
//        false
//    }

    fun doScroll(selectedPos: Int) {
        temp = true;
        if (adapter is BaseRVAdapter<*, *>) {
            var holder = findViewHolderForAdapterPosition(this.selectedPos)
//            Log.d(MyHorizontalRecyclerView::class.java.name, "holder : ${holder.toString()}")

            if (holder is ItemSelectable) {
                (holder as ItemSelectable).changeSelected(false, this.selectedPos, (adapter as BaseRVAdapter<*, *>).getItem(this.selectedPos))
//                Log.d(MyHorizontalRecyclerView::class.java.name, "selected false ${this.selectedPos}")
            }

            holder = findViewHolderForAdapterPosition(selectedPos)
//            Log.d(MyHorizontalRecyclerView::class.java.name, "holder2 : ${holder.toString()}")

            if (holder is ItemSelectable) {
                (holder as ItemSelectable).changeSelected(true, selectedPos, (adapter as BaseRVAdapter<*, *>).getItem(selectedPos))
//                Log.d(MyHorizontalRecyclerView::class.java.name, "selected true ${selectedPos}")
                lastNotifyChange = selectedPos;
            }
        }
        this.selectedPos = selectedPos;
    }


    interface ItemSelectable {
        fun changeSelected(isSelected: Boolean, pos: Int, obj: Any?)
    }


    interface OnItemSelectedListener {
        fun onItemSelected(position: Int, obj: Any?, v: ViewHolder?, adapter: Adapter<*>?)
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, obj: Any?, v: ViewHolder?, adapter: Adapter<*>?)
    }

    interface OnItemLongClickListener {
        fun onItemLongClick(position: Int, obj: Any?, v: ViewHolder?, adapter: Adapter<*>?)
    }
}