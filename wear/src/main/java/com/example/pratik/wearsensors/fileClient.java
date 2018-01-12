package com.example.pratik.wearsensors;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by pratik on 11/19/17.
 */

public class fileClient extends AsyncTask<String, Void, Boolean> {

    //private final String

    Context ctx;
    private String stream = "";

    public  fileClient(Context ctx){
        this.ctx = ctx;
    }

    protected void onPreExecute() {
        super.onPreExecute();
    }


    protected Boolean doInBackground(String... data){

        String sourceFileUri = ctx.getFilesDir().getAbsolutePath() + "/data.csv";

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        if (sourceFile.isFile()) {

            try {
                String upLoadServerUri = "http://223.194.199.10/webapp/saveCSV.php";

                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(
                        sourceFile);
                URL url = new URL(upLoadServerUri);

                // Open a HTTP connection to the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE",
                        "multipart/form-data");
                conn.setRequestProperty("Content-Type",
                        "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("bill", sourceFileUri);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"bill\";filename=\""
                        + sourceFileUri + "\"" + lineEnd);

                dos.writeBytes(lineEnd);

                // create a buffer of maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math
                            .min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0,
                            bufferSize);

                }

                // send multipart form data necesssary after file
                // data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens
                        + lineEnd);

                // Responses from the server (code and message)
                int serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn
                        .getResponseMessage();

                if (serverResponseCode == 200) {

                    // messageText.setText(msg);
                    //Toast.makeText(ctx, "File Upload Complete.",
                    //      Toast.LENGTH_SHORT).show();

                    // recursiveDelete(mDirectory1);

                }

                // close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();
                return true;

            } catch (Exception e) {

                // dialog.dismiss();
                e.printStackTrace();
                return false;
            }

        }
        else {
            Log.d("wear", "Missig File");
            return false;
        }

    }

    // Saves file onto devices internal memory
    public void toCSV(ArrayList<String> data){
        try {
            Log.d("File", "Start");
            updateStream(data);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(ctx.openFileOutput("data.csv", Context.MODE_PRIVATE));
            outputStreamWriter.write(stream);
            outputStreamWriter.close();
            Log.d("File", "Done");
        }
        catch (Exception e) {
            Log.d("Exception", "File write failed: " + e.toString());
        }
    }


    // Arraylist to String
    private void updateStream(ArrayList<String> data){
        stream = "";
        for (String item : data){
            stream += item + '\n';
        }
    }
}
