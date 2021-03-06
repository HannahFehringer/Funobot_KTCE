package com.example.muffin.myapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Set;

/**
 * Created by Dodig Tomislav, Fehringer Hannah und Mehic Emin
 */

public class ButtonConnectOnClickListener implements View.OnClickListener {
    private BluetoothAdapter m_btAdapter;
    private MainActivity m_currentActivity;

    public ButtonConnectOnClickListener(BluetoothAdapter btAdapter, MainActivity currentActivity) {
        m_btAdapter = btAdapter;
        m_currentActivity = currentActivity;
    }

    public static Set<BluetoothDevice> findDevices() {
        return null;
    }

    private void activateBluetooth() {
        Intent startBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        m_currentActivity.startActivityForResult(startBT, MainActivity.REQUEST_ENABLE_BT);
    }

    /* Returns device that got selected - null on no choice*/
    private void createPairingDeviceDialog() {
        Set<BluetoothDevice> knownDevices = m_btAdapter.getBondedDevices();

        if (knownDevices.size() > 0) {
            //Yes device found -> show and if the user wants to connect to another one, let him searchForDevice()
            BluetoothPairedDevicesDialog dialog = new BluetoothPairedDevicesDialog().initialize(m_currentActivity, knownDevices);
            dialog.show(m_currentActivity.getFragmentManager(), "Devices");
        } else {
            // No device found
            BluetoothPairedDevicesDialog dialog = new BluetoothPairedDevicesDialog().initialize(m_currentActivity, findDevices());
            dialog.show(m_currentActivity.getFragmentManager(), "Found devices");
        }
    }

    public void onClick(View v) {

        if (!m_btAdapter.isEnabled()) {
            activateBluetooth();
            Toast.makeText(m_currentActivity.getApplicationContext(), "Please enable Bluetooth first.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(m_currentActivity.getApplicationContext(), "Searching...", Toast.LENGTH_SHORT).show();
            createPairingDeviceDialog();
        }

    }
}
