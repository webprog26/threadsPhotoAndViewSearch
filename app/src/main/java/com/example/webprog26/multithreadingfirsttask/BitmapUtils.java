package com.example.webprog26.multithreadingfirsttask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import java.io.File;

/**
 * Created by webprog26 on 31.10.2016.
 */

public class BitmapUtils {

    private static final int THUMB_WIDTH_AND_HEIGHT = 96;

    /**
     * Gets thumbnail preview from photo & video files
     * @param filePath
     * @return preview Bitmap
     */
    public static Bitmap getBitmap(String filePath) {

        File bitmapFile = new File(filePath);
        if(FilesFinder.isPhoto(filePath)){
            return ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(bitmapFile.getAbsolutePath()), THUMB_WIDTH_AND_HEIGHT, THUMB_WIDTH_AND_HEIGHT);
        }

        if(FilesFinder.isVideo(filePath))
        {
            return ThumbnailUtils.createVideoThumbnail(bitmapFile.getAbsolutePath(), MediaStore.Video.Thumbnails.MICRO_KIND);
        }
        return null;
    }
}