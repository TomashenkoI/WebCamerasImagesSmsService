package com.kievstar.webcameras;

public interface CamerasImagesDao {

    void saveImage(String filePath, int cameraId);

    String getLastImagePath(int cameraId);

}
