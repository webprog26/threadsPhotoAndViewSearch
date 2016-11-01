package com.example.webprog26.multithreadingfirsttask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import java.util.ArrayList;



public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity_TAG";
    private ArrayList<String> mItems;

    private GridView mGridView;
    private PicsDownloader<ImageView> mPicsDownloader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGridView = (GridView) findViewById(R.id.gridView);

        OlderTask olderTask = new OlderTask();
        olderTask.execute();

        mPicsDownloader = new PicsDownloader<>(new Handler());
        mPicsDownloader.setListener(new PicsDownloader.Listener<ImageView>() {
            @Override
            public void onPicUploaded(ImageView token, Bitmap pic) {
                token.setImageBitmap(pic);
            }
        });
        mPicsDownloader.start();
        mPicsDownloader.getLooper();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPicsDownloader.clearQueue();
        mPicsDownloader.quit();
    }

    private class OlderTask extends AsyncTask<Void, Void, ArrayList<String>>{
        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            return FilesFinder.getFilesPaths(FilesFinder.getCameraFiles());
        }

        @Override
        protected void onPostExecute(ArrayList<String> filesPath) {
            super.onPostExecute(filesPath);
            mItems = filesPath;
            Log.i(TAG, "mItems.size(): " + mItems.size());
            setupAdapter();
        }
    }

    /**
     * Sets adpter to GridView
     */
    private void setupAdapter(){
        if(mItems != null){
            mGridView.setAdapter(new GridItemsAdapter(mItems));
        } else {
            mGridView.setAdapter(null);
        }
    }

    private class GridItemsAdapter extends ArrayAdapter<String>{
        public GridItemsAdapter(ArrayList<String> filesPaths) {
            super(MainActivity.this, 0, filesPaths);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = MainActivity.this.getLayoutInflater().inflate(R.layout.grid_item, parent, false);
            }

            ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
            imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
            String filePath = getItem(position);
            mPicsDownloader.queuePic(imageView, filePath);
            return convertView;
        }
    }
}
