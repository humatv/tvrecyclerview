package ir.huma.tvrecyclerview.lib

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.view.View.OnFocusChangeListener
import android.view.animation.AnimationUtils
import android.widget.ScrollView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ir.atitec.everythingmanager.adapter.recyclerview.BaseRVAdapter
import ir.atitec.everythingmanager.utility.RecyclerClickListener
import ir.atitec.everythingmanager.utility.RecyclerTouchListener
import ir.huma.tvrecyclerview.lib.listener.*

class MyHorizontalRecyclerView : RecyclerView {
    private val TAG = "MyHorizontalRecyclerView"

    var onItemClickListener: OnItemClickListener? = null
    var onItemLongClickListener: OnItemLongClickListener? = null
    var onItemSelectedListener: OnItemSelectedListener? = null
    var onItemSelectedWithoutFocusListener: OnItemSelectedWithoutFocusListener? = null
    var myOnKeyListener: OnKeyListener? = null
    var millisecondPerInch = 35f
    var selectedPos = 0
    var lastScrollSelectedPos = 0
    var useAnim = false
    var scaleInAnimSource = R.anim.scale_in
    var scaleOutAnimSource = R.anim.scale_out
    var isReverseLayout = false
    var isLTR = true
    var lastNotifyChange = 0;
    var rowCount = 1
        set(value) {
            field = value
            var layoutManager = CenterLayoutManager(context, field, GridLayoutManager.HORIZONTAL, isReverseLayout);
            layoutManager.setMillisecondPerInch(millisecondPerInch)
            super.setLayoutManager(layoutManager)
            super.setOnFocusChangeListener(focusChangeListener)
        }


    var longPress = false;
    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        if (myOnKeyListener != null && myOnKeyListener?.onKey(this, event?.keyCode!!, event)!!) {
            return true
        }

