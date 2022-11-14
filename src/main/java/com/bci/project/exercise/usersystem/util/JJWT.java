package com.bci.project.exercise.usersystem.util;

import com.bci.project.exercise.usersystem.entity.User;
import com.bci.project.exercise.usersystem.enums.CodeType;
import com.bci.project.exercise.usersystem.exception.ApiException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class JJWT {

    @Value("${com.bci.project.expiration.time:60}")
    private Integer expirationTime;

    @Value("${com.bci.project.subject}")
    private String subject;

    @Value("${com.bci.project.secret}")
    private String secret;

    public String generateToken(final User user) {
        final Instant currentDate = Instant.now();
        final Map<String, Object> claims = this.getClaims(user);
        final Key key = getSecretKey();

        return Jwts.builder()
                .setId(user.getId().toString())
                .addClaims(claims)
                .setSubject(this.subject)
                .setIssuedAt(Date.from(currentDate))
                .setExpiration(Date.from(currentDate.plus(this.expirationTime, ChronoUnit.SECONDS)))
                .signWith(key)
                .compact();
    }

    public Optional<String> parseJwtAndGetMail(final String jwt) throws ApiException {
        Optional<String> email = Optional.empty();
        try {
            final Key key = getSecretKey();

            Jws<Claims> jws = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt);

            email = Optional.of((String) jws.getBody().get("email"));
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature. Cause: {}", ex.getMessage());
            throw ErrorManager.createError(CodeType.INVALID_JWT_SIGNATURE);
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token. Cause: {}", ex.getMessage());
            throw ErrorManager.createError(CodeType.INVALID_JWT_TOKEN);
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token. Cause: {}", ex.getMessage());
            throw ErrorManager.createError(CodeType.EXPIRED_JWT_TOKEN);
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token. Cause: {}", ex.getMessage());
            throw ErrorManager.createError(CodeType.UNSUPPORTED_JWT_TOKEN);
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty. Cause: {}", ex.getMessage());
            throw ErrorManager.createError(CodeType.ILEGAL_JWT_CLAIMS);
        }

        return email;
    }

    private Map<String, Object> getClaims(final User user) {
        final Map<String, Object> claims = new HashMap<>();

        if (user.getName() != null) {
            claims.put("name", user.getName());
        }
        claims.put("email", user.getEmail());

        return claims;
    }

    private Key getSecretKey() {
        return new SecretKeySpec(Base64.getDecoder().decode(this.secret), SignatureAlgorithm.HS256.getJcaName());
    }

}
