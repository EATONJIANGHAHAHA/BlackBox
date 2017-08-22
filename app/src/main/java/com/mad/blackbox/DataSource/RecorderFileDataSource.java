package com.mad.blackbox.DataSource;

import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * this class manages the cache generated by the recorder
 * Created by tomat on 2017-05-29.
 */

public class RecorderFileDataSource {

    private static final String TAG = "RecorderFileDataSource";
    public static final String LOCAL = "SoundMeter";
    public static final String LOCAL_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator;

    /**
     * record file path
     */
    public static final String REC_PATH = LOCAL_PATH + LOCAL + File.separator;



    /**
     * auto create file path in SD card
     */
    static {
        File dirRootFile = new File(LOCAL_PATH);
        if (!dirRootFile.exists()) {
            dirRootFile.mkdirs();
        }
        File recFile = new File(REC_PATH);
        if (!recFile.exists()) {
            recFile.mkdirs();
        }
    }

    private RecorderFileDataSource() {
    }

    /**
     * check if SD card has space
     *
     * @return
     */
    public static boolean isExitSDCard() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    private static boolean hasFile(String fileName) {
        File f = createFile(fileName);
        return null != f && f.exists();
    }

    /**
     * creates the record cache file
     * @param fileName
     * @return
     */
    public static File createFile(String fileName) {

        File myCaptureFile = new File(REC_PATH + fileName);
        if (myCaptureFile.exists()) {
            myCaptureFile.delete();
        }
        try {
            myCaptureFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myCaptureFile;
    }
}