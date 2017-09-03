package fi.ptm.dialogsexample;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

/**
 *
 * @author PTM
 */

public class MainActivity extends Activity implements TeamDialogFragment.TeamDialogListener {

    private ProgressDialog dialog; // Only for testing, don't use anymore.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void exitDialog(View view) {
        ExitDialogFragment eDialog = new ExitDialogFragment();
        eDialog.show(getFragmentManager(), "exit");
    }

    public void listDialog(View view) {
        ListDialogFragment eDialog = new ListDialogFragment();
        eDialog.show(getFragmentManager(), "");
    }

    public void customDialog(View view) {
        TeamDialogFragment eDialog = new TeamDialogFragment();
        eDialog.show(getFragmentManager(), "Add a new Team");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String teamName) {
        Toast.makeText(getApplicationContext(), "New team = " + teamName, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        Toast.makeText(getApplicationContext(), "Cancel", Toast.LENGTH_SHORT).show();
    }

    public void progressDialog(View view) {
		/*
		dialog = ProgressDialog.show(
				MainActivity.this,
				"",
				"Loading. Please wait...",
				 true);
	 	*/
        ProgressDialogTask task = new ProgressDialogTask(this);
        task.execute();
    }

    class ProgressDialogTask extends AsyncTask<Void, Void, Void> {

        public static final int WAIT_LENGTH = 3000;
        private ProgressDialog dialog;

        public ProgressDialogTask(Activity activity) {
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading...");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            long start = System.currentTimeMillis();
            while(System.currentTimeMillis()-start < WAIT_LENGTH){}
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            if(dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

}
