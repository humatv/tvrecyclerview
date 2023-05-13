package ir.huma.tvrecyclerview.lib

import android.content.Context
import android.content.res.TypedArray
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.view.View.OnFocusChangeListener
import android.view.animation.AnimationUtils
import android.widget.ScrollView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import ir.huma.tvrecyclerview.lib.interfaces.*
import ir.huma.tvrecyclerview.lib.interfaces.GetItemAdaptable

class TvRecyclerView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    RecyclerView(context, attrs, defStyleAttr) {
    private val TAG = "TvRecyclerView"

    var onItemClickListener: OnItemClickListener? = null
    var onItemLongClickListener: OnItemLongClickListener? = null
    var onItemSelectedListener: OnItemSelectedListener? = null
    var onItemSelectedWithoutFocusListener: OnItemSelectedWithoutFocusListener? = null
    var onRvfocusChangeListener: OnFocusChangeListener? = null
    var myOnKeyListener: OnKeyListener? = null
    var scrollSpeed = 35f //(bigger = slower)
    var useAnim = false
        set(value) {
            field = value
            isChildrenDrawingOrderEnabled = true
        }
    var selectAnimation = R.anim.scale_in
    var unselectAnimation = R.anim.scale_out
    var isReverseLayout = false
    var isLTR = true
    var orientation: Int = HORIZONTAL
    var rowCount = 1


    var selectedPos = 0
        private set
    private var lastScrollSelectedPos = 0
    private var longPress = false

    @Volatile
    private var temp = false

    private var increaseKeyCode = KeyEvent.KEYCODE_DPAD_RIGHT
    private var decreaseKeyCode = KeyEvent.KEYCODE_DPAD_LEFT
    private var increaseRowKeyCode = KeyEvent.KEYCODE_DPAD_DOWN
    private var decreaseRowKeyCode = KeyEvent.KEYCODE_DPAD_UP

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(
        context,
        attrs,
        androidx.recyclerview.R.attr.recyclerViewStyle
    )


    init {

        val typedArray: TypedArray =
            context.obtainStyledAttributes(attrs, R.styleable.TvRecyclerView, defStyleAttr, 0)

        scrollSpeed = typedArray.getFloat(R.styleable.TvRecyclerView_scrollSpeed, 35f)
        useAnim = typedArray.getBoolean(R.styleable.TvRecyclerView_useAnim, false)
        isReverseLayout = typedArray.getBoolean(R.styleable.TvRecyclerView_reverseLayout, false)
        isLTR = typedArray.getBoolean(R.styleable.TvRecyclerView_isLTR, true)
        rowCount = typedArray.getInt(R.styleable.TvRecyclerView_rowCount, 1)

        selectAnimation = typedArray.getResourceId(
            R.styleable.TvRecyclerView_selectAnimation,
            R.anim.scale_in
        )
        unselectAnimation = typedArray.getResourceId(
            R.styleable.TvRecyclerView_unselectAnimation,
            R.anim.scale_out
        )
        orientation = typedArray.getInt(
            R.styleable.TvRecyclerView_layoutOrientation,
            HORIZONTAL
        )
        typedArray.recycle()
        initAnim()
    }


    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        Log.d(TAG, "dispatchKeyEvent: ${event.toString()}")
        if (myOnKeyListener != null && myOnKeyListener?.onKey(this, event?.keyCode!!, event)!!) {
            return true
        }

