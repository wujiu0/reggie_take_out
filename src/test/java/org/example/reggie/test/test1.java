package org.example.reggie.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

@SpringBootTest
public class test1 {

    @Value("${reggie.path}")
    private String path;

    @Test
    public void defangIPaddr() {
        String address = "125.6.6.1";
        String[] strings = address.split("\\.");
        System.out.println(String.join("[.]", strings));
    }

    @Test
    public void pathTest() {
        System.out.println(new File(path).getAbsolutePath());
        String fileName = "hello.jpg";
        System.out.println(fileName.substring(fileName.lastIndexOf(".")));

    }

    @Test
    public void testMod() {
        System.out.println(-1%3);
    }

    @Test
    public void testReturn() {
        int head = 1;
        System.out.println(head = 2);
    }
}
