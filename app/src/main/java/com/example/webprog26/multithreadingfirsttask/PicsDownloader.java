package com.example.webprog26.multithreadingfirsttask;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by webprog26 on 01.11.2016.
 */

public class PicsDownloader<T> extends HandlerThread {

    private static final String TAG = "PicsDownloader";

    private static final int PIC_UPLOAD = 0;
    private Handler mHandler;
    private Handler mResponseHandler;
    private Map<T, String> mPicRequestMap = Collections.synchronizedMap(new HashMap<T, String>());
    private Listener<T> mListener;


    public interface Listener<T>{
        void onPicUploaded(T token, Bitmap pic);
    }

    public void setListener(Listener<T> listener) {
        this.mListener = listener;
    }

    public PicsDownloader(Handler responseHandler) {
        super(TAG);
        this.mResponseHandler = responseHandler;
    }

    /**
     * Puts ImageView and filePath to the map, obtains and sends message to current Thread's Handler
     * @param token ImageView
     * @param filePath String
     */
    public void queuePic(T token, String filePath){
        Log.i(TAG, "filePath is: " + filePath);
        mPicRequestMap.put(token, filePath);

        mHandler.obtainMessage(PIC_UPLOAD, token).sendToTarget();
    }

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Log.i(TAG, "Thread.currentThread().getName(): " + Thread.currentThread().getName());
                if(msg.what == PIC_UPLOAD){
                    T token = (T) msg.obj;
                    Log.i(TAG, "received request for file " + mPicRequestMap.get(token));
                    handleRequest(token);
                }
            }
        };
    }

    /**
     * Loads thumbnail directly to ImageView, removes it from map
     * @param token ImageView
     */
    private void handleRequest(final T token)
    {
        final String filePath = mPicRequestMap.get(token);
        if(filePath == null) return;
        final Bitmap bitmap = BitmapUtils.getBitmap(filePath);
        Log.i(TAG, "Bitmap created");

        mResponseHandler.post(new Runnable() {
            @Override
            public void run() {
                if(mPicRequestMap.get(token) != filePath){
                    return;
                }
                mPicRequestMap.remove(token);
                mListener.onPicUploaded(token, bitmap);
            }
        });
    }

    /**
     * Clears messages queue
     */
    public void clearQueue() {
        mHandler.removeMessages(PIC_UPLOAD);
        mPicRequestMap.clear();
    }
}
