package com.mad.blackbox.Tool;

import android.media.MediaRecorder;

import java.io.File;
import java.io.IOException;

/**
 * this class manages the recorder
 * Created by tomat on 2017-05-29.
 */

public class MyMediaRecorderTool {
    public  File myRecAudioFile ;
    private MediaRecorder mMediaRecorder ;
    public boolean isRecording = false ;

    /**
     * this method is responsible for getting the max amplitude,
     * it analyzes a very short period of sound and get the average amplitude
     * @return
     */
    public float getMaxAmplitude() {
        if (mMediaRecorder != null) {
            try {
                return mMediaRecorder.getMaxAmplitude();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return 0;
            }
        } else {
            return 5;
        }
    }

    public File getMyRecAudioFile() {
        return myRecAudioFile;
    }

    public void setMyRecAudioFile(File myRecAudioFile) {
        this.myRecAudioFile = myRecAudioFile;
    }

    /**
     * recording
     * @return if successfully starts recording
     */
    public boolean startRecorder(){
        if (myRecAudioFile == null) {
            return false;
        }
        try {
            mMediaRecorder = new MediaRecorder();

            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mMediaRecorder.setOutputFile(myRecAudioFile.getAbsolutePath());

            mMediaRecorder.prepare();
            mMediaRecorder.start();
            isRecording = true;
            return true;
        } catch(IOException exception) {
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
            isRecording = false ;
            exception.printStackTrace();
        }catch(IllegalStateException e){
            stopRecording();
            e.printStackTrace();
            isRecording = false ;
        }
        return false;
    }

    /**
     * this method stops recording
     */
    public void stopRecording() {
        if (mMediaRecorder != null){
            if(isRecording){
                try{
                    mMediaRecorder.stop();
                    mMediaRecorder.release();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            mMediaRecorder = null;
            isRecording = false ;
        }
    }


    /**
     * this method will delete the cache file
     */
    public void delete() {
        stopRecording();
        if (myRecAudioFile != null) {
            myRecAudioFile.delete();
            myRecAudioFile = null;
        }
    }
}