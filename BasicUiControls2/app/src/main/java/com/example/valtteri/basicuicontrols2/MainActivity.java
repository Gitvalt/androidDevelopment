package com.example.valtteri.basicuicontrols2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //---Populate AutoCompleteTextView---

            //find loginBox from view
            AutoCompleteTextView actv = (AutoCompleteTextView) findViewById(R.id.loginBox);

            //create arrayadapter
            ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,
                    new String[]{"Pasi", "Juha", "Kari", "Jouni", "Esa", "Hannu"}
            );

            //setup the adapter
            actv.setAdapter(aa);

        //---ending population---
    }
}
