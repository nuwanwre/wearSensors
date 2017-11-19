/*
   This class defines all the methods that needed to function the app interface.
 */


package com.example.pratik.wearsensors;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

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

public class MainActivity extends Activity implements DataApi.DataListener, GoogleApiClient.ConnectionCallbacks{

    private static final String DATA_KEY = "data";
    private GoogleApiClient mGoogleApiClient;

    private EditText name, date, height, weight;
    private Button submit;

    private int count = 0;

    private Student student;

    private ArrayList<String> sensorData = new ArrayList<String>();
    private ArrayList<ArrayList<String>> sensorArray = new ArrayList<>();

    private networkClient client ;
    private String activity = "walk";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        initControllers();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .build();

        mGoogleApiClient.connect();
        Log.d("Starting Phone : ", "Started");
    }

    private void initControllers() {
        name = (EditText) findViewById(R.id.name);

        date = (EditText) findViewById(R.id.dob);
        date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(date.getText().length() == 4){
                    date.append("-");
                }
                if(date.getText().length() == 7) {
                    date.append("-");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        height = (EditText) findViewById(R.id.height);
        weight = (EditText) findViewById(R.id.weight);

        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //client.connect();
                //Log.d("Validate:", "" + validateInput());
                if(validateInput()) {
                    client = new networkClient(getApplicationContext());
                    sensorData.add(student.getName()+","+student.getDoB()+","+student.getHeight()+","+student.getWeight());
                    sensorData.add("aX,aY,aZ,gX,gY,gZ,hr");
                    sensorData.add(activity);
                    sendMessage(student.getName());
                    showDialog();
                }
            }
        });
    }

    private boolean validateInput() {
        if ((!name.getText().toString().contains(" ") && !name.getText().toString().matches("[a-zA-Z]"))) {
            Toast toast  = Toast.makeText(getApplicationContext(), "Please recheck Name Field.", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        if ((!date.getText().toString().matches("\\d{4}-\\d{2}-\\d{2}"))){
            Toast toast  = Toast.makeText(getApplicationContext(), "Please recheck Date of Birth Field.", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        if((weight.getText().toString().isEmpty()) || (height.getText().toString().isEmpty())){
            Toast toast  = Toast.makeText(getApplicationContext(), "Please recheck Height/Weight Fields.", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        else {
            student = new Student(name.getText().toString(), date.getText().toString(), height.getText().toString(), weight.getText().toString());
            return true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Wearable.DataApi.addListener(mGoogleApiClient, this);
        Log.d("Mobile: ", "Connected");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Wearable.DataApi.removeListener(mGoogleApiClient, this);
        mGoogleApiClient.disconnect();
    }


    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        for (DataEvent event : dataEvents) {

            DataItem item = event.getDataItem();
            if (item.getUri().getPath().compareTo("/sensors") == 0) {
                DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                String data = dataMap.getString(DATA_KEY);
                if (data.equals("Done")){
                    // TODO : Reset UI and Arraylist

                    if(!sensorData.isEmpty()){
                        // sensorArray.add(sensorData);
                        //if(client.execute(sensorData)){
                        client.execute(sensorData);
                        //sensorData.clear();
                        sensorArray.clear();
                        name.setText(""); date.setText(""); height.setText(""); weight.setText("");
                        sendMessage("Done");
                        //Toast toast  = Toast.makeText(getApplicationContext(), "Data Collection complete. Transferring to server.", Toast.LENGTH_LONG);
                        //toast.show();

                        restartApp();
                        //}

                    }
                    else {
                        Log.d("Network", "Send Data Failed");
                    }


                }else {
                    // sensorData.append("\n" + data);
                    Log.d("Data :", String.valueOf(count));
                    count ++;
                    sensorData.add(data);
                    //client.execute(data);
                }
            }


        }
    }

    private void sendMessage(String data){
        // Log.d("SendMessage:Phone:", "DataChange!");
        PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/init");
        putDataMapReq.getDataMap().putString(DATA_KEY, data);
        PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
        PendingResult<DataApi.DataItemResult> pendingResult =
                Wearable.DataApi.putDataItem(mGoogleApiClient, putDataReq);
    }


    private void restartApp(){
        Intent intent =  getIntent();
        finish();
        startActivity(intent);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("Phone : ", "Connection Suspended");
    }


    public void showDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_dialog);
        dialog.setTitle("Select Activity");

        dialog.setCancelable(false);

        Button done = (Button) dialog.findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });


        dialog.show();


    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.walkRadio:
                if (checked) {
                    activity = "walk";
                    sensorData.add(activity);
                    break;
                }
            case R.id.runRadio:
                if (checked) {
                    activity = "run";
                    sensorData.add(activity);
                    break;
                }
            case R.id.typeRadio:
                if (checked) {
                    activity = "type";
                    sensorData.add(activity);
                    break;
                }
            case R.id.openRadio:
                if (checked) {
                    activity = "open";
                    sensorData.add(activity);
                    //break;
                }
        }

        //Log.d("Dialog", activity);
    }
}
