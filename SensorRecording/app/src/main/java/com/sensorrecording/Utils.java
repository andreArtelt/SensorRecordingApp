package com.sensorrecording;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Environment;

public class Utils {
    // Check if we can write on sd-card (most common type of external storage)
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();

        return Environment.MEDIA_MOUNTED.equals(state);
    }

    // Check if an sensor is available (NOTE: getSystemService can be called from Context only!)

    public static boolean checkForLinearAccelerometer(Context context) {
        return ((SensorManager) context.getSystemService(Context.SENSOR_SERVICE)).getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION) != null;
    }

    public static boolean checkForGyroscope(Context context) {
        return ((SensorManager) context.getSystemService(Context.SENSOR_SERVICE)).getDefaultSensor(Sensor.TYPE_GYROSCOPE) != null;
    }

    public static boolean checkForAccelerometer(Context context) {
        return ((SensorManager) context.getSystemService(Context.SENSOR_SERVICE)).getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null;
    }
}