        try {
            if (event?.action == KeyEvent.ACTION_DOWN) {
                if (event?.keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    if (!isLTR) {
                        if (selectedPos - rowCount >= 0) {
                            playSoundEffect(SoundEffectConstants.NAVIGATION_RIGHT)
                            selectedPos = selectedPos - rowCount
                            smoothScrollToPosition(selectedPos)
                            doScroll(selectedPos, true)
                        }
                    } else {
                        if (selectedPos + rowCount < adapter!!.itemCount) {
                            playSoundEffect(SoundEffectConstants.NAVIGATION_LEFT)
                            selectedPos = selectedPos + rowCount
                            smoothScrollToPosition(selectedPos)
                            doScroll(selectedPos, true)
//                    Log.d(MyHorizontalRecyclerView::class.java.name, "dpadLeft")
                        }
                    }

                    return true
                } else if (event?.keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                    if (!isLTR) {
                        if (selectedPos + rowCount < adapter!!.itemCount) {
                            playSoundEffect(SoundEffectConstants.NAVIGATION_LEFT)
                            selectedPos = selectedPos + rowCount
                            smoothScrollToPosition(selectedPos)
                            doScroll(selectedPos, true)
//                    Log.d(MyHorizontalRecyclerView::class.java.name, "dpadLeft")
                        }
                    } else {
                        if (selectedPos - rowCount >= 0) {
                            playSoundEffect(SoundEffectConstants.NAVIGATION_RIGHT)
                            selectedPos = selectedPos - rowCount
                            smoothScrollToPosition(selectedPos)
                            doScroll(selectedPos, true)
                        }
                    }

                    return true
                } else if (event?.keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                    if ((selectedPos + 1) % rowCount != 0 && selectedPos + 1 < adapter!!.itemCount) {
                        playSoundEffect(SoundEffectConstants.NAVIGATION_DOWN)
                        selectedPos = selectedPos + 1
                        doScroll(selectedPos, true)
                        temp = true
                        return true
                    }
                } else if (event?.keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                    if ((selectedPos - 1) % rowCount < rowCount - 1 && selectedPos - 1 >= 0) {
                        playSoundEffect(SoundEffectConstants.NAVIGATION_UP)
                        selectedPos = selectedPos - 1
                        doScroll(selectedPos, true)
                        temp = true
                        return true
                    }
                } else if (event.keyCode == KeyEvent.KEYCODE_ENTER || event.keyCode == KeyEvent.KEYCODE_NUMPAD_ENTER || event.keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
                    val eventDuration = event.eventTime - event.downTime
                    if (eventDuration > ViewConfiguration.getLongPressTimeout()) {
                        if (!longPress) {
                            Log.d(TAG, "onKeyLongClick")
                            try {
                                if (onItemLongClickListener != null) {
                                    onItemLongClickListener?.onItemLongClick(
                                        selectedPos, (adapter as BaseRVAdapter<*, *>).getItem(selectedPos), findViewHolderForLayoutPosition(selectedPos), adapter
                                    )
                                } else if (onItemClickListener != null) {
                                    onItemClickListener?.onItemClick(
                                        selectedPos, (adapter as BaseRVAdapter<*, *>).getItem(selectedPos), findViewHolderForLayoutPosition(selectedPos), adapter
                                    )
                                }
                                playSoundEffect(SoundEffectConstants.CLICK)

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        longPress = true
                        return true;
                    }
                }
            } else if (event?.action == KeyEvent.ACTION_UP) {
                if (event.keyCode == KeyEvent.KEYCODE_ENTER || event.keyCode == KeyEvent.KEYCODE_NUMPAD_ENTER || event.keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
                    return if (longPress) {
                        longPress = false
                        temp = false
                        return true
                    } else {
                        Log.d(TAG, "onKeyClick")
                        try {
                            onItemClickListener?.onItemClick(
                                selectedPos, (adapter as BaseRVAdapter<*, *>).getItem(selectedPos), findViewHolderForLayoutPosition(selectedPos), adapter
                            )
                            playSoundEffect(SoundEffectConstants.CLICK)

                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                        }
                        temp = false
                        return true
                    }
                }
                if (temp) {
                    temp = false
                    return true
                }
            }
        } catch (e: Exception) {

        }


        return super.dispatchKeyEvent(event)
    }


    constructor(context: Context) : super(context) {
        initAnim()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initAnim()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        initAnim()
    }

    private fun initAnim() {
        addOnItemTouchListener(RecyclerTouchListener(context, this, object : RecyclerClickListener {
            override fun onClick(view: View?, position: Int) {
                try {
                    doParentScroll()
                    requestFocus()
                    selectedPos = position
                    smoothScrollToPosition(position)
                    doScroll(position, true)
                    if (onItemClickListener != null) onItemClickListener?.onItemClick(
                        position, (adapter as BaseRVAdapter<*, *>).getItem(position), findViewHolderForLayoutPosition(selectedPos), adapter
                    )
                } catch (e: java.lang.Exception) {

                }
            }

            override fun onLongClick(view: View?, position: Int) {
                try {
                    doParentScroll()
                    requestFocus()
                    selectedPos = position
                    smoothScrollToPosition(position)
                    doScroll(position, true)
                    if (onItemLongClickListener != null) onItemLongClickListener?.onItemLongClick(
                        position, (adapter as BaseRVAdapter<*, *>).getItem(position), findViewHolderForLayoutPosition(selectedPos), adapter
                    )
                    else if (onItemClickListener != null) onItemClickListener?.onItemClick(
                        position, (adapter as BaseRVAdapter<*, *>).getItem(position), findViewHolderForLayoutPosition(selectedPos), adapter
                    )
                } catch (e: java.lang.Exception) {

                }
            }

        }))

    }

    private fun doParentScroll() {
        if (parent is ViewGroup && parent.parent is ScrollView) {
            if ((parent as ViewGroup).indexOfChild(this) < (parent as ViewGroup).indexOfChild((parent as ViewGroup).findFocus())) {
                (parent.parent as ScrollView).executeKeyEvent(
                    KeyEvent(
                        KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_UP
                    )
                )
            } else if ((parent as ViewGroup).indexOfChild(this) > (parent as ViewGroup).indexOfChild(
                    (parent as ViewGroup).findFocus()
                )
            ) {
                (parent.parent as ScrollView).executeKeyEvent(
                    KeyEvent(
                        KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_DOWN
                    )
                )
            }
        }
    }

    override fun setLayoutManager(layout: LayoutManager?) {
        throw RuntimeException("please set rowCount , don't need setLayoutManager...")
    }

    var temp = false;

    override fun setOnFocusChangeListener(l: OnFocusChangeListener?) {
        myfocusChangeListener = l;
    }

    var myfocusChangeListener: OnFocusChangeListener? = null
    var focusChangeListener = OnFocusChangeListener { view: View, focus: Boolean ->
        callItemSelectable(selectedPos, focus, focus)

        myfocusChangeListener?.onFocusChange(view, focus)
    }

    fun selectItem(pos: Int) {
        selectItem(pos, false, false)
    }

    fun selectItem(pos: Int, focus: Boolean, runAnimation: Boolean = true) {
        smoothScrollToPosition(pos)
        selectedPos = pos;
        doScroll(pos, focus, runAnimation)
    }

    fun doScroll(selectedPos: Int, focus: Boolean, runAnimation: Boolean = true) {
//        Log.d(
//            TAG, "doScroll : $selectedPos ${this.selectedPos} ${this.lastScrollSelectedPos}"
//        )
        if (selectedPos != this.lastScrollSelectedPos) {
            temp = true;

//            Log.d(TAG, "lastScrollSelectedPos : ${this.lastScrollSelectedPos}")
            callItemSelectable(lastScrollSelectedPos, false, focus, runAnimation)
            callItemSelectable(selectedPos, true, focus, runAnimation)
            lastNotifyChange = selectedPos;

            onItemSelectedWithoutFocusListener?.onItemSelectedWithoutFocus(
                selectedPos, (adapter as BaseRVAdapter<*, *>).getItem(selectedPos), findViewHolderForAdapterPosition(selectedPos), adapter
            )
            this.lastScrollSelectedPos = selectedPos
        } else {
            callItemSelectable(selectedPos, true, focus, runAnimation)
        }
    }


    fun selectLastPosition() {
//        Log.d(TAG, "isFocused : $isFocused")
        if (isFocused) callItemSelectable(selectedPos, selected = true, focus = true)
    }

    fun deSelect() {
        callItemSelectable(selectedPos, false, false, isFocused)
    }


    private fun callItemSelectable(selectedPos: Int, selected: Boolean, focus: Boolean, runAnimation: Boolean = true, countCall: Int = 3) {
        var holder = findViewHolderForAdapterPosition(selectedPos)
//        Log.d(TAG, "callItemSelectable $selectedPos  $selected  $focus ${holder?.toString()}")

        if (holder == null && countCall != 0) {
            handleViewHolderNullPosition(selectedPos, selected, focus, runAnimation, countCall)
        } else if (holder is ItemSelectable) {
            if (runAnimation && useAnim) runAnimation(selected, holder.itemView)
            if (focus) onItemSelectedListener?.onItemSelected(selectedPos, if (adapter is BaseRVAdapter<*, *>) (adapter as BaseRVAdapter<*, *>).getItem(selectedPos) else null, holder, adapter)

//            Log.d(TAG, "call change selected for $selectedPos")
            (holder as ItemSelectable).changeSelected(
                selected, focus, selectedPos, if (adapter is BaseRVAdapter<*, *>) (adapter as BaseRVAdapter<*, *>).getItem(lastScrollSelectedPos) else null
            )
        }
    }

    private fun runAnimation(selected: Boolean, view: View) {
        var anim = if (selected) AnimationUtils.loadAnimation(context, scaleInAnimSource) else AnimationUtils.loadAnimation(context, scaleOutAnimSource)
        anim!!.fillAfter = true
        view.startAnimation(anim)
    }

    private fun handleViewHolderNullPosition(selectedPos: Int, selected: Boolean, focus: Boolean, runAnimation: Boolean = true, countCall: Int) {
        Handler().postDelayed(Runnable {
//            Log.d(TAG, "handleViewHolderNullPosition selectedPos: $selectedPos  selected: $selected focus: $focus tryCount: $countCall")
            if (!selected || selectedPos == this.selectedPos) {
//                Log.d(TAG, "callItemSelectable return $selectedPos  $selected")
                callItemSelectable(selectedPos, selected, focus, runAnimation, countCall - 1)
            }
        }, 200)
    }

}
