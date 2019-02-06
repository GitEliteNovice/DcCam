package com.aryan.dhankar.DcCam;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.aryan.dhankar.DcCam.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (null == savedInstanceState) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, DcCamFragment.newInstance())
                    .commit();
        }

    }
}
