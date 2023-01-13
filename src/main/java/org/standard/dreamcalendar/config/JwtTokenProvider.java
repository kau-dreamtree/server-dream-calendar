package org.standard.dreamcalendar.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.standard.dreamcalendar.domain.user.type.TokenType;
import org.standard.dreamcalendar.domain.user.type.TokenValidationStatus;
import org.standard.dreamcalendar.domain.user.dto.TokenValidationResult;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

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

        long ACCESS_EXPIRATION_MS = TimeUnit.HOURS.toMillis(accessTokenExpirationHours);
        long REFRESH_EXPIRATION_MS = TimeUnit.DAYS.toMillis(refreshTokenExpirationDays);

        Date now = new Date();

        Date expiration;
        String key;

        switch (type) {

            case AccessToken:
                expiration = new Date(now.getTime() + ACCESS_EXPIRATION_MS);
                key = ACCESS_GENERATION_KEY;
                break;

            case RefreshToken:
                expiration = new Date(now.getTime() + REFRESH_EXPIRATION_MS);
                key = REFRESH_GENERATION_KEY;
                break;

            default:
                return null;

        }

        Header header = Jwts.header();
        Claims claims = Jwts.claims();
        SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));

        String subject = "authorization";

        header.put("typ", "JWT");
        header.put("alg", "HS256");

        claims.put("email", claim);

        String jwt = Jwts.builder()
                .setSubject(subject)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact()
                .replace("=", "");

        return jwt;

    }

    public TokenValidationResult validateToken(String token, TokenType type) {

        String key;

        switch (type) {

            case AccessToken:
                key = ACCESS_GENERATION_KEY;
                break;

            case RefreshToken:
                key = REFRESH_GENERATION_KEY;
                break;

            default:
                return null;

        }

        try {

            SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String email = claims.get("email").toString();

            Date now = new Date();
            Date refreshDate = getRefreshDate(claims.getExpiration().getTime());

            if (type == TokenType.RefreshToken && refreshDate.before(now)) {
                return new TokenValidationResult(TokenValidationStatus.UPDATE, email);
            }

            return new TokenValidationResult(TokenValidationStatus.OK, email) ;

        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token " + ex);
            return new TokenValidationResult(TokenValidationStatus.EXPIRED, null);
        } catch (Exception ex) {
            log.error("Invalid JWT " + ex);
        }

        return new TokenValidationResult(TokenValidationStatus.INVALID, null);

    }

    private Date getRefreshDate(Long expiration) {
        return new Date(expiration - TimeUnit.DAYS.toMillis(refreshDays));
    }

}