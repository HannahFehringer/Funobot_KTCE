package com.example.muffin.myapplication;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;


/**
 * Created by Dodig Tomislav, Fehringer Hannah und Mehic Emin
 */


public class MainActivity extends AppCompatActivity {
    /* Constants */
    public static final int REQUEST_ENABLE_BT = 1;
    public BluetoothClientSocketThread m_btMain;
    /* GUI */
    public Button m_connectButton;
    public Button bt_up;
    public Button bt_down;
    public Button bt_left;
    public Button bt_right;
    /* Adapter */
    private BluetoothAdapter m_btAdapter;
    /* Sensors */
    private SensorManager m_sensorManager;
    private Sensor m_accelerometer;
    private Sensor m_geomagnetic;


    private boolean init() {
        /* 1. Initiate by XML */
        m_connectButton = findViewById(R.id.button_connect);
        bt_up = findViewById(R.id.button_up);
        bt_down = findViewById(R.id.button_down);
        bt_left = findViewById(R.id.button_left);
        bt_right = findViewById(R.id.button_right);

        /* 2. Initiate sensors */
        m_sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        // m_rotationSensor = m_sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);;
        m_geomagnetic = m_sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        ;
        m_accelerometer = m_sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        /* 3. Check for adapters */
        m_btAdapter = BluetoothAdapter.getDefaultAdapter();
        m_btMain = null;

        /* 4. Add Listeners */
        m_connectButton.setOnClickListener(new ButtonConnectOnClickListener(m_btAdapter, this));
        bt_up.setOnTouchListener(new ButtonUpOnClickListener(this, bt_up));
        bt_down.setOnTouchListener(new ButtonDownOnClickListener(this,bt_down));
        bt_left.setOnTouchListener(new ButtonLeftOnClickListener(this,bt_left));
        bt_right.setOnTouchListener(new ButtonRightOnClickListener(this,bt_right));
        SensorEventListener listener = new SensorEventListener() {
            float[] gravity = null;
            float[] geomagnetic = null;
            float R[] = new float[9];
            float I[] = new float[9];
            float orientation[] = new float[3];
            @Override
            public void onSensorChanged(SensorEvent event) {
                /*
                System.out.println(event.sensor.getStringType());
                String txt = "";
                m_textBox.getText().clear();
                for (int i = 0; i < event.values.length; i++) {
                    txt += ("values[" + i + "]: " + Math.round(Math.toDegrees(event.values[i]))) + "\r\n";
                }
                if(event.values.length > 0) {
                    m_textBox.append(txt);
                }*/
                //copy paste stack overflow yeahh
                float azimut = 0.0f, pitch = 0.0f, roll = 0.0f;
                if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                    // Log.i("if","accelerometer");
                    gravity = event.values;
                }
                if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {

                    //Log.i("if","geomagnetic");
                    geomagnetic = event.values;
                }
                if (event.sensor.getType() != Sensor.TYPE_MAGNETIC_FIELD && event.sensor.getType() != Sensor.TYPE_ACCELEROMETER) {
                    //Log.i("que",event.sensor.toString());
                }

                if (gravity != null && geomagnetic != null) {

                    boolean success = SensorManager.getRotationMatrix(R, I, gravity, geomagnetic);
                    if (success) {
                        SensorManager.getOrientation(R, orientation);
                        azimut = (float) Math.toDegrees(orientation[0]); // orientation contains: azimut, pitch and roll
                        pitch = (float) Math.toDegrees(orientation[1]);
                        roll = (float) Math.toDegrees(orientation[2]);
                        //m_textBox.getText().clear();
                        //m_textBox.append("azi: " + azimut + "\n");
                        //m_textBox.append("pitch: " + pitch + "\n");
                        //m_textBox.append("roll: " + roll + "\n");
                    } else {
                        Log.i("main()", "no success");
                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                Log.i("SensorEventListener()", "Listener: " + sensor.getName() + " | acc: " + accuracy);
            }
        };
        m_sensorManager.registerListener(listener, m_accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        m_sensorManager.registerListener(listener, m_geomagnetic, SensorManager.SENSOR_DELAY_NORMAL);
        /* Returns true on all init */
        boolean all_success;

        all_success = (m_connectButton != null)  && (m_btAdapter != null) && (bt_up != null) && (bt_down != null)
                && (m_accelerometer != null) && (bt_left != null)  && (bt_left != null) && (m_geomagnetic != null);
        return all_success;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Log.i("main", "onCreate: ");

        if (!init()) {
            Log.i("INIT()", "ERROR AHHH");
            System.exit(1);
        }
    }
}
