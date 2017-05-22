package ru.sbt.ds;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;

import static java.nio.file.Files.readAllBytes;

@RestController
public class PhotoController {
    @RequestMapping(value = "/img", produces = "image/jpeg")
    public byte[] getById(long id) throws IOException {
        if ((id % 2) == 0 ) {
            return readAllBytes(new File("D:/Pictures/16502647f0b1.jpg").toPath());
        } else {
            return readAllBytes(new File("D:/Pictures/c479fde63de2.jpg").toPath());
        }
    }

    @RequestMapping(value = "/health")
    public int checkHealth() {
        return 200;
    }
}
