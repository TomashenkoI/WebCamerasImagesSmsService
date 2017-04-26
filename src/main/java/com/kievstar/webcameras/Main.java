package com.kievstar.webcameras;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import static spark.Spark.*;

@Component
public class Main {

    public static void main(String[] args) {

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("application-context");
        PhotoReceiver photoReceiver = (PhotoReceiver) applicationContext.getBean("photoReceiver");
        MmsSender mmsSender = (MmsSender) applicationContext.getBean("mmsSender");

        port(Integer.valueOf(System.getenv("PORT")));
        staticFileLocation("/public");

        post("/save_image", (request, response) -> {
            photoReceiver.receiveImage(request.raw());
            return null;
        });

        post("/get_image", (request, response) -> {
            mmsSender.service(request.raw(), response.raw());
            return null;
        });

    }

}
