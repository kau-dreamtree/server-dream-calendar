package org.standard.dreamcalendar.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.standard.dreamcalendar.domain.user.type.TokenType;
import org.standard.dreamcalendar.domain.user.type.TokenValidationStatus;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Component
public class JWTProvider {

    @Value("${access-key}")
    private String ACCESS_GENERATION_KEY;
    @Value("${refresh-key}")
    private String REFRESH_GENERATION_KEY;
    @Value("${access-expiration-hours}")
    private long accessTokenExpirationHours;
    @Value("${refresh-expiration-days}")
    private long refreshTokenExpirationDays;
    @Value("${refresh-days}")
    private int refreshDays;

    public String generate(String claim, TokenType type) {

        Header header = Jwts.header();
        Claims claims = Jwts.claims();
        String subject = "Authorization";
        SecretKey secretKey = getKey(type);
        Date expiration = getExpirationDate(type);

        header.put("typ", "JWT");
        header.put("alg", "HS256");

        claims.put("email", claim);

        return Jwts.builder()
                .setSubject(subject)
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(expiration)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact()
                .replace("=", "");

    }

    public String generateForExpirationTest(String claim, TokenType type, Long second) {

        Header header = Jwts.header();
        Claims claims = Jwts.claims();
        String subject = "Authorization";
        SecretKey secretKey = getKey(type);
        Date expiration = getCustomExpirationDate(second);

        header.put("typ", "JWT");
        header.put("alg", "HS256");

        claims.put("email", claim);

        return Jwts.builder()
                .setSubject(subject)
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(expiration)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact()
                .replace("=", "");

    }

    public TokenValidationStatus validateToken(String token, TokenType type) {

        try {
            SecretKey secretKey = getKey(type);
            Claims claims = getClaims(secretKey, token);
            Date refreshDate = getRefreshDate(claims.getExpiration().getTime());

            return (type == TokenType.RefreshToken && refreshDate.before(new Date())) ?
                    TokenValidationStatus.UPDATE :
                    TokenValidationStatus.VALID;
        } catch (ExpiredJwtException ex) {
            return TokenValidationStatus.EXPIRED;
        } catch (Exception ex) {
            return TokenValidationStatus.INVALID;
        }

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

    private Date getCustomExpirationDate(Long second) {
        return new Date(new Date().getTime() + TimeUnit.SECONDS.toMillis(second));
    }

    private Claims getClaims(SecretKey key, String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Date getRefreshDate(Long expiration) {
        return new Date(expiration - TimeUnit.DAYS.toMillis(refreshDays));
    }

}