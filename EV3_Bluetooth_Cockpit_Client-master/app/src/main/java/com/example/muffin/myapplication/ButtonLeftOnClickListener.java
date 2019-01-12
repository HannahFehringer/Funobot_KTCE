package com.example.muffin.myapplication;

import android.view.View;
import android.view.MotionEvent;
import android.widget.Button;


/**
 * Created by Dodig Tomislav, Fehringer Hannah und Mehic Emin
 */

public class ButtonLeftOnClickListener implements View.OnTouchListener {
    private MainActivity m_currentActivity;

    private final Button bt_left;
    private int previous_speed = 0;

    public ButtonLeftOnClickListener(MainActivity currentActivity, Button btLeft) {
        m_currentActivity = currentActivity;
        bt_left = btLeft;
    }

    private void changeSpeed(int speed) {
        if (speed != previous_speed) {
            int speedDifference = Math.abs(speed - previous_speed);
            if (speedDifference >= 5) {
                if (m_currentActivity.m_btMain != null) {
                    if (m_currentActivity.m_btMain.getSocket() != null) {
                        if (m_currentActivity.m_btMain.getSocket().isConnected()) {
                            /* If device is found - transmit data */
                            m_currentActivity.m_btMain.write(new String("L" + speed).getBytes());
                            previous_speed = speed;
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            this.changeSpeed(25);
        }
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            this.changeSpeed(0);
        }
        return true;
    }
}
