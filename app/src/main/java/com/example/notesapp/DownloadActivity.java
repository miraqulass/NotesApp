package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.PowerManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.Bundle;

public class DownloadActivity extends AppCompatActivity {

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);


        progressDialog = new ProgressDialog(DownloadActivity.this);
        progressDialog.setMessage("Starting download...");
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(true);

        final DownloadTask downloadTask = new DownloadTask(DownloadActivity.this);

    }

    private class DownloadTask extends com.example.notesapp.DownloadTask {
        private Context context;
        private PowerManager.WakeLock wakeLock;
        public DownloadTask(Context context) {
            this.context = context;
        }


        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;

            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK){
                    return "Server returned HTTP"+ connection.getResponseCode()+ " "+ connection.getResponseMessage();
                    
                }
                
                int fileLength = connection.getContentLength();
                
                input = connection.getInputStream();
                output = new FileOutputStream("/sdcard/file_name.extension");
                
                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    
                    total += count;
                    
                    if (fileLength > 0){
                        publishProgress((int) (total * 100 / fileLength));
                        output.write(data, 0, count);
                    }
                }

            } catch (Exception e) {
                return e.toString();
            } 
            finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException e) {
                    
                }
                if (connection != null)
                    connection.disconnect();
            }
            
            return null;
        }

            @Override
        protected void onPreExecute() {
            super.onPreExecute();
            PowerManager powerManager = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
            wakeLock.acquire();
            progressDialog.show();
        }

       @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            progressDialog.setIndeterminate(false);
            progressDialog.setMax(100);
            progressDialog.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            wakeLock.release();
            progressDialog.dismiss();
            if (result != null)
                Utility.showToast(DownloadActivity.this, "Download error: "+result);
            else
                Utility.showToast(DownloadActivity.this, "File Downloaded");
        }

        @Override
        protected void onProgressUdpate(Integer... progress) {

        }
    }

    }

