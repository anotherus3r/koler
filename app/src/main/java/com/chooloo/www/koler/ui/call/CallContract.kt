package com.chooloo.www.koler.ui.call

import android.net.Uri
import com.chooloo.www.koler.data.PhoneLookupAccount
import com.chooloo.www.koler.ui.base.BaseContract
import com.chooloo.www.koler.util.call.CallItem

interface CallContract : BaseContract {
    interface View : BaseContract.View {
        var stateText: String?
        var stateTextColor: Int
        var callerImageURI: Uri?
        var callerNameText: String?
        var secondaryCallsVisibility: Boolean

        fun stopStopwatch()
        fun blinkStateText()
        fun startStopwatch()
        fun transitionToActiveUI()
        fun updateSecondaryCall(callItem: CallItem)
        fun getCallAccount(callItem: CallItem): PhoneLookupAccount
    }

    interface Presenter<V : View> : BaseContract.Presenter<V> {
        fun onAnswerClick()
        fun onRejectClick()
        fun onDisplayCalls()
    }
}