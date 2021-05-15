package com.chooloo.www.koler.adapter

import android.net.Uri
import com.chooloo.www.koler.R
import com.chooloo.www.koler.ui.widgets.ListItem
import com.chooloo.www.koler.util.call.CallItem

class CallsAdapter : ListAdapter<CallItem>() {
    fun updateCallItem(callItem: CallItem) {
        data.items.forEachIndexed { index, item ->
            if (item.isTheSameCall(callItem)) {
                if (callItem.isDisconnected) {
                    removeItem(item)
                    return
                } else {
                    updateItem(callItem, index)
                    return
                }
            }
        }
        addItem(callItem)
    }

    override fun onBindListItem(listItem: ListItem, item: CallItem) {
        listItem.apply {
            leftButtonVisibility = true
            rightButtonVisibility = true

            setOnLeftButtonClickListener { item.reject() }
            setOnRightButtonClickListener { item.answer() }
            setLeftButtonTintColor(R.color.green_foreground)
            setLeftButtonDrawable(R.drawable.ic_call_black_24dp)
            setRightButtonDrawable(R.drawable.ic_call_end_black_24dp)
            setImageUri(Uri.parse(item.getAccount(listItem.context).photoUri))
        }
    }
}