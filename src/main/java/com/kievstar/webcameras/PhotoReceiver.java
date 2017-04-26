package com.kievstar.webcameras;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

public interface PhotoReceiver {

    void receiveImage(HttpServletRequest request) throws ServletException, java.io.IOException;

}
