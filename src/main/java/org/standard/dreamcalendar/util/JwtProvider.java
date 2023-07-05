package org.standard.dreamcalendar.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
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

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtProvider {

    @Value("${access-key}")
    private final String ACCESS_GENERATION_KEY;
    @Value("${refresh-key}")
    private final String REFRESH_GENERATION_KEY;
    @Value("${access-expiration-hours}")
    private final long accessTokenExpirationHours;
    @Value("${refresh-expiration-days}")
    private final long refreshTokenExpirationDays;

    public String generate(Long id, TokenType type) {

        Header header = Jwts.header();
        Claims claims = Jwts.claims();
        String subject = "Authorization";
        SecretKey secretKey = getKey(type);
        Date expiration = getExpirationDate(type);

        header.put("typ", "JWT");
        header.put("alg", "HS256");

        claims.put("user_id", id);

        return Jwts.builder()
                .setSubject(subject)
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
            return new TokenValidationResult(TokenValidationStatus.EXPIRED, extractId(token, type));
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            return new TokenValidationResult(TokenValidationStatus.INVALID, null);
        }
    }

    public Long extractId(String token, TokenType type) {
        SecretKey secretKey = getKey(type);
        Claims claims = getClaims(secretKey, token);
        return claims.get("user_id", Long.class);
    }

    public String generateForExpirationTest(long userId, String timeUnit, long duration, TokenType type) {

        Header header = Jwts.header();
        Claims claims = Jwts.claims();
        String subject = "Authorization";
        SecretKey secretKey = getKey(type);
        Date expiration = getCustomExpirationDate(getTimeUnit(timeUnit), duration);

        header.put("typ", "JWT");
        header.put("alg", "HS256");

        claims.put("user_id", userId);

        return Jwts.builder()
                .setSubject(subject)
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(expiration)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact()
                .replace("=", "");

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
            case "seconds":
                return TimeUnit.SECONDS;
            case "minutes":
                return TimeUnit.MINUTES;
            case "hours":
                return TimeUnit.HOURS;
            case "days":
                return TimeUnit.DAYS;
            default:
                return null;
        }
    }

    private Date getCustomExpirationDate(TimeUnit timeUnit, long duration) {
        return new Date(new Date().getTime() + timeUnit.convert(duration, timeUnit));
    }

    private Claims getClaims(SecretKey key, String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}