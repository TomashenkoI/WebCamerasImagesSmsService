package com.kievstar.webcameras.impl;

import com.kievstar.webcameras.CamerasImagesDao;
import com.kievstar.webcameras.MmsSender;
import com.twilio.sdk.verbs.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class MmsSenderImpl implements MmsSender {

    @Autowired
    private CamerasImagesDao camerasImagesDao;

    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        TwiMLResponse twiml = new TwiMLResponse();
        Message message = new Message();
        int cameraId = Integer.valueOf(request.getParameter("camera_id"));
        String lastImagePath = camerasImagesDao.getLastImagePath(cameraId);
        try {
            message.append(new Body(""));
            message.append(new Media(lastImagePath));
            twiml.append(message);
        } catch (TwiMLException e) {
            e.printStackTrace();
        }

        response.setContentType("application/xml");
        response.getWriter().print(twiml.toXML());
    }

    public void setCamerasImagesDao(CamerasImagesDao camerasImagesDao) {
        this.camerasImagesDao = camerasImagesDao;
    }
}
