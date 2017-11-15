/*
    This module listens to the following sensors on the Smartwatch and pushes data
    onto the connected phone.

    Sensors:
        - Linear Acc : X, Y, Z
        - Gyroscope
        - Heart rate

    All the strings are hardcoded. No support for multiple languages.

    @author : nuwanwre
 */

package com.example.pratik.wearsensors;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;

public class MainActivity extends WearableActivity implements
        DataApi.DataListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener/*,
        SensorEventListener*/{


    private SensorManager mSensorManager;
    /*
    dataBaseHelper dbHandler;
    NewTrain newTrain=new NewTrain();
    */
    //private ArrayList<String> dataArray = new ArrayList<String>();

    //private Sensor mLinAcc;                             // Sensor for Linear Acceleration
    //private Sensor mGyroSensor;                         // Sensor for Gyroscope
    //private Sensor mHeartRate;                          // Sensor for hear rate


    private static GoogleApiClient mGoogleApiClient;           // Google API Client
    private static final String DATA_KEY = "data";

    private Button buttonTap;                           // Button to start/stop recording
    private TextView status;                            // Label to update user about the status

    //private String statusText = "";
    //private boolean recordData = false;

    // Variables necessary to calculate linear acceleration
    //private float[] gravity = {0, 0, 0};
    //private float[] linear_acceleration = {0, 0, 0};

    // Variables needed to calculate Heart rate
    //private float hRate = 0.0f;
    // Variables that are necessary to calculate the rotation
    //private float[] rotation = {0, 0, 0};

    private String init;

    private int count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.round_activity_main);

        buttonTap = (Button) findViewById(R.id.buttonStart);
        status = (TextView) findViewById(R.id.status);

        buttonTap.setEnabled(false);
        /*
        dbHandler = new dataBaseHelper(this, null, null, 1);
        newTrain.NewUserDataSensor();
        //**/
        buttonTap.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(v.getId()==R.id.buttonStart){
                    Intent i= new Intent(v.getContext(), SensorDash.class);
                    startActivity(i);
                }

                /*if(count == 0){
                    dataArray.clear();
                    status.setText("Keep walking...");
                    buttonTap.setText("Tap to Run");
                    dataArray.add("walk");
                    count ++;
                    recordData = true;
                }
                else if(count == 1){
                    dataArray.add("run");
                    status.setText("Keep running...");
                    buttonTap.setText("Tap to Open Door");
                    count ++;
                }
                else if(count == 2){
                    dataArray.add("open");
                    status.setText("Open door");
                    buttonTap.setText("Tap to Type");
                    count ++;

                }
                else if(count == 3){
                    dataArray.add("type");
                    status.setText("Keep typing...");
                    buttonTap.setText("Stop");
                    count ++;
                }
                else{
                status.setText(statusText);
                    recordData = false;
                    sendMessage(dataArray);
                    //sendMessage("Done");
                    buttonTap.setEnabled(false);
                    buttonTap.setText("Start");
                    count = 0;
                }*/
            }
        });

        /*mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // Register sensor or linear acceleration
        mLinAcc = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        // Register the gyroscope
        mGyroSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        // Register the Heartbeat sensor
        mHeartRate = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);*/

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .build();

        mGoogleApiClient.connect();

        Log.d("Wear: ", "Ready");
    }

    /*@Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }*/

    /*@Override
    public final void onSensorChanged(SensorEvent event) {

        // Compute if only recording data is enabled
        if(recordData){


            if(event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION){
                // Linear acceleration : m/s^2
                // alpha is calculated as t / (t + dT)
                // with t, the low-pass filter's time-constant
                // and dT, the event delivery rate

                final float alpha = 0.8f;

                gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
                gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
                gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

                linear_acceleration[0] = event.values[0] - gravity[0];
                linear_acceleration[1] = event.values[1] - gravity[1];
                linear_acceleration[2] = event.values[2] - gravity[2];

            }

            /*if(event.sensor.getType() == Sensor.TYPE_HEART_RATE){
                // Discard if Sensor status is unreliable or sensor is not contacting

                hRate = event.values[0];



            }


            if(event.sensor.getType() == Sensor.TYPE_GYROSCOPE){
                // Gyroscope data : rad/s
                // event.values[0] 	Rate of rotation around the x axis.
                // event.values[1] 	Rate of rotation around the y axis.
                // event.values[2] 	Rate of rotation around the z axis.

                rotation[0] = event.values[0];
                rotation[1] = event.values[1];
                rotation[2] = event.values[2];

            }


            /* Log.d("Data: ", "X : " + Float.toString(linear_acceleration[0])
                            + "\tY : " + Float.toString(linear_acceleration[1])
                            + "\tZ : " + Float.toString(linear_acceleration[2])
                            + "\nX : " + Float.toString(rotation[0])
                            + "\tY : " + Float.toString(rotation[1])
                            + "\tZ : " + Float.toString(rotation[2]));
             */

            /*dataArray.add(Float.toString(linear_acceleration[0])
                    + "," + Float.toString(linear_acceleration[1])
                    + "," + Float.toString(linear_acceleration[2])
                    + "," + Float.toString(rotation[0])
                    + "," + Float.toString(rotation[1])
                    + "," + Float.toString(rotation[2])
                    + "," + Float.toString(hRate));


        }

    }*/



    public static void sendMessage(ArrayList<String> data){
        Log.d("sendM", String.valueOf(data.size()));
        PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/sensors");
        putDataMapReq.getDataMap().putStringArrayList(DATA_KEY, data);
        PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
        PendingResult<DataApi.DataItemResult> pendingResult =
                Wearable.DataApi.putDataItem(mGoogleApiClient, putDataReq);
        //restartApp();

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Wearable.DataApi.addListener(mGoogleApiClient, this);
        Log.d("Wear: ", "Connected");
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.disconnect();
        Log.d("Wear : ", "Connection Suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("Wear : ", "Connection Failed");
    }

    /*@Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mLinAcc, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mGyroSensor, SensorManager.SENSOR_DELAY_NORMAL);
<<<<<<< HEAD
        mSensorManager.registerListener(this, mHeartRate, SensorManager.SENSOR_DELAY_NORMAL);
=======
        mSensorManager.registerListener(this, mHeartRate, SensorManager.SENSOR_DELAY_FASTEST);
>>>>>>> parent of e819f7d... Added selectio of activities
        mGoogleApiClient.connect();
    }*/

    @Override
    protected void onPause() {
        super.onPause();
        Wearable.DataApi.removeListener(mGoogleApiClient, this);
        //mSensorManager.unregisterListener(this);
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {


        for (DataEvent event : dataEvents) {
            // Log.d("SendMessage:Wear:", "DataChange!");

            DataItem item = event.getDataItem();
            if (item.getUri().getPath().compareTo("/init") == 0) {
                DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                init = dataMap.getString(DATA_KEY);
                // Log.d("Init :", data);
                // sensorData.append("\n" + data);
            }
        }

        if (init.equals("Done")){
            //statusText = "Ready";
            buttonTap.setEnabled(false);
        }
        else {
            buttonTap.setEnabled(true);
            //statusText = "Data Recorded for : " + init;
        }
    }

}
