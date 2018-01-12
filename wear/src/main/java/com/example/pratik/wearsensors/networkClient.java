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
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by pratik on 11/19/17.
 */

public class networkClient  extends AsyncTask<ArrayList<String>, Void, Boolean> {

    private final String server_url = "http://223.194.199.10/webapp/saveFile.php";

    private HttpURLConnection httpURLConnection;
    private URL url;

    private DataOutputStream dos;
    private String stream = "";

    Context ctx;

    public networkClient(Context ctx){
        // no context
        this.ctx = ctx;
    }

    protected void onPreExecute() {
        super.onPreExecute();
    }

    protected Boolean doInBackground(ArrayList<String>... dataList) {

        for (String item : dataList[0]) {
            stream += item + '\n';
        }

        try{
            url = new URL(server_url);
            httpURLConnection = (HttpURLConnection)url.openConnection();
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

        }
        catch(Exception e){
            Log.d("Error", e.toString());
            return false;
        }
        finally {
            stream = "";
            httpURLConnection.disconnect();
            return true;
        }
    }

    protected void onProgressUpdate(Void voids) {
        //Log.d("ANDRO_ASYNC",progress[0]);
    }

    protected boolean onPostExecute(boolean result) {
        //closeSocket();
        Toast toast  = Toast.makeText(ctx, "Data transfer Complete", Toast.LENGTH_LONG);
        toast.show();
        return true;
    }
}
