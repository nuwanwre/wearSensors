package com.example.pratik.wearsensors;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class SensorDash extends Activity implements SensorEventListener {

    private TextView mTextView;

    private boolean recordData = false;

    private SensorManager mSensorManager;
    private Sensor mLinAcc;                             // Sensor for Linear Acceleration
    private Sensor mGyroSensor;                         // Sensor for Gyroscope
    private Sensor mHeartRate;                          // Sensor for hear rate

    public static ArrayList<String> dataArray = new ArrayList<String>();
    private Button buttonFour;
    private String statusText = "";

    // Variables necessary to calculate linear acceleration
    private float[] gravity = {0, 0, 0};
    private float[] linear_acceleration = {0, 0, 0};

    // Variables needed to calculate Heart rate
    private float hRate = 0.0f;
    // Variables that are necessary to calculate the rotation
    private float[] rotation = {0, 0, 0};
    int count=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_dash);

        buttonFour = (Button) findViewById(R.id.buttonFour);
        mTextView=(TextView)findViewById(R.id.messageText);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        // Register sensor or linear acceleration
        mLinAcc = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        // Register the gyroscope
        mGyroSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        // Register the Heartbeat sensor
        mHeartRate = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);



        buttonFour.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                if(count == 0){
                    dataArray.clear();
                    mTextView.setText("Keep walking...");
                    buttonFour.setText("Tap to Run");
                    dataArray.add("walk");
                    count ++;
                    recordData = true;
                }
                else if(count == 1){
                    dataArray.add("run");
                    mTextView.setText("Keep running...");
                    buttonFour.setText("Tap to Open Door");
                    count ++;
                }
                else if(count == 2){
                    dataArray.add("open");
                    mTextView.setText("Open door");
                    buttonFour.setText("Tap to Type");
                    count ++;

                }
                else if(count == 3){
                    dataArray.add("type");
                    mTextView.setText("Keep typing...");
                    buttonFour.setText("Stop");
                    count ++;
                }
                else{
                    mTextView.setText("Data Recorded");
                    recordData = false;
                    MainActivity.sendMessage(dataArray);
                    buttonFour.setEnabled(false);
                    buttonFour.setText("Start");
                    count = 0;
                }

            }
        });

    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if(recordData) {

            if (sensorEvent.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
                // Linear acceleration : m/s^2
                // alpha is calculated as t / (t + dT)
                // with t, the low-pass filter's time-constant
                // and dT, the event delivery rate

                final float alpha = 0.8f;

                gravity[0] = alpha * gravity[0] + (1 - alpha) * sensorEvent.values[0];
                gravity[1] = alpha * gravity[1] + (1 - alpha) * sensorEvent.values[1];
                gravity[2] = alpha * gravity[2] + (1 - alpha) * sensorEvent.values[2];
                /* Cleared for Database Trial ********************************/
                linear_acceleration[0] = sensorEvent.values[0] - gravity[0];
                linear_acceleration[1] = sensorEvent.values[1] - gravity[1];
                linear_acceleration[2] = sensorEvent.values[2] - gravity[2];
                /*/
        }

        if(sensorEvent.sensor.getType() == Sensor.TYPE_HEART_RATE){
            // Discard if Sensor status is unreliable or sensor is not contacting
                /*Cleared for Database Trial ********************************/
                hRate = sensorEvent.values[0];


            }


            if (sensorEvent.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                // Gyroscope data : rad/s
                // event.values[0] 	Rate of rotation around the x axis.
                // event.values[1] 	Rate of rotation around the y axis.
                // event.values[2] 	Rate of rotation around the z axis.
                /* Cleared for Database Trial ********************************/
                rotation[0] = sensorEvent.values[0];
                rotation[1] = sensorEvent.values[1];
                rotation[2] = sensorEvent.values[2];


            }

            dataArray.add(Float.toString(linear_acceleration[0])
                    + "," + Float.toString(linear_acceleration[1])
                    + "," + Float.toString(linear_acceleration[2])
                    + "," + Float.toString(rotation[0])
                    + "," + Float.toString(rotation[1])
                    + "," + Float.toString(rotation[2])
                    + "," + Float.toString(hRate));
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mLinAcc, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mGyroSensor, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mHeartRate, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }


}
