package com.sensorrecording;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import java.io.File;

public class MainActivity extends AppCompatActivity {
    private Intent mService;
    public static String mStoragePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mService = null;

        // Check some requirements
        checkRequirements();

        // Find storage path
        getStoragePath();
    }

    private void checkRequirements()
    {
        if(Utils.isExternalStorageWritable() == false) {
            quit("Can not write on sd-card (external storage)");
        }
        /*if(Utils.checkForAccelerometer(this) == false) {
            msg("No suitable sensor found (Accelerometer)", "Missing requirement");
        }
        if(Utils.checkForLinearAccelerometer(this) == false) {
            msg("No suitable sensor found (Linear accelerometer)", "Missing requirement");
        }
        if(Utils.checkForGyroscope(this) == false) {
            msg("No suitable sensor found (Gyroscope)", "Missing requirement");
        }*/
    }

    private void getStoragePath() {
        // Find suitable path for storage
        try {
            /*mStoragePath = System.getenv("SECONDARY_STORAGE");
            File file = new File(mStoragePath);
            if (mStoragePath == null || !Environment.getStorageState(file).equals("MEDIA_MOUNTED")) {
                mStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath();  // Note: This might be emulated!
            }*/
            mStoragePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath();
        }
        catch(Exception ex) {
            // If nothing works...
            mStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        }

        // Display path in ui
        TextView ctrlStorage = (TextView) findViewById(R.id.txtStoragePath);
        ctrlStorage.setText("Data is saved to: " + mStoragePath);
    }

    private void msg(String strMsg, String strTitle) {
        AlertDialog dlg = new AlertDialog.Builder(MainActivity.this).create();
        dlg.setTitle(strTitle);
        dlg.setMessage(strMsg);
        dlg.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        dlg.show();
    }

    private void quit(String strMsg) {
        AlertDialog dlg = new AlertDialog.Builder(MainActivity.this).create();
        dlg.setTitle("Missing requirement");
        dlg.setMessage(strMsg);
        dlg.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        exit();
                    }
                });
        dlg.show();
    }

    public void onClickExit(View view) {
        exit();
    }

    private void exit() {
        // Close/Shutdown app
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void onClickWalking(View view) {
        // Start service logging sensor
        startLogger("walking");

        // Switch to recording page
        switchToRecording();
    }

    public void onClickRunning(View view) {
        // Start service logging sensor
        startLogger("jogging");

        // Switch to recording page
        switchToRecording();
    }

    public void onClickDownstairs(View view) {
        // Start service logging sensor
        startLogger("downstairs");

        // Switch to recording page
        switchToRecording();
    }

    public void onClickUpstairs(View view) {
        // Start service logging sensor
        startLogger("upstairs");

        // Switch to recording page
        switchToRecording();
    }

    public void onClickBiking(View view) {
        // Start service logging sensor
        startLogger("biking");;

        // Switch to recording page
        switchToRecording();
    }

    public void onClickSitting(View view) {
        // Start service logging sensor
        startLogger("sitting");

        // Switch to recording page
        switchToRecording();
    }

    private void startLogger(String label) {
        mService = new Intent(this, SensorLoggerService.class);
        mService.putExtra("label", label);
        startService(mService);
    }

    private void switchToRecording() {
        Intent recordingActivity = new Intent(getApplicationContext(), RecordingActivity.class);
        recordingActivity.putExtra("service", mService);
        startActivity(recordingActivity);
    }
}
