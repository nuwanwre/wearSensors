package com.example.pratik.wearsensors;

import android.os.AsyncTask;
import android.util.Log;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by pratik on 11/3/17.
 */

public class networkClient extends AsyncTask<Void, boolean, Void> {

    private Socket socket;
    private DataOutputStream dos;

    private String stream = "";

    public networkClient(){

    }

    public void connect(){
        try{
            socket = new Socket ("192.168.0.100", 80);
            dos = new DataOutputStream(socket.getOutputStream());
            Log.d("Network", Boolean.toString(socket.isConnected()));
        }
        catch (Exception e){
            Log.d("Error", e.toString());
        }
    }
    @Override
    public void doInBackground(String ... params){

    }

    public boolean sendData(ArrayList<String> data){
           for(String item : data){
               stream += item + '\n';
           }
           try {
               dos.writeUTF(stream);
               return true;
           }
           catch(Exception e){
               Log.d("Error", e.toString());
               return false;
           }
           finally {
               stream = "";
           }
    }

    public void closeSocket(){
        try {
            socket.close();
        }
        catch(Exception e){
            Log.d("Error", e.toString());
        }
    }
}
