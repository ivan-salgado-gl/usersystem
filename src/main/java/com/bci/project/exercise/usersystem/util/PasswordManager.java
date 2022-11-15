package com.bci.project.exercise.usersystem.util;

import com.bci.project.exercise.usersystem.enums.CodeType;
import com.bci.project.exercise.usersystem.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;

@Slf4j
public class PasswordManager {

    private final static int LONG_ROUNDS = 12;

    private PasswordManager() { }

    public static String encrypt(final String password) throws ApiException {
        log.info("Password encryption");
        try {
            return BCrypt.hashpw(password, BCrypt.gensalt(LONG_ROUNDS));
        } catch (Exception e) {
            log.error("Encryption failed. Cause: {}", e.getMessage());
            throw ErrorManager.createError(CodeType.ENCRYPTION_ERROR);
        }
    }

}
