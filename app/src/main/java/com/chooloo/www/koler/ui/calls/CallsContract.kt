package com.chooloo.www.koler.ui.calls

import com.chooloo.www.koler.ui.base.BaseContract
import com.chooloo.www.koler.util.call.CallItem

interface CallsContract : BaseContract {
    interface View : BaseContract.View {
        fun updateCallItem(callItem: CallItem)
        fun updateCallItems(callItems: ArrayList<CallItem>)
    }

    interface Presenter<V : View> : BaseContract.Presenter<V> {

    }
}