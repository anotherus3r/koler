package com.chooloo.www.koler.ui.call

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import com.chooloo.www.koler.R
import com.chooloo.www.koler.databinding.CallBinding
import com.chooloo.www.koler.ui.base.BaseActivity
import com.chooloo.www.koler.ui.callactions.CallActionsFragment
import com.chooloo.www.koler.util.*
import com.chooloo.www.koler.util.call.CallItem

@SuppressLint("ClickableViewAccessibility")
class CallActivity : BaseActivity(), CallContract.View {
    private val _animationManager by lazy { AnimationManager(this) }
    private val _presenter by lazy { CallPresenter<CallContract.View>() }
    private val _proximitySensor by lazy { ProximitySensor(this) }
    private val _binding by lazy { CallBinding.inflate(layoutInflater) }

    override var stateText: String?
        get() = _binding.callStateText.text.toString()
        set(value) {
            _binding.callStateText.text = value
        }

    override var stateTextColor: Int
        get() = _binding.callStateText.currentTextColor
        set(value) {
            _binding.callStateText.setTextColor(value)
        }

    override var callerNameText: String?
        get() = _binding.callNameText.text.toString()
        set(value) {
            _binding.callNameText.text = value
        }

    override var callerImageURI: Uri?
        get() = null
        set(value) {
            _binding.callImage.setImageURI(value)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(_binding.root)
    }

    override fun onSetup() {
        _presenter.attach(this)
        _proximitySensor.acquire()

        _binding.apply {
            callAnswerButton.apply {
                setOnTouchListener(object : AllPurposeTouchListener(this@CallActivity) {
                    override fun onSingleTapConfirmed(v: View?): Boolean {
                        _presenter.onAnswerClick()
                        return true
                    }

                    override fun onSwipeRight() {
                        _presenter.onAnswerClick()
                    }
                })
            }

            callRejectButton.apply {
                setOnTouchListener(object : AllPurposeTouchListener(this@CallActivity) {
                    override fun onSingleTapConfirmed(v: View?): Boolean {
                        _presenter.onRejectClick()
                        return true
                    }

                    override fun onSwipeLeft() {
                        _presenter.onRejectClick()
                    }
                })
            }
        }

        _presenter.displayCurrentCalls()
        setShowWhenLocked()
        disableKeyboard()
    }

    override fun onDestroy() {
        super.onDestroy()
        _presenter.detach()
        _proximitySensor.release()
    }

    override fun transitionToActiveUI() {
        if (_binding.root.currentState == R.id.incoming_call) {
            supportFragmentManager
                .beginTransaction()
                .add(_binding.callActionsContainer.id, CallActionsFragment.newInstance())
                .commitNow()
            _animationManager.showView(_binding.callActionsContainer, true)
            _binding.root.transitionToEnd()
        }
    }

    override fun blinkStateText() {
        _animationManager.blinkView(_binding.callStateText, 2500)
    }

    override fun startStopwatch() {
        _binding.callChronometer.apply {
            base = SystemClock.elapsedRealtime()
            start()
        }
    }

    override fun stopStopwatch() {
        _binding.callChronometer.stop()
    }

    override fun updateCallView(callItem: CallItem) {
        _binding.callCallsView.updateCall(callItem)
    }

    override fun getCallAccount(callItem: CallItem) = callItem.getAccount(this)
}