        try {
            if (event?.action == KeyEvent.ACTION_DOWN) {
                if (event.keyCode == increaseKeyCode) {
//                    if (orientation == VERTICAL && super.dispatchKeyEvent(event)) {
//                        return true
//                    }
                    if (!isLTR && orientation == HORIZONTAL) {
                        if (selectedPos - rowCount >= 0) {
                            playSoundEffect(SoundEffectConstants.NAVIGATION_RIGHT)
                            selectedPos -= rowCount
                            smoothScrollToPosition(selectedPos)
                            doScroll(selectedPos, true)
                            temp = true
                            return true
                        }
                    } else {
                        if (selectedPos + rowCount < adapter!!.itemCount) {
                            playSoundEffect(SoundEffectConstants.NAVIGATION_LEFT)
                            selectedPos += rowCount
                            smoothScrollToPosition(selectedPos)
                            doScroll(selectedPos, true)
                            temp = true
                            return true
                        }
                    }

                } else if (event.keyCode == decreaseKeyCode) {
//                    if (orientation == VERTICAL && super.dispatchKeyEvent(event)) {
//                        return true
//                    }
                    if (!isLTR && orientation == HORIZONTAL) {
                        if (selectedPos + rowCount < adapter!!.itemCount) {
                            playSoundEffect(SoundEffectConstants.NAVIGATION_LEFT)
                            selectedPos += rowCount
                            smoothScrollToPosition(selectedPos)
                            doScroll(selectedPos, true)
                            temp = true
                            return true
                        }
                    } else {
                        if (selectedPos - rowCount >= 0) {
                            playSoundEffect(SoundEffectConstants.NAVIGATION_RIGHT)
                            selectedPos -= rowCount
                            smoothScrollToPosition(selectedPos)
                            doScroll(selectedPos, true)
                            temp = true
                            return true
                        }
                    }
                } else if (event.keyCode == increaseRowKeyCode) {
                    if ((selectedPos + 1) % rowCount != 0 && selectedPos + 1 < adapter!!.itemCount) {
                        playSoundEffect(SoundEffectConstants.NAVIGATION_DOWN)
                        selectedPos += 1
                        doScroll(selectedPos, true)
                        temp = true
                        return true
                    }
                } else if (event.keyCode == decreaseRowKeyCode) {
                    if ((selectedPos - 1) % rowCount < rowCount - 1 && selectedPos - 1 >= 0) {
                        playSoundEffect(SoundEffectConstants.NAVIGATION_UP)
                        selectedPos -= 1
                        doScroll(selectedPos, true)
                        temp = true
                        return true
                    }
                } else if (event.keyCode == KeyEvent.KEYCODE_ENTER || event.keyCode == KeyEvent.KEYCODE_NUMPAD_ENTER || event.keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
                    val eventDuration = event.eventTime - event.downTime
                    if (eventDuration > ViewConfiguration.getLongPressTimeout()) {
                        if (!longPress) {
                            try {
                                if (onItemLongClickListener != null) {
                                    onItemLongClickListener?.onItemLongClick(
                                        selectedPos,
                                        (adapter as GetItemAdaptable<*>).getItem(selectedPos),
                                        findViewHolderForLayoutPosition(selectedPos),
                                        adapter
                                    )
                                    longPress = true
                                    return true
                                } else if (onItemClickListener != null) {
                                    onItemClickListener?.onItemClick(
                                        selectedPos,
                                        (adapter as GetItemAdaptable<*>).getItem(selectedPos),
                                        findViewHolderForLayoutPosition(selectedPos),
                                        adapter
                                    )
                                    return true
                                }
                                playSoundEffect(SoundEffectConstants.CLICK)

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            } else if (event?.action == KeyEvent.ACTION_UP) {
                if (event.keyCode == KeyEvent.KEYCODE_ENTER || event.keyCode == KeyEvent.KEYCODE_NUMPAD_ENTER || event.keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
                    if (longPress && onItemLongClickListener != null) {
                        longPress = false
                        temp = false
                        return true
                    } else if (onItemClickListener != null) {
                        try {
                            onItemClickListener?.onItemClick(
                                selectedPos,
                                (adapter as GetItemAdaptable<*>).getItem(selectedPos),
                                findViewHolderForLayoutPosition(selectedPos),
                                adapter
                            )
                            playSoundEffect(SoundEffectConstants.CLICK)

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        temp = false
                        return true
                    }
                } else if (temp) {
                    temp = false
                    return true
                }
            }
        } catch (e: Exception) {

        }

        return super.dispatchKeyEvent(event)
    }


    private fun initAnim() {
        addOnItemTouchListener(
            RecyclerTouchListener(
                context,
                this,
                object : RecyclerClickListener {
                    override fun onClick(view: View?, position: Int) {
                        try {
                            doParentScroll()
                            requestFocus()
                            selectedPos = position
                            smoothScrollToPosition(position)
                            doScroll(position, true)
                            if (onItemClickListener != null) onItemClickListener?.onItemClick(
                                position,
                                (adapter as GetItemAdaptable<*>).getItem(position),
                                findViewHolderForLayoutPosition(selectedPos),
                                adapter
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
                                position,
                                (adapter as GetItemAdaptable<*>).getItem(position),
                                findViewHolderForLayoutPosition(selectedPos),
                                adapter
                            )
                            else if (onItemClickListener != null) onItemClickListener?.onItemClick(
                                position,
                                (adapter as GetItemAdaptable<*>).getItem(position),
                                findViewHolderForLayoutPosition(selectedPos),
                                adapter
                            )
                        } catch (e: java.lang.Exception) {

                        }
                    }

                })
        )

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

    //    @Deprecated(
//        "you should call setLayoutManager with param(orientation,rowCount)",
//        ReplaceWith("throw RuntimeException(\"please set rowCount , don't need setLayoutManager(LayoutManager)\")")
//    )
    override fun setLayoutManager(layout: LayoutManager?) {
//        throw RuntimeException("")
    }

    private fun setLayoutManager() {
        val layoutManager = CenterLayoutManager(context, rowCount, orientation, isReverseLayout)
        layoutManager.setMillisecondPerInch(scrollSpeed)
        if (orientation == VERTICAL) {
            if(isReverseLayout) {
                increaseKeyCode = KeyEvent.KEYCODE_DPAD_DOWN
                decreaseKeyCode = KeyEvent.KEYCODE_DPAD_UP
            } else{
                increaseKeyCode = KeyEvent.KEYCODE_DPAD_UP
                decreaseKeyCode = KeyEvent.KEYCODE_DPAD_DOWN
            }
            if (isLTR) {
                increaseRowKeyCode = KeyEvent.KEYCODE_DPAD_RIGHT
                decreaseRowKeyCode = KeyEvent.KEYCODE_DPAD_LEFT
            } else {
                increaseRowKeyCode = KeyEvent.KEYCODE_DPAD_LEFT
                decreaseRowKeyCode = KeyEvent.KEYCODE_DPAD_RIGHT
            }
        }

        super.setLayoutManager(layoutManager)
        super.setOnFocusChangeListener(focusChangeListener)

    }

    override fun setAdapter(adapter: Adapter<*>?) {
        setLayoutManager()
        super.setAdapter(adapter)
    }


    override fun setOnFocusChangeListener(l: OnFocusChangeListener?) {
        this.onRvfocusChangeListener = l
    }


    private var focusChangeListener = OnFocusChangeListener { view: View, focus: Boolean ->
        Log.d(TAG, "focusChangeListener: ${orientation}  ${focus}")
        callItemSelectable(selectedPos, focus, focus)
        onRvfocusChangeListener?.onFocusChange(view, focus)
    }

    fun selectItem(pos: Int) {
        selectItem(pos, focus = false, runAnimation = false)
    }

    fun selectItem(pos: Int, focus: Boolean, runAnimation: Boolean = true) {
        smoothScrollToPosition(pos)
        selectedPos = pos
        doScroll(pos, focus, runAnimation)
    }

    fun doScroll(selectedPos: Int, focus: Boolean, runAnimation: Boolean = true) {

        if (selectedPos != this.lastScrollSelectedPos) {
            temp = true

            callItemSelectable(lastScrollSelectedPos, false, focus, runAnimation)
            callItemSelectable(selectedPos, true, focus, runAnimation)

            onItemSelectedWithoutFocusListener?.onItemSelectedWithoutFocus(
                selectedPos,
                (adapter as GetItemAdaptable<*>).getItem(selectedPos),
                findViewHolderForAdapterPosition(selectedPos),
                adapter
            )
            this.lastScrollSelectedPos = selectedPos
        } else {
            callItemSelectable(selectedPos, true, focus, runAnimation)
        }
    }


    fun selectLastPosition() {
        if (isFocused) callItemSelectable(selectedPos, selected = true, focus = true)
    }

    fun deSelect() {
        callItemSelectable(
            selectedPos,
            selected = false,
            focus = false,
            runAnimation = isFocused
        )
    }


    private fun callItemSelectable(
        selectedPos: Int,
        selected: Boolean,
        focus: Boolean,
        runAnimation: Boolean = true,
        countCall: Int = 4
    ) {
        val holder = findViewHolderForAdapterPosition(selectedPos)

        if (holder == null && countCall != 0) {
            handleViewHolderNullPosition(selectedPos, selected, focus, runAnimation, countCall)
        } else if (holder != null) {
            if (runAnimation && useAnim) runAnimation(selected, holder.itemView)
            if (focus) onItemSelectedListener?.onItemSelected(
                selectedPos,
                if (adapter is GetItemAdaptable<*>) (adapter as GetItemAdaptable<*>).getItem(
                    selectedPos
                ) else null,
                holder,
                adapter
            )
            if (holder is ItemSelectable) {
                (holder as ItemSelectable).changeSelected(
                    selected,
                    focus,
                    selectedPos,
                    if (adapter is GetItemAdaptable<*>) (adapter as GetItemAdaptable<*>).getItem(
                        selectedPos
                    ) else null
                )
            }
        }
    }

    private fun runAnimation(selected: Boolean, view: View) {
        ViewCompat.setElevation(view, if (selected) 1f else 0f);
        val anim = if (selected) AnimationUtils.loadAnimation(
            context,
            selectAnimation
        ) else AnimationUtils.loadAnimation(context, unselectAnimation)
        anim!!.fillAfter = true
        view.startAnimation(anim)
    }

    private fun handleViewHolderNullPosition(
        selectedPos: Int,
        selected: Boolean,
        focus: Boolean,
        runAnimation: Boolean = true,
        countCall: Int
    ) {
        Handler(Looper.getMainLooper()).postDelayed({
            if (!selected || selectedPos == this.selectedPos) {
                callItemSelectable(selectedPos, selected, focus, runAnimation, countCall - 1)
            }
        }, 200)
    }

}
