package com.chooloo.www.callmanager.ui.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.util.Utilities;

public class CallActionButton extends androidx.appcompat.widget.AppCompatImageButton {

    Context mContext;

    public CallActionButton(Context context) {
        super(context);
        mContext = context;
    }

    public CallActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public CallActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @SuppressLint("ResourceType")
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            setColorFilter(ContextCompat.getColor(mContext, R.color.grey_400));
        } else {
            setColorFilter(ContextCompat.getColor(mContext, Utilities.getAttributeColor(mContext, R.attr.iconColor)));
        }
    }

    @SuppressLint("ResourceType")
    @Override
    public void setActivated(boolean activated) {
        super.setActivated(activated);
        if (activated) {
            setColorFilter(ContextCompat.getColor(mContext, R.color.soft_black));
            setBackgroundColor(ContextCompat.getColor(mContext, Utilities.getAttributeColor(mContext, R.attr.colorAccent)));
        } else {
            setColorFilter(ContextCompat.getColor(mContext, R.color.white));
            setBackgroundColor(ContextCompat.getColor(mContext, Color.TRANSPARENT));
        }
    }
}
