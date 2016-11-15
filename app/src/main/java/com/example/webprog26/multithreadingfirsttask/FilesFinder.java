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
    private static final String DECTINATION_FOLDER = "Camera";

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
            sdCard = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString());
        }
        return sdCard;
    }

    /**
     * Gets list of files on sd card
     * @return array File
     */
    static File[] getCameraFiles()
    {
        File destinationDir = null;
        for(File cameraDirectory: getStorageDir().listFiles())
        {
            if(cameraDirectory.isDirectory()){
                if(cameraDirectory.getName().equals(DECTINATION_FOLDER)){
                    destinationDir = cameraDirectory;
                    break;
                }
            }
        }
        return destinationDir.listFiles();
    }

    /**
     * Adds to ArrayList only photos/ vieos files and returns it
     * @param files {@link File[]}
     * @return {@link ArrayList<File>}
     */
    static ArrayList<String> getFilesPaths(File[] files)
    {
        if(files == null) return null;

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
