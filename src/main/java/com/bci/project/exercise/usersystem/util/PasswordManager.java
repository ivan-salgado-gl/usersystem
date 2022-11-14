package com.bci.project.exercise.usersystem.util;

import com.bci.project.exercise.usersystem.enums.CodeType;
import com.bci.project.exercise.usersystem.exception.ApiException;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class PasswordManager {

    private final static String SHA_256 = "SHA-256";

    private PasswordManager() { }

    public static String encrypt(final String password) throws ApiException {
        log.info("Password encryption");
        try {
            final MessageDigest digest = MessageDigest.getInstance(SHA_256);
            final byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            log.error("Encryption failed. Cause: {}", e.getMessage());
            throw ErrorManager.createError(CodeType.ENCRYPTION_ERROR);
        }
    }

    private static String bytesToHex(final byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte h : hash) {
            String hex = Integer.toHexString(0xff & h);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

}
