package com.example.pratik.wearsensors;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Created by pratik on 17. 11. 19.
 */

public class newSensData {

    private OutputStreamWriter outputStreamWriter;
    private Context ctx;
    newSensData(Context ctx){
        this.ctx = ctx;
        try {
            if(!fileExists("data.csv"))
                outputStreamWriter = new OutputStreamWriter(this.ctx.openFileOutput("data.csv", Context.MODE_PRIVATE));
            else{
                ctx.deleteFile("data.csv");
                outputStreamWriter = new OutputStreamWriter(this.ctx.openFileOutput("data.csv", Context.MODE_PRIVATE));
            }
        }
        catch(Exception e){
            Log.d("FileError", e.toString());
        }
    }

    /*private ArrayList<String> accx=new ArrayList<String>();
    private ArrayList<String> accy=new ArrayList<String>();
    private ArrayList<String> accz=new ArrayList<String>();
    private ArrayList<String> gx=new ArrayList<String>();
    private ArrayList<String> gy=new ArrayList<String>();
    private ArrayList<String> gz=new ArrayList<String>();
    private ArrayList<String> hr=new ArrayList<String>();*/
    private ArrayList<String> dataArray=new ArrayList<>();

    public void addSensorData(String accx,String accy, String accz, String gx, String gy, String gz, String hr){
        /*this.accx.add(accx);
        this.accy.add(accy);
        this.accz.add(accz);
        this.gx.add(gx);
        this.gy.add(gy);
        this.gz.add(gz);
        this.hr.add(hr);*/
        this.dataArray.add(accx+","+accy+","+accz+","+gx+","+","+gy+","+gz+","+hr);
    }

    public void addUserInfo(String userInfo){
        dataArray.add(userInfo);
    }

    public String getDataArrayGet() {
        Log.d("DataArraySize:::",Integer.toString(dataArray.size()));
        return dataArray.get(0);
    }

    public ArrayList<String> getDataArray() {
        return dataArray;
    }

    // Saves file onto devices internal memory
    public void toCSV(String data){
        try {
            //Log.d("File", "Start");
            //updateStream(data);
            if(data.equals("Close"))
                outputStreamWriter.close();
            else
                outputStreamWriter.append(data + "\r\n");

            //Log.d("File", "Done");
        }
        catch (Exception e) {
            Log.d("Exception", "File write failed: " + e.toString());
        }
    }

    public boolean fileExists(String filename) {
        File file = ctx.getFileStreamPath(filename);
        if(file == null || !file.exists()) {
            return false;
        }
        return true;
    }
}
