package company.supernice.listviewexampleprogram;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Valtteri on 30.8.2017.
 */

public class DetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_layout);

        //get the Intent that has openned this activity
        Intent intent = getIntent();

        //get the data from the intent
        Bundle bundle = intent.getExtras();

        //get the phone name from intent
        String phone = bundle.getString("phone");

        //setup the phone name
        TextView textView = (TextView) findViewById(R.id.phoneTextView);
        textView.setText(phone);

        //show the corrent phone logo
        ImageView imageView = (ImageView)findViewById(R.id.phoneImageView);

        switch (phone) {
            case "Android": imageView.setImageResource(R.drawable.android); break;
            case "Iphone": imageView.setImageResource(R.drawable.ios); break;
            case "WindowsMobile": imageView.setImageResource(R.drawable.windows); break;
            case "Blackberry": imageView.setImageResource(R.drawable.blackberry); break;
            case "WebOS": imageView.setImageResource(R.drawable.webos); break;
            case "Ubuntu": imageView.setImageResource(R.drawable.ubuntu); break;
        }
    }

    public void backButtonPressed(View view){
        finish();
    }

}
