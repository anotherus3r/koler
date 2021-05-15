package com.chooloo.www.koler.ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.LinearLayout.VERTICAL
import android.widget.ScrollView
import com.chooloo.www.koler.util.call.CallItem

class CallsView : ScrollView {
    private val _linearLayout: LinearLayout
    private val _calls = HashMap<CallItem, CallListItem>()

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleRes: Int = 0
    ) : super(context, attrs, defStyleRes) {
        _linearLayout = LinearLayout(context, attrs, defStyleRes).apply {
            layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            orientation = VERTICAL
        }
    }

    private fun getCallView(callItem: CallItem) = _calls.getOrDefault(callItem, null)

    fun addCall(callItem: CallItem) {
        val callItemView = CallListItem.fromCallItem(callItem, context)
        _calls[callItem] = callItemView
        _linearLayout.addView(callItemView)
        callItemView.showCallState(callItem.state)
    }

    fun removeCall(callItem: CallItem) {
        getCallView(callItem)?.let {
            _linearLayout.removeView(it)
            _calls.remove(callItem)
        }
    }

    fun updateCall(callItem: CallItem) {
        val callView = getCallView(callItem)
        when {
            callView == null -> addCall(callItem)
            callItem.isDisconnected -> removeCall(callItem)
            else -> callView.showCallState(callItem.state)
        }
    }

    fun updateCalls(callItems: ArrayList<CallItem>) {
        callItems.forEach { updateCall(it) }
    }

}