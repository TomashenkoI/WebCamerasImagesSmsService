package com.kievstar.webcameras.impl;

import com.kievstar.webcameras.PhotoReceiver;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Iterator;
import java.util.List;

@Component
public class PhotoReceiverImpl implements PhotoReceiver {

    @Autowired
    private CamerasImagesDaoImpl camerasImagesDao;

    private String filePath = "d://";
    private int maxFileSize = 100 * 1024;
    private int maxMemSize = 4 * 1024;
    private File file;

    public void receiveImage(HttpServletRequest request) throws ServletException, java.io.IOException {

        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(maxMemSize);
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setSizeMax(maxFileSize);

        try{
            List fileItems = upload.parseRequest(request);
            Iterator i = fileItems.iterator();

            while (i.hasNext()) {
                FileItem fi = (FileItem)i.next();
                if (!fi.isFormField()) {
                    String fileName = fi.getName();
                    filePath = filePath + fileName;
                    file = new File(filePath);
                    fi.write(file) ;
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }

        camerasImagesDao.saveImage(filePath, Integer.parseInt(request.getParameter("cameraId")));

    }

    public void setCamerasImagesDao(CamerasImagesDaoImpl camerasImagesDao) {
        this.camerasImagesDao = camerasImagesDao;
    }

}
