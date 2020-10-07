package com.chooloo.www.callmanager.service;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.chooloo.www.callmanager.util.BluetoothUtils;

import java.util.Objects;

import timber.log.Timber;

import static android.bluetooth.BluetoothProfile.EXTRA_STATE;
import static com.chooloo.www.callmanager.util.BluetoothUtils.ACTION_CONNECTION_STATE_CHANGED;
import static com.chooloo.www.callmanager.util.BluetoothUtils.ACTION_PLAYING_STATE_CHANGED;
import static com.chooloo.www.callmanager.util.BluetoothUtils.STATE_CONNECTED;
import static com.chooloo.www.callmanager.util.BluetoothUtils.STATE_DISCONNECTED;
import static com.chooloo.www.callmanager.util.BluetoothUtils.STATE_NOT_PLAYING;
import static com.chooloo.www.callmanager.util.BluetoothUtils.STATE_PLAYING;

public class BluetoothBroadcastReceiver extends BroadcastReceiver {

    Context mContext;
    OnBluetoothChangeListener mOnBluetoothChangeListener;

    public BluetoothBroadcastReceiver(Context context) {
        mContext = context;
        mOnBluetoothChangeListener = new OnBluetoothChangeListener() {
            @Override
            public void onStateConnected() {
            }

            @Override
            public void onStateDisconnected() {
            }

            @Override
            public void onPlaying() {
            }

            @Override
            public void onNotPlaying() {
            }
        };
    }

    public BluetoothBroadcastReceiver(Context context, OnBluetoothChangeListener onBluetoothChangeListener) {
        mContext = context;
        setOnBluetoothChangeListener(onBluetoothChangeListener);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction(); // get the action type
        switch (Objects.requireNonNull(action)) {
            case ACTION_CONNECTION_STATE_CHANGED: {
                BluetoothDevice device = BluetoothUtils.getDevice(intent);
                Timber.d("Bluetooth device : %s", device.getName());
                int state = intent.getIntExtra(EXTRA_STATE, STATE_DISCONNECTED);
                switch (state) {
                    case STATE_CONNECTED: {
                        Timber.d("Bluetooth connected");
                        mOnBluetoothChangeListener.onStateConnected();
                        break;
                    }
                    case STATE_DISCONNECTED: {
                        Timber.d("Bluetooth disconnected");
                        mOnBluetoothChangeListener.onStateDisconnected();
                        break;
                    }
                }
                break;
            }
            case ACTION_PLAYING_STATE_CHANGED: {
                int state = intent.getIntExtra(EXTRA_STATE, STATE_NOT_PLAYING);
                switch (state) {
                    case STATE_PLAYING: {
                        Timber.d("Bluetooth started playing");
                        mOnBluetoothChangeListener.onPlaying();
                        break;
                    }
                    case STATE_NOT_PLAYING: {
                        Timber.d("Bluetooth stopped playing");
                        mOnBluetoothChangeListener.onNotPlaying();
                        break;
                    }
                }
                break;
            }
        }
    }

    public void setOnBluetoothChangeListener(OnBluetoothChangeListener onBluetoothChangeListener) {
        mOnBluetoothChangeListener = onBluetoothChangeListener;
    }

    public interface OnBluetoothChangeListener {
        void onStateConnected();

        void onStateDisconnected();

        void onPlaying();

        void onNotPlaying();
    }
}
