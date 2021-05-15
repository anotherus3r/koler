package com.chooloo.www.koler.ui.widgets

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.core.content.ContextCompat
import com.chooloo.www.koler.R
import com.chooloo.www.koler.util.call.CallItem
import com.chooloo.www.koler.util.call.CallItem.Companion.CallState

class CallListItem : ListItem {
    private val redForeground by lazy { ContextCompat.getColor(context, R.color.red_foreground) }
    private val redBackground by lazy { ContextCompat.getColor(context, R.color.red_background) }
    private val greenBackground by lazy {
        ContextCompat.getColor(context, R.color.green_background)
    }
    private val greenForeground by lazy {
        ContextCompat.getColor(context, R.color.green_foreground)
    }

    private val dimenImageSize by lazy { resources.getDimensionPixelSize(R.dimen.image_size_big) }
    private val dimenSpacingSmall by lazy { resources.getDimensionPixelSize(R.dimen.default_spacing_small) }

    private val _answerButton: IconButton
    private val _hangupButton: IconButton

    companion object {
        fun fromCallItem(callItem: CallItem, context: Context) = CallListItem(context).apply {
            val account = callItem.getAccount(context)
            titleText = account.name ?: account.number ?: "Unknown"
            showCallState(callItem.state)
        }
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleRes: Int = 0
    ) : super(context, attrs, defStyleRes) {
        imageSize = dimenImageSize
        imageTextSize = resources.getDimension(R.dimen.headline_3)

        _answerButton = IconButton(context, attrs, defStyleRes).apply {
            id = generateViewId()
            imageTintList = ColorStateList.valueOf(greenForeground)
            backgroundTintList = ColorStateList.valueOf(greenBackground)
            layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                setMargins(
                    dimenSpacingSmall,
                    dimenSpacingSmall,
                    dimenSpacingSmall,
                    dimenSpacingSmall
                )
            }

            setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_call_black_24dp))
        }

        _hangupButton = IconButton(context, attrs, defStyleRes).apply {
            id = generateViewId()
            imageTintList = ColorStateList.valueOf(redForeground)
            backgroundTintList = ColorStateList.valueOf(redBackground)
            layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                setMargins(
                    dimenSpacingSmall,
                    dimenSpacingSmall,
                    dimenSpacingSmall,
                    dimenSpacingSmall
                )
            }

            setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_call_end_black_24dp))
        }

        addView(_answerButton)
        addView(_hangupButton)
    }

    private var answerButtonVisibility: Boolean
        get() = _answerButton.visibility == VISIBLE
        set(value) {
            _answerButton.visibility = if (value) VISIBLE else GONE
        }

    private var hangupButtonVisibility: Boolean
        get() = _hangupButton.visibility == VISIBLE
        set(value) {
            _hangupButton.visibility = if (value) VISIBLE else GONE
        }

    fun showCallState(state: CallState) {
        captionText = resources.getString(
            when (state) {
                CallState.ACTIVE -> R.string.call_status_active
                CallState.DISCONNECTED -> R.string.call_status_disconnected
                CallState.RINGING -> R.string.call_status_incoming
                CallState.DIALING -> R.string.call_status_dialing
                CallState.CONNECTING -> R.string.call_status_dialing
                CallState.HOLDING -> R.string.call_status_holding
                else -> R.string.call_status_active
            }
        )

        when (state) {
            CallState.DIALING -> answerButtonVisibility = false
            CallState.CONNECTING -> answerButtonVisibility = false
            CallState.HOLDING -> {
                setCaptionTextColor(redForeground)
                blinkCaption()
            }
            CallState.ACTIVE -> {
                answerButtonVisibility = false
                setCaptionTextColor(greenForeground)
                blinkCaption()
            }
            CallState.DISCONNECTED -> {
                answerButtonVisibility = false
                hangupButtonVisibility = false
                setCaptionTextColor(redForeground)
                blinkCaption()
            }
            else -> {
            }
        }
    }
}