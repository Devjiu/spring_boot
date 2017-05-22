package ru.sbt.ds;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ThreadLocalRandom;

@RestController
public class PhotoClientController {
    @Autowired
    private PhotoServiceClient photoServiceClient;

    @RequestMapping(value = "/getPicture", produces = "image/jpeg")
    public byte[] getPicture() {
        return photoServiceClient.getPhoto( ThreadLocalRandom.current().nextInt(1,3) );
    }

    @RequestMapping(value = "/health")
    public int checkHealth() {
        return photoServiceClient.checkHealth();
    }
}
