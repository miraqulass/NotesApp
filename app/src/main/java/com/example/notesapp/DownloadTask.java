package com.example.notesapp;

import android.os.AsyncTask;

public abstract class DownloadTask extends AsyncTask<String, Integer, String> {
    protected abstract void onProgressUdpate(Integer... progress);
}
