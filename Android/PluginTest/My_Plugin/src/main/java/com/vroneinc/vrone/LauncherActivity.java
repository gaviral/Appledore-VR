package com.vroneinc.vrone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Window;

public class LauncherActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = this.getWindow();

        Intent intent = new Intent(this, MainActivity.class);
        //Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
        finish();
    }
}
