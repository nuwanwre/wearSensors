package com.example.pratik.wearsensors;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by pratik on 17. 3. 30.
 */

public class backGroundWorker extends AsyncTask <String,Void,String> {
    Context ctx;
    AlertDialog alertDialog;
    backGroundWorker(Context ctx){
        this.ctx=ctx;
    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        String save_url= "http://192.168.0.100/webapp/insert.php";
        String savedata_url = "http://192.168.0.100/webapp/insertSensor.php";

        String method =  params[0];
        if(method.equals("Save")){
            String name = params[1];
            String lastname= params[2];
            String dob=params[3];
            String username=params[5];
            try {
                URL url = new URL(save_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream wr= httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter= new BufferedWriter(new OutputStreamWriter
                        (wr,"UTF-8"));
                String data= URLEncoder.encode("server_name","UTF-8")+"="+URLEncoder.encode(name,"UTF-8")+"&"+
                        URLEncoder.encode("server_lastname","UTF-8")+"="+URLEncoder.encode(lastname,"UTF-8")+"&"+
                        URLEncoder.encode("server_dob","UTF-8")+"="+URLEncoder.encode(dob,"UTF-8")+"&"+
                        URLEncoder.encode("server_username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();

                InputStream IS = httpURLConnection.getInputStream();
                wr.close();
                IS.close();

                return "Data Saved on Server";
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else if(method.equals("DataSave")){
            String accx= params[1];
            String accy= params[2];
            String accz= params[3];
            String gx= params[4];
            String gy= params[5];
            String gz= params[6];
            String username=params[7];
            try {
                URL url = new URL(savedata_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream wr= httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter= new BufferedWriter(new OutputStreamWriter
                        (wr,"UTF-8"));
                String data= URLEncoder.encode("server_accx","UTF-8")+"="+URLEncoder.encode(accx,"UTF-8")+"&"+
                        URLEncoder.encode("server_accy","UTF-8")+"="+URLEncoder.encode(accy,"UTF-8")+"&"+
                        URLEncoder.encode("server_accz","UTF-8")+"="+URLEncoder.encode(accz,"UTF-8")+"&"+
                        URLEncoder.encode("server_gx","UTF-8")+"="+URLEncoder.encode(gx,"UTF-8")+"&"+
                        URLEncoder.encode("server_gy","UTF-8")+"="+URLEncoder.encode(gy,"UTF-8")+"&"+
                        URLEncoder.encode("server_gz","UTF-8")+"="+URLEncoder.encode(gz,"UTF-8")+"&"+
                        URLEncoder.encode("server_username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();

                InputStream IS = httpURLConnection.getInputStream();
                wr.close();
                IS.close();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }



    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);

    }

    @Override
    protected void onPostExecute(String result) {

        if(result=="Data Saved on Server") {
            Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
        }


    }

}

