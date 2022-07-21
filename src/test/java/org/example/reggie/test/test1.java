package org.example.reggie.test;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class test1 {

    @Test
    public void defangIPaddr() {
        String address = "125.6.6.1";
        String[] strings = address.split("\\.");
        System.out.println(String.join("[.]", strings));
    }
}
