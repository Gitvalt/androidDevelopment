package com.example.k1967.basicuicontrols;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void selectButtonClicked(View view){
        RadioGroup rg = (RadioGroup) findViewById(R.id.myNiceRadioGroup);
        int id = rg.getCheckedRadioButtonId();
        RadioButton button = (RadioButton)findViewById(id);
        String text = (String)button.getText();
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }
}
