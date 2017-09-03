package fi.ptm.toastexample;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 *
 * @author PTM
 */

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // this method is called from layout (activity_main)
    public void showShortToast(View view) {
        Toast.makeText(getApplicationContext(), "Short Toast", Toast.LENGTH_SHORT).show();
    }

    // this method is called from layout (activity_main)
    public void showLongToast(View view) {
        Toast.makeText(getApplicationContext(), "Long Toast", Toast.LENGTH_SHORT).show();
    }

    // this method is called from layout (activity_main)
    public void showCustomPositionToast(View view) {
        Toast toast = Toast.makeText(getApplicationContext(), "Custom Position Toast", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM|Gravity.LEFT, 0, 0);
        toast.show();
    }

    // this method is called from layout (activity_main)
    public void showCustomLayoutToast(View view) {
        // get layout from Activity
        LayoutInflater inflater = getLayoutInflater();
        // inflate layout from XML (toast.xml)
        View layout = inflater.inflate(R.layout.toast, (ViewGroup) findViewById(R.id.toast));
        // get image from resource
        ImageView image = (ImageView) layout.findViewById(R.id.imageView);
        image.setImageResource(R.drawable.ptm);
        // set text
        TextView text = (TextView) layout.findViewById(R.id.textView);
        text.setText("This is a custom PTM toast!");
        // create and show toast
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 100);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}
