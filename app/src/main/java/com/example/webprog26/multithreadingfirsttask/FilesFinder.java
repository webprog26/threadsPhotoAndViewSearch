package com.example.webprog26.multithreadingfirsttask;

import android.os.Environment;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by webprog26 on 31.10.2016.
 */

class FilesFinder {

    private static final String PHOTOS = "jpg";
    private static final String VIDEOS = "3gp";
    private static final String DECTINATION_FOLDER = "DCIM/Camera";

    /**
     * Checks is sd card available
     * @return sd card state
     */
    private static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * Gets path to sd card
     * @return sd card path String
     */
    private static File getStorageDir() {
        File sdCard = null;
        if (isExternalStorageReadable()) {
            sdCard = Environment.getExternalStorageDirectory();
        }
        return sdCard;
    }

    /**
     * Gets list of files on sd card
     * @return array File
     */
    static File[] getCameraFiles()
    {
        return new File(getStorageDir().getAbsolutePath(), DECTINATION_FOLDER).listFiles();
    }

    static ArrayList<String> getFilesPaths(File[] files)
    {
        ArrayList<String> filePathList = new ArrayList<>();
        for(File file: files)
        {
            if(isPhoto(file.getPath()) || isVideo(file.getPath())){
                filePathList.add(file.getPath());
            }
        }

        return filePathList;
    }

    /**
     * Checks file is photo
     * @param filePath {@link String}
     * @return isPhoto boolean
     */
    private static boolean isPhoto(String filePath){
        return filePath.substring(filePath.length() - 3).equals(PHOTOS);
    }

    /**
     * Checks file is video
     * @param filePath {@link String}
     * @return isVideo boolean
     */
    private static boolean isVideo(String filePath){
        return filePath.substring(filePath.length() - 3).equals(VIDEOS);
    }
}
