package com.chooloo.www.koler.util.call

import android.telecom.Call


object CallsManager : Call.Callback() {
    var sCalls: ArrayList<CallItem> = arrayListOf()
    var sListeners: ArrayList<CallsListener> = arrayListOf()

    interface CallsListener {
        fun onCallChanged(callItem: CallItem) {}
        fun onCallStateChanged(callItem: CallItem) {}
        fun onCallDetailsChanged(callItem: CallItem) {}
    }

    val primaryCall: CallItem?
        get() = sCalls.maxByOrNull { it.timeConnected }

    val secondaryCalls: ArrayList<CallItem>
        get() = ArrayList(sCalls.filter { it != primaryCall })

    fun addCall(call: Call) {
        sCalls.add(CallItem(call))
        call.registerCallback(this)
    }

    fun removeCall(call: Call) {
        sCalls.remove(CallItem(call))
        call.unregisterCallback(this)
    }

    fun registerListener(callsListener: CallsListener) {
        sListeners.add(callsListener)
    }

    fun unregisterListener(callsListener: CallsListener) {
        sListeners.remove(callsListener)
    }

    //region call.callback
    override fun onDetailsChanged(call: Call, details: Call.Details) {
        super.onDetailsChanged(call, details)
        sListeners.forEach {
            it.onCallDetailsChanged(CallItem(call))
            it.onCallChanged(CallItem(call))
        }
    }

    override fun onStateChanged(call: Call, state: Int) {
        super.onStateChanged(call, state)
        sListeners.forEach {
            it.onCallStateChanged(CallItem(call))
            it.onCallChanged(CallItem(call))
        }
    }
    //endregion
}

