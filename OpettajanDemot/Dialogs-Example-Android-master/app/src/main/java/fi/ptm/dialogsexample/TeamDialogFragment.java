package fi.ptm.dialogsexample;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 *
 * @author  PTM
 */
public class TeamDialogFragment extends DialogFragment {

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface TeamDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog, String teamName);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    TeamDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the TeamDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the TeamDialogListener so we can send events to the host
            mListener = (TeamDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString() + " must implement TeamDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // create a new AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View dialogView = inflater.inflate(R.layout.add_team_dialog, null);
        builder.setView(dialogView)
            // Set title
            .setTitle("Add a new Team")
            // Add action buttons
            .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    // find a team name
                    EditText editText = (EditText) dialogView.findViewById(R.id.editText);
                    String teamName = editText.getText().toString();
                    // Send the positive button event back to the host activity
                    mListener.onDialogPositiveClick(TeamDialogFragment.this,teamName);
                }
            })
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // Send the negative button event back to the host activity
                    mListener.onDialogNegativeClick(TeamDialogFragment.this);
                }
            });
        return builder.create();
    }
}
