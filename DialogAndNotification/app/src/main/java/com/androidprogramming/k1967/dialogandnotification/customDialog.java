package com.androidprogramming.k1967.dialogandnotification;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

/**
 * Created by K1967 on 5.9.2017.
 */

public class customDialog extends DialogFragment {

    public interface CustomDialogListener{
        public void onDialogPositiveClick(DialogFragment dialog, Boolean response);
        public void onDialogNegativeClick(DialogFragment dialog, Boolean response);
    }

    CustomDialogListener mListener;

    //check if the activity this is connected to is a correct type
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {

        } catch (ClassCastException e){

        }

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.custom_dialog_layout, null);

        Button ok = (Button)dialogView.findViewById(R.id.yes);
        Button no = (Button)dialogView.findViewById(R.id.no);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onDialogPositiveClick(customDialog.this, true);
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onDialogPositiveClick(customDialog.this, true);
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onDialogNegativeClick(customDialog.this, false);
            }
        });

        builder.setView(dialogView).setTitle("Confirm question");

        return builder.create();

    }
}
