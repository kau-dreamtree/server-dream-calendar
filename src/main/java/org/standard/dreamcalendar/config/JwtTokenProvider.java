package org.standard.dreamcalendar.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
public class JwtTokenProvider {

    private static final String KEY = "SECRETKEY";

    public static final long EXPIRATION_MS = TimeUnit.HOURS.toMillis(2);

    public static String generate(String claim) {

        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_MS);

        return Jwts.builder()
                .setSubject(claim)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512, KEY)
                .compact();
    }

    public static boolean validateToken(String jwt)  {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(KEY)
                    .build()
                    .parseClaimsJwt(jwt);
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }

    public static String getUserIdFromJwt(String jwt) {
        return Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJwt(jwt)
                .getBody()
                .getSubject();
    }
}
