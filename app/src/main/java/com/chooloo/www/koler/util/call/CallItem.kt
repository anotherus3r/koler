package com.chooloo.www.koler.util.call

import android.content.Context
import android.os.Bundle
import android.provider.ContactsContract
import android.telecom.Call
import com.chooloo.www.koler.data.PhoneLookupAccount
import com.chooloo.www.koler.util.call.CallItem.Companion.CallState.RINGING

class CallItem(
    private val _call: Call
) : Call.Callback() {
    companion object {
        enum class CallState(val state: Int) {
            ACTIVE(Call.STATE_ACTIVE),
            CONNECTING(Call.STATE_CONNECTING),
            DIALING(Call.STATE_DIALING),
            DISCONNECTED(Call.STATE_DISCONNECTED),
            DISCONNECTING(Call.STATE_DISCONNECTING),
            HOLDING(Call.STATE_HOLDING),
            NEW(Call.STATE_NEW),
            PULLING_CALL(Call.STATE_PULLING_CALL),
            RINGING(Call.STATE_RINGING),
            SELECT_PHONE_ACCOUNT(Call.STATE_SELECT_PHONE_ACCOUNT),
            UNKNOWN(-1)
        }
    }

    private var _isMerging: Boolean = false

    init {
        _call.registerCallback(this)
    }

    val state: CallState
        get() = CallState.values().associateBy(CallState::state)
            .getOrDefault(_call.state, CallState.UNKNOWN)

    val isMerging: Boolean
        get() = _isMerging

    val details: Call.Details
        get() = _call.details

    val parentCall: CallItem
        get() = CallItem(_call.parent)

    val timeConnected: Long
        get() = if (_call.details.connectTimeMillis <= 0) {
            System.currentTimeMillis()
        } else {
            _call.details.connectTimeMillis
        }

    val isDisconnected: Boolean
        get() = state in arrayOf(CallState.DISCONNECTING, CallState.DISCONNECTING)

    val isInConference: Boolean
        get() = _call.parent != null

    fun answer() {
        _call.answer(_call.details.videoState)
    }

    fun reject(message: String? = null) {
        if (state == RINGING) {
            _call.reject(message != null, message)
        } else {
            _call.disconnect()
        }
    }

    fun swapConference() {
        _call.swapConference()
    }

    fun leaveConference() {
        _call.splitFromConference()
    }

    fun hold() {
        _call.hold()
        _call.children.forEach { it.hold() }
    }

    fun unhold() {
        _call.unhold()
    }

    fun invokeKeypadChar(char: Char) {
        _call.apply {
            playDtmfTone(char)
            stopDtmfTone()
        }
    }

    fun getAccount(context: Context) = _call.lookupContact(context) ?: PhoneLookupAccount(
        name = null,
        number = _call.getNumber(),
        type = ContactsContract.CommonDataKinds.Phone.TYPE_OTHER
    )

    fun registerListener(callCallback: Call.Callback) {
        _call.registerCallback(callCallback)
    }

    fun unregisterListener(callCallback: Call.Callback) {
        _call.unregisterCallback(callCallback)
    }

    override fun onCallDestroyed(call: Call?) {
        if (_call == call) {
            _call.unregisterCallback(this)
        }
    }

    override fun onConnectionEvent(call: Call?, event: String?, extras: Bundle?) {
        super.onConnectionEvent(call, event, extras)
        if (_call == call) {
            if (event?.contains("MERGE_START") == true) {
                _isMerging = true
            } else if (event?.contains("MERGE_COMPLETE") == true) {
                _isMerging = false
            }
        }
    }

    fun isTheSameCall(callItem: CallItem?) =
        details.accountHandle.id == callItem?.details?.accountHandle?.id
}