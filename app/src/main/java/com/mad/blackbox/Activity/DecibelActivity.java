package com.mad.blackbox.Activity;
/**
 * this class manage and user interface, link the user interface back to the data and
 * manages the custome tools .
 */

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.mad.blackbox.DataSource.RecorderFileDataSource;
import com.mad.blackbox.R;
import com.mad.blackbox.Tool.MyMediaRecorderTool;
import com.mad.blackbox.Util.PointerAdjustUtil;
import com.mad.blackbox.View.SoundDiscView;

import java.io.File;

public class DecibelActivity extends AppCompatActivity {

    float mVolumeflt = 10000;
    private SoundDiscView mSoundDiscView;
    private MyMediaRecorderTool mRecorder;
    private static final int msgWhat = 0x1001;
    private static final int refreshTime = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decibel);
        mRecorder = new MyMediaRecorderTool();
    }


    /**
     * defines a handler to handle the change of data from the sensor.
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (this.hasMessages(msgWhat)) {
                return;
            }
            mVolumeflt = mRecorder.getMaxAmplitude();  //get sound pressure
            if(mVolumeflt > 0 && mVolumeflt < 1000000) {
                PointerAdjustUtil.setDbCount(20 * (float)(Math.log10(mVolumeflt)));  //parse sound pressure to decibel
                mSoundDiscView.refresh();
            }
            handler.sendEmptyMessageDelayed(msgWhat, refreshTime);
        }
    };

    /**
     * send a message to the handleer.
     */
    private void startListenAudio() {
        handler.sendEmptyMessageDelayed(msgWhat, refreshTime);
    }

    /**
     * start record
     * @param fFile
     */
    public void startRecord(File fFile){
        try{
            mRecorder.setMyRecAudioFile(fFile);
            if (mRecorder.startRecorder()) {
                startListenAudio();
            }else{
                Toast.makeText(this, "failed start recording", Toast.LENGTH_SHORT).show();
            }
        }catch(Exception e){
            Toast.makeText(this, "recorder is been used by other application or permission not granted ", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /**
     * this creates the file and start record.
     */
    @Override
    protected void onResume() {
        super.onResume();
        mSoundDiscView = (SoundDiscView) findViewById(R.id.soundDiscView);
        File file = RecorderFileDataSource.createFile("temp.amr");
        if (file != null) {
            Log.v("file", "file =" + file.getAbsolutePath());
            startRecord(file);
        } else {
            Toast.makeText(getApplicationContext(), "create file failed", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * stop recording
     */
    @Override
    protected void onPause() {
        super.onPause();
        mRecorder.delete(); //delete record cache file
        handler.removeMessages(msgWhat);
    }

    /**
     * delete the cache file
     */
    @Override
    protected void onDestroy() {
        handler.removeMessages(msgWhat);
        mRecorder.delete();
        super.onDestroy();
    }
}
