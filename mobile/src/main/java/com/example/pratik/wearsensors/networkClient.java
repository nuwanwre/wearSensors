package com.example.pratik.wearsensors;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by pratik on 11/3/17.
 */

public class networkClient extends AsyncTask <ArrayList<String>, Void, Boolean> {

    Context ctx;

    private Socket socket;
    private DataOutputStream dos;

    private String stream = "";

    public networkClient(Context ctx) {
        this.ctx = ctx;
    }

    protected void onPreExecute() {
       super.onPreExecute();
    }



    protected Boolean doInBackground(ArrayList<String>... dataList) {
        String save_url= "http://192.168.0.100/webapp/saveFile.php";

        for (String item : dataList[0]) {
            stream += item + '\n';
            //Log.d("Item:", item);
        }


        try {
            //stream = dataList[0];
            URL url = new URL(save_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setDoOutput(true);
            OutputStream wr= httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter= new BufferedWriter(new OutputStreamWriter
                    (wr,"UTF-8"));
            String temp = URLEncoder.encode("sensor_stream","UTF-8")+"="+URLEncoder.encode(stream,"UTF-8");

            bufferedWriter.write(temp);
            bufferedWriter.flush();
            bufferedWriter.close();

            InputStream IS = httpURLConnection.getInputStream();
            wr.close();
            IS.close();
            //dos.writeUTF(stream);
            return true;
        } catch (Exception e) {
            Log.d("Error", e.toString());
            return false;
        } finally {
            stream = "";
        }
    }

    public void connect() {
        try {
            socket = new Socket("192.168.0.100", 80);
            dos = new DataOutputStream(socket.getOutputStream());
            Log.d("Network", Boolean.toString(socket.isConnected()));
        } catch (Exception e) {
            Log.d("Error", e.toString());
        }
    }

    public boolean sendData(ArrayList<String> data) {
        for (String item : data) {
            stream += item + '\n';
        }
        try {
            dos.writeUTF(stream);
            return true;
        } catch (Exception e) {
            Log.d("Error", e.toString());
            return false;
        } finally {
            stream = "";
        }
    }

    public void closeSocket() {
        try {
            socket.close();
        } catch (Exception e) {
            Log.d("Error", e.toString());
        }
    }

    protected void onProgressUpdate(Void voids) {
        //Log.d("ANDRO_ASYNC",progress[0]);
    }

    protected void onPostExecute(boolean result) {
        //closeSocket();
        Toast toast  = Toast.makeText(ctx, "Data transfer Complete", Toast.LENGTH_SHORT);
        toast.show();
    }
}
