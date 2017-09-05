package com.androidprogramming.k1967.dialogandnotification;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by K1967 on 5.9.2017.
 */

public class ExitDialogFrag extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Title")
                .setMessage("VOI P****'")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getActivity(), "ok", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getActivity(), ":(", Toast.LENGTH_SHORT).show();
                    }
                });
        return builder.create();
    }
}
