package com.chooloo.www.callmanager.util;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

import java.lang.reflect.Method;

import static android.bluetooth.BluetoothProfile.EXTRA_STATE;
import static android.bluetooth.BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED;
import static android.bluetooth.BluetoothA2dp.STATE_CONNECTED;
import static android.bluetooth.BluetoothA2dp.STATE_DISCONNECTED;
import static android.bluetooth.BluetoothA2dp.ACTION_PLAYING_STATE_CHANGED;
import static android.media.session.PlaybackState.STATE_PLAYING;
import static android.bluetooth.BluetoothA2dp.STATE_NOT_PLAYING;

public class BluetoothUtils {

    public final static int STATE_CONNECTED = BluetoothA2dp.STATE_CONNECTED;
    public final static int STATE_DISCONNECTED = BluetoothA2dp.STATE_DISCONNECTED;
    public final static int STATE_PLAYING = BluetoothA2dp.STATE_PLAYING;
    public final static int STATE_NOT_PLAYING = BluetoothA2dp.STATE_NOT_PLAYING;

    public final static String ACTION_CONNECTION_STATE_CHANGED = BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED;
    public final static String ACTION_PLAYING_STATE_CHANGED = BluetoothA2dp.ACTION_PLAYING_STATE_CHANGED;


    public static boolean isBluetoothHeadsetConnected() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()
                && mBluetoothAdapter.getProfileConnectionState(BluetoothHeadset.HEADSET) == BluetoothHeadset.STATE_CONNECTED;
    }


    public static BluetoothDevice getDevice(Intent intent) {
        return intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
    }

    public static void toggleBluetooth(Context context, boolean toggle) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (toggle) {
            audioManager.setMode(0);
            audioManager.setBluetoothScoOn(true);
            audioManager.startBluetoothSco();
            audioManager.setMode(AudioManager.MODE_IN_CALL);
        } else {
            audioManager.setBluetoothScoOn(false);
            audioManager.stopBluetoothSco();
            audioManager.setMode(AudioManager.MODE_NORMAL);
            audioManager.setSpeakerphoneOn(true);
        }
    }
}
