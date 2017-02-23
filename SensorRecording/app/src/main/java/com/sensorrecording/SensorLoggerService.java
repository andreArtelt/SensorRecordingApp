package com.sensorrecording;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.IBinder;
import java.util.Calendar;

public class SensorLoggerService extends Service implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mLinAccelerometer, mAccelerometer, mGyroscope;
    private Logger mLoggerAccelerometer, mLoggerLinAccelerometer, mLoggerGyroscope;

    public void init(String strPrefix)
    {
        String strID = "" + Calendar.getInstance().getTimeInMillis();  // Create unique id for this record

        // If sensor is not available, do not use it!
        if(Utils.checkForAccelerometer(this) == false)
            mLoggerAccelerometer = null;
        else
            mLoggerAccelerometer = new Logger(strPrefix + "-a-" + strID);


        if(Utils.checkForLinearAccelerometer(this) == false)
            mLoggerLinAccelerometer = null;
        else
            mLoggerLinAccelerometer = new Logger(strPrefix + "-al-" + strID);

        if(Utils.checkForGyroscope(this) == false)
            mLoggerGyroscope = null;
        else
            mLoggerGyroscope = new Logger(strPrefix + "-g-" + strID);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            mLinAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

            // Init logger
            init(intent.getExtras().getString("label"));

            // ATTENTION: API does not allow specifying a custom sampling rate! (SENSOR_DELAY_NORMAL is too slow)
            // https://developer.android.com/guide/topics/sensors/sensors_overview.html states: SENSOR_DELAY_GAME (20,000 microsecond delay) = 50Hz
            if(mLinAccelerometer != null)
                mSensorManager.registerListener(this, mLinAccelerometer, SensorManager.SENSOR_DELAY_GAME);
            if(mAccelerometer != null)
                mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
            if(mGyroscope != null)
                mSensorManager.registerListener(this, mGyroscope, SensorManager.SENSOR_DELAY_GAME);
        } catch(Exception ex)
        {
            ex.printStackTrace();
        }

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        new LoggerTask().execute(event);  // Do not block: Logging data async
    }

    private class LoggerTask extends AsyncTask<SensorEvent, Void, Void> {
        @Override
        protected Void doInBackground(SensorEvent... events) {
            SensorEvent event = events[0];
            int iSensorType = event.sensor.getType();

            // Choose right file
            if(iSensorType == Sensor.TYPE_ACCELEROMETER)
                mLoggerAccelerometer.log(event.values[0], event.values[1], event.values[2]);
            else if(iSensorType == Sensor.TYPE_LINEAR_ACCELERATION)
                mLoggerLinAccelerometer.log(event.values[0], event.values[1], event.values[2]);
            else if(iSensorType == Sensor.TYPE_GYROSCOPE)
                mLoggerGyroscope.log(event.values[0], event.values[1], event.values[2]);

            return null;
        }
    }

    @Override
    public void onDestroy() {
        mSensorManager.unregisterListener(this);

        // Make sure data is written into files
        if(mLoggerAccelerometer != null)
            mLoggerAccelerometer.dump();
        if(mLoggerLinAccelerometer != null)
            mLoggerLinAccelerometer.dump();
        if(mLoggerGyroscope != null)
            mLoggerGyroscope.dump();
    }
}
