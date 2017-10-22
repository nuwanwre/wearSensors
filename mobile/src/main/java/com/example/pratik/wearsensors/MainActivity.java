package com.example.pratik.wearsensors;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;

public class MainActivity extends Activity implements DataApi.DataListener, GoogleApiClient.ConnectionCallbacks{

    private static final String DATA_KEY = "data";
    private GoogleApiClient mGoogleApiClient;

    private EditText name, date, height, weight;
    private Button submit;

    private Student student;

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
        height = (EditText) findViewById(R.id.height);
        weight = (EditText) findViewById(R.id.weight);

        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Validate:", "" + validateInput());
            }
        });
    }

    private boolean validateInput() {
        if ((!name.getText().toString().contains(" ") && !name.getText().toString().matches(".*[a-zA-Z].*")) ||
                date.getText().toString().equals("") || height.getText().toString().equals("") ||
                weight.getText().toString().equals("")
                ) {
            return false;
        } else {
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
                // sensorData.append("\n" + data);
            }


        }
    }


    @Override
    public void onConnectionSuspended(int i) {
        Log.d("Phone : ", "Connection Suspended");
    }

}
