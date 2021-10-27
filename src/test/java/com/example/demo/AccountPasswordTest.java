package com.example.demo;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


public class AccountPasswordTest {

    @Test
    public void 암호화() {
        String encoded = new BCryptPasswordEncoder().encode("1111");
        System.out.println(encoded);
    }
}
