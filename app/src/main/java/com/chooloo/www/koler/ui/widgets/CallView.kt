package com.chooloo.www.koler.ui.widgets

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.ConstraintSet.*
import androidx.core.content.ContextCompat
import com.chooloo.www.koler.R
import com.chooloo.www.koler.util.AnimationManager
import com.chooloo.www.koler.util.call.CallItem
import com.chooloo.www.koler.util.call.CallItem.Companion.CallState
import com.github.abdularis.civ.AvatarImageView
import com.github.abdularis.civ.AvatarImageView.Companion.SHOW_IMAGE
import com.github.abdularis.civ.AvatarImageView.Companion.SHOW_INITIAL

class CallView : ConstraintLayout {
    private val _animationManager by lazy { AnimationManager(context) }

    private val redForeground by lazy { ContextCompat.getColor(context, R.color.red_foreground) }
    private val redBackground by lazy { ContextCompat.getColor(context, R.color.red_background) }
    private val greenBackground by lazy {
        ContextCompat.getColor(context, R.color.green_background)
    }
    private val greenForeground by lazy {
        ContextCompat.getColor(context, R.color.green_foreground)
    }

    private val spacing by lazy { resources.getDimensionPixelSize(R.dimen.default_spacing) }
    private val imageSize by lazy { resources.getDimensionPixelSize(R.dimen.image_size_big) }
    private val spacingSmall by lazy { resources.getDimensionPixelSize(R.dimen.default_spacing_small) }

    private val _image: AvatarImageView
    private val _title: AppCompatTextView
    private val _answerButton: IconButton
    private val _hangupButton: IconButton
    private val _caption: AppCompatTextView

    companion object {
        fun fromCallItem(callItem: CallItem, context: Context) = CallView(context).apply {
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
        _title = AppCompatTextView(context, attrs, defStyleRes).apply {
            id = View.generateViewId()
            textAlignment = TEXT_ALIGNMENT_VIEW_START
            layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                setMargins(spacingSmall, spacingSmall, spacingSmall, spacingSmall)
            }

            setTextAppearance(R.style.Koler_Text_Headline3)
        }

        _caption = AppCompatTextView(context, attrs, defStyleRes).apply {
            id = View.generateViewId()
            textAlignment = TEXT_ALIGNMENT_VIEW_START
            layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                setMargins(spacingSmall, spacingSmall, spacingSmall, spacingSmall)
            }

            setTextAppearance(R.style.Koler_Text_Caption)
        }

        _image = AvatarImageView(context, attrs).apply {
            id = generateViewId()
            state = AvatarImageView.SHOW_INITIAL
            layoutParams = LayoutParams(imageSize, imageSize)
            textSize = resources.getDimension(R.dimen.headline_3)
            textColor = ContextCompat.getColor(context, R.color.color_image_placeholder_foreground)
            avatarBackgroundColor =
                ContextCompat.getColor(context, R.color.color_image_placeholder_background)
        }

        _answerButton = IconButton(context, attrs, defStyleRes).apply {
            id = generateViewId()
            imageTintList = ColorStateList.valueOf(greenForeground)
            backgroundTintList = ColorStateList.valueOf(greenBackground)
            layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                setMargins(spacingSmall, spacingSmall, spacingSmall, spacingSmall)
            }

            setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_call_black_24dp))
        }

        _hangupButton = IconButton(context, attrs, defStyleRes).apply {
            id = generateViewId()
            imageTintList = ColorStateList.valueOf(redForeground)
            backgroundTintList = ColorStateList.valueOf(redBackground)
            layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                setMargins(spacingSmall, spacingSmall, spacingSmall, spacingSmall)
            }

            setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_call_end_black_24dp))
        }

        addView(_image)
        addView(_title)
        addView(_caption)
        addView(_answerButton)
        addView(_hangupButton)

        ConstraintSet().apply {
            clone(this@CallView)

            _title.id.also {
                connect(it, TOP, PARENT_ID, TOP)
                connect(it, START, PARENT_ID, START)
                connect(it, BOTTOM, _caption.id, TOP)
            }

            _caption.id.also {
                connect(it, TOP, _title.id, BOTTOM)
                connect(it, START, PARENT_ID, START)
                connect(it, BOTTOM, PARENT_ID, BOTTOM)
            }

            _image.id.also {
                connect(it, END, PARENT_ID, END)
                connect(it, TOP, PARENT_ID, TOP)
                connect(it, BOTTOM, PARENT_ID, BOTTOM)
            }

            _answerButton.id.also {
                connect(it, END, _hangupButton.id, START)
                connect(it, TOP, PARENT_ID, TOP)
                connect(it, BOTTOM, PARENT_ID, BOTTOM)
            }

            _hangupButton.id.also {
                connect(it, TOP, PARENT_ID, TOP)
                connect(it, BOTTOM, PARENT_ID, BOTTOM)
                connect(it, END, START, _image.id)
            }

            applyTo(this@CallView)
        }

        context.obtainStyledAttributes(attrs, R.styleable.Koler_CallItem, 0, 0).also {
            titleText = it.getString(R.styleable.Koler_CallItem_title)
            captionText = it.getString(R.styleable.Koler_CallItem_caption)
            imageDrawable = it.getDrawable(R.styleable.Koler_CallItem_src)
        }
    }

    var titleText: String?
        get() = _title.text.toString()
        set(value) {
            _title.text = value
        }

    var captionText: String?
        get() = _caption.text.toString()
        set(value) {
            _caption.text = value
        }

    var imageDrawable: Drawable?
        get() = _image.drawable
        set(value) {
            _image.apply {
                setImageDrawable(value)
                state = SHOW_IMAGE
            }
        }

    var answerButtonVisibility: Boolean
        get() = _answerButton.visibility == VISIBLE
        set(value) {
            _answerButton.visibility = if (value) VISIBLE else GONE
        }

    var hangupButtonVisibility: Boolean
        get() = _hangupButton.visibility == VISIBLE
        set(value) {
            _hangupButton.visibility = if (value) VISIBLE else GONE
        }

    fun setImageInitials(text: String?) {
        _image.text = text
        text?.let { _image.state = SHOW_INITIAL }
    }

    fun setImageUri(imageUri: Uri?) {
        _image.setImageURI(imageUri)
        _image.state = if (imageUri != null) SHOW_IMAGE else SHOW_INITIAL
    }

    fun setImageBackgroundColor(@ColorInt color: Int) {
        _image.setBackgroundColor(color)
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
                _caption.setTextColor(redForeground)
                blinkCaption()
            }
            CallState.ACTIVE -> {
                answerButtonVisibility = false
                _caption.setTextColor(greenForeground)
                blinkCaption()
            }
            CallState.DISCONNECTED -> {
                answerButtonVisibility = false
                hangupButtonVisibility = false
                _caption.setTextColor(redForeground)
                blinkCaption()
            }
            else -> {
            }
        }
    }

    private fun blinkCaption() {
        _animationManager.blinkView(_caption, 2500)
    }
}