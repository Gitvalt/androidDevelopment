package fi.ptm.notificationsexample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * Created by PTM on 22/04/15.
 */
public class ResultActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
    }

    public void backButtonClicked(View view) {
        finish();
    }

}
