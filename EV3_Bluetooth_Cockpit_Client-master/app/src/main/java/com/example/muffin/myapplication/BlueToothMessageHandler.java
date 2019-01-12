package com.example.muffin.myapplication;

import android.os.Handler;
import android.os.Message;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Dodig Tomislav, Fehringer Hannah und Mehic Emin
 */

public class BlueToothMessageHandler extends Handler {
    private MainActivity m_currentActivity;

    public BlueToothMessageHandler(MainActivity currentActivity) {
        m_currentActivity = currentActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        // Codes
        switch (msg.what) {
            case BluetoothClientSocketThread.RETURN_WRITE:
                break;
            case BluetoothClientSocketThread.RETURN_READ:
                break;
            case BluetoothClientSocketThread.RETURN_TOAST:
                Toast.makeText(m_currentActivity.getApplicationContext(), (String) msg.obj, Toast.LENGTH_SHORT).show();
                break;
            case BluetoothClientSocketThread.RETURN_INFORMATION:
                InformationWrapper informationWrapper = (InformationWrapper) msg.obj;



        }
    }
}
