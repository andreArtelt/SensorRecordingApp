package com.sensorrecording;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class RecordingActivity extends AppCompatActivity {
    private Intent mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording);

        mService = super.getIntent().getParcelableExtra("service");
    }

    public void onClickStop(View view) {
        // Stop service recording sensor
        stopService(mService);

        // Switch back to start page
        Intent startActivity = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(startActivity);
    }
}
