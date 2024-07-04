package ru.kata.spring.boot_security.demo.Util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordUtil {
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static boolean isPasswordEncoded(String password) {
        // Here we just check the length and the structure of bcrypt hashes
        return password != null && password.startsWith("$2a$");
    }

    public static String encodePassword(String password) {
        if (isPasswordEncoded(password)) {
            return password; // Return as is if already encoded
        }
        return passwordEncoder.encode(password);
    }
}
