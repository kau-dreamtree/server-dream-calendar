package org.standard.dreamcalendar.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${jwt-key}")
    private String jwtKey;

    public final long EXPIRATION_MS = TimeUnit.HOURS.toMillis(2);

    public String generate(String claim) {

        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_MS);

        return Jwts.builder()
                .setSubject(claim)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(Keys.hmacShaKeyFor(jwtKey.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();

    }

    public boolean validateToken(String jwt)  {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(jwtKey)
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

    public String getUserIdFromJwt(String jwt) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtKey)
                .build()
                .parseClaimsJwt(jwt)
                .getBody()
                .getSubject();
    }
}
