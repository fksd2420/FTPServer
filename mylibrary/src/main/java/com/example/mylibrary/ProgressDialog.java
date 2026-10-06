package com.example.mylibrary;

import android.app.Activity;
import android.content.Context;

public class ProgressDialog {

    private Context context = null;
    android.app.ProgressDialog progressDialog = null;

    public ProgressDialog(Context context) {
        this.context = context;

    }

    public void show(String title, String message) {
        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog = new android.app.ProgressDialog(context);
                progressDialog.setTitle(title);
                progressDialog.setMessage(message);
                progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);

                progressDialog.show();
            }
        });

    }

    public void dismiss() {
        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        });

    }
}
