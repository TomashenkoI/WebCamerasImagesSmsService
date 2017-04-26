package com.kievstar.webcameras;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface MmsSender {

    void service(HttpServletRequest request, HttpServletResponse response) throws IOException;

}
