package com.example.gh_train_app.OthersFiles;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.appcompat.app.AlertDialog;

import com.example.gh_train_app.R;


public class CommonMethod {

    public static boolean isNetworkAvailable(Context ctx) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void showAlert(String message, Activity context) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message).
                setTitle("Oops!!!")
                .setIcon(R.drawable.ic_warning_black_24dp)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        try {
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void showExit(String message, Activity context) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message).
                setTitle("Exit")
                .setIcon(R.drawable.ic_warning_black_24dp)
                .setCancelable(false)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        System.exit(0);
                    }
                });

        try {
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}