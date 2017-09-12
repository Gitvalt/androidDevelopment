package com.example.k1967.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void buttonPressed(View view){
        EditText nameEditText = (EditText)findViewById(R.id.NameEditText);

        String name = nameEditText.getText().toString();

        Intent intent = new Intent(this, SecondActivity.class);
        intent.putExtra("editName", name);
        startActivity(intent);

    }
}
