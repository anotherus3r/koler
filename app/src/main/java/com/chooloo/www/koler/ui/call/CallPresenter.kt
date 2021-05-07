package com.chooloo.www.koler.ui.call

import android.net.Uri
import android.os.Handler
import com.chooloo.www.koler.App
import com.chooloo.www.koler.R
import com.chooloo.www.koler.ui.base.BasePresenter
import com.chooloo.www.koler.util.call.CallItem
import com.chooloo.www.koler.util.call.CallItem.Companion.CallState.*
import com.chooloo.www.koler.util.call.CallsManager

class CallPresenter<V : CallContract.View> : BasePresenter<V>(), CallContract.Presenter<V> {
    override fun attach(mvpView: V) {
        super.attach(mvpView)
        CallsManager.registerListener(object : CallsManager.CallsListener {
            override fun onCallChanged(callItem: CallItem) {
                super.onCallChanged(callItem)
                if (callItem != CallsManager.primaryCall) {
                    displayPrimaryCall(callItem)
                } else {
                    mvpView.updateCallView(callItem)
                }
            }
        })
    }

    override fun onAnswerClick() {
        CallsManager.primaryCall?.answer()
    }

    override fun onRejectClick() {
        CallsManager.primaryCall?.reject()
    }

    override fun displayCurrentCalls() {
        CallsManager.primaryCall?.let { displayPrimaryCall(it) }
        CallsManager.secondaryCalls.forEach { mvpView?.updateCallView(it) }
    }

    private fun displayPrimaryCall(callItem: CallItem) {
        mvpView?.getCallAccount(callItem)?.apply {
            mvpView?.callerNameText = name ?: number ?: "Unknown"
            photoUri?.let { mvpView?.callerImageURI = Uri.parse(it) }
        }

        mvpView?.stateText = App.resources?.getString(
            when (callItem.state) {
                ACTIVE -> R.string.call_status_active
                DISCONNECTED -> R.string.call_status_disconnected
                RINGING -> R.string.call_status_incoming
                DIALING -> R.string.call_status_dialing
                CONNECTING -> R.string.call_status_dialing
                HOLDING -> R.string.call_status_holding
                else -> R.string.call_status_active
            }
        )

        when (callItem.state) {
            CONNECTING -> mvpView?.transitionToActiveUI()
            HOLDING -> mvpView?.apply {
                getColor(R.color.red_foreground).let { mvpView?.stateTextColor = it }
                blinkStateText()
            }
            ACTIVE -> mvpView?.apply {
                getColor(R.color.green_foreground).let { mvpView?.stateTextColor = it }
                blinkStateText()
                startStopwatch()
                transitionToActiveUI()
            }
            DISCONNECTED -> mvpView?.apply {
                getColor(R.color.red_foreground).let { stateTextColor = it }
                blinkStateText()
                stopStopwatch()
                Handler().postDelayed({ finish() }, 2000)
            }
        }
    }
}