package ru.kata.spring.boot_security.demo.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordUtil {
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static boolean isPasswordEncoded(String password) {
        return password != null && password.startsWith("$2a$");
    }

    public static String encodePassword(String password) {
        if (isPasswordEncoded(password)) {
            return password;
        }
        return passwordEncoder.encode(password);
    }
}
