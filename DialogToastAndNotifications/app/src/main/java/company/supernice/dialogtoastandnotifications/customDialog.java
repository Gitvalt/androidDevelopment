package company.supernice.dialogtoastandnotifications;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by Valtteri on 3.9.2017.
 */

public class customDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //create a new dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //get layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.customdialogger, null);
        builder.setView(dialogView)
                .setTitle("test")
                .setPositiveButton("add", View.OnClickListener(){

        })
        return super.onCreateDialog(savedInstanceState);
    }
}
