package com.adam.myplugin;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Toast.makeText(this, "Test activity create", Toast.LENGTH_LONG);
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
    }

    public void buttonClicked(View v) {
        Intent intent = new Intent(this, com.adam.myplugin.GameActivity.class);
        startActivityForResult(intent, 0);
    }
}