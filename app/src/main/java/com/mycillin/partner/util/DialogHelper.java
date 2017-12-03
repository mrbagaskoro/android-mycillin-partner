package com.mycillin.partner.util;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;

import com.mycillin.partner.R;

public class DialogHelper {
    public static void showDialog(Handler handler, final Activity activity, final String title, final String message, final Boolean isFinish) {
        if (activity != null) {
            if (!activity.isFinishing()) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        new AlertDialog.Builder(activity)
                                .setTitle(title)
                                .setMessage(message)
                                .setIcon(R.mipmap.ic_launcher)
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (isFinish) {
                                            activity.finish();
                                        }
                                    }
                                })
                                .show();
                    }
                });
            }
        }
    }

    public static void showDialog(final Activity activity, final String title, final String message) {
        if (!activity.isFinishing()) {
            new AlertDialog.Builder(activity)
                    .setTitle(title)
                    .setMessage(message)
                    .setIcon(R.mipmap.ic_launcher)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }
    }
}
