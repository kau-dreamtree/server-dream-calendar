package org.standard.dreamcalendar.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.standard.dreamcalendar.domain.user.dto.TokenValidationResult;
import org.standard.dreamcalendar.domain.user.type.TokenType;
import org.standard.dreamcalendar.domain.user.type.TokenValidationStatus;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@NoArgsConstructor
@AllArgsConstructor
@Component
public class JwtProvider {

    @Value("${access-key}")
    private String ACCESS_GENERATION_KEY;
    @Value("${refresh-key}")
    private String REFRESH_GENERATION_KEY;
    @Value("${access-expiration-hours}")
    private long accessTokenExpirationHours;
    @Value("${refresh-expiration-days}")
    private long refreshTokenExpirationDays;

    public String generate(Long id, TokenType type) {

        Claims claims = Jwts.claims();
        SecretKey secretKey = getKey(type);
        Date expiration = getExpirationDate(type);

        claims.put("user_id", id);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(expiration)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact()
                .replace("=", "");
    }

    public String generate(Long id, TokenType type, String timeUnit, Long duration) {

        Claims claims = Jwts.claims();
        SecretKey secretKey = getKey(type);
        Date expiration = getCustomExpirationDate(getTimeUnit(timeUnit), duration);

        claims.put("user_id", id);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(expiration)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact()
                .replace("=", "");
    }

    public TokenValidationResult validateToken(String token, TokenType type) {
        try {
            return new TokenValidationResult(TokenValidationStatus.VALID, extractId(token, type));
        } catch (ExpiredJwtException e) {
            return new TokenValidationResult(TokenValidationStatus.EXPIRED, null);
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            return new TokenValidationResult(TokenValidationStatus.INVALID, null);
        }
    }

    private Long extractId(String token, TokenType type) {
        SecretKey secretKey = getKey(type);
        Claims claims = getClaims(secretKey, token);
        return claims.get("user_id", Long.class);
    }

    private SecretKey getKey(TokenType type) {
        switch (type) {
            case AccessToken:
                return Keys.hmacShaKeyFor(ACCESS_GENERATION_KEY.getBytes(StandardCharsets.UTF_8));
            case RefreshToken:
                return Keys.hmacShaKeyFor(REFRESH_GENERATION_KEY.getBytes(StandardCharsets.UTF_8));
            default:
                return null;
        }
    }

    private Date getExpirationDate(TokenType type) {

        Date now = new Date();

        switch (type) {
            case AccessToken:
                return new Date(now.getTime() + TimeUnit.HOURS.toMillis(accessTokenExpirationHours));
            case RefreshToken:
                return new Date(now.getTime() + TimeUnit.DAYS.toMillis(refreshTokenExpirationDays));
            default:
                return null;
        }

    }

    private TimeUnit getTimeUnit(String unit) {
        switch (unit) {
            case "millis":
                return TimeUnit.MILLISECONDS;
            case "minutes":
                return TimeUnit.MINUTES;
            case "hours":
                return TimeUnit.HOURS;
            case "days":
                return TimeUnit.DAYS;
            default:
                return TimeUnit.SECONDS;
        }
    }

    protected Date getCustomExpirationDate(TimeUnit timeUnit, long duration) {
        return new Date(System.currentTimeMillis() + timeUnit.toMillis(duration));
    }

    private Claims getClaims(SecretKey key, String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}