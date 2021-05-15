package com.chooloo.www.koler.data

import com.chooloo.www.koler.util.call.CallItem

data class CallsBundle(
    val callItems: ArrayList<CallItem>
) {
    val listBundle = ListBundle(callItems)
}