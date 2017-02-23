package com.sensorrecording;

import java.io.File;
import java.io.FileWriter;

public class Logger {
    private File mFile;
    private FileWriter mWriter;

    public Logger(String strPrefix)
    {
        try {
            String strFile = strPrefix + ".csv";

            // Create/Find directory
            File dir = new File(MainActivity.mStoragePath + "/SensorRecording/data");
            dir.mkdirs();

            // Create new file
            mFile = new File(dir, strFile);
            mWriter = new FileWriter(mFile);

            // Write header of .csv
            mWriter.write("t, x, y, z\n");
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public synchronized void log(float val1, float val2, float val3)
    {
        try {
            String strData = System.currentTimeMillis() + "," + val1 + "," + val2 + "," + val3 + "\n";
            mWriter.write(strData);
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public void dump()
    {
        try {
            mWriter.flush();
            mWriter.close();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
