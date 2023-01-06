package org.standard.dreamcalendar.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.standard.dreamcalendar.config.type.TokenType;
import org.standard.dreamcalendar.config.type.TokenValidationType;
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
    private int accessTokenExpirationHours;
    @Value("${refresh-expiration-days}")
    private int refreshTokenExpirationDays;
    @Value("${refresh-days}")
    private int refreshDays;

    public final long ACCESS_EXPIRATION_MS = TimeUnit.HOURS.toMillis(accessTokenExpirationHours);
    public final long REFRESH_EXPIRATION_MS = TimeUnit.DAYS.toMillis(refreshTokenExpirationDays);

    public String generate(String claim, TokenType type) {

        Date now = new Date();
        Date expiration = null;
        String key = null;

        if (type == TokenType.AccessToken) {
            expiration = new Date(now.getTime() + ACCESS_EXPIRATION_MS);
            key = ACCESS_GENERATION_KEY;
        }

        if (type == TokenType.RefreshToken) {
            expiration = new Date(now.getTime() + REFRESH_EXPIRATION_MS);
            key = REFRESH_GENERATION_KEY;
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
                .compressWith(CompressionCodecs.GZIP)
                .compact()
                .replace("=", "");

        return jwt;

    }

    public TokenValidationResult validateToken(String token, TokenType type) {

        String key = null;

        if (type == TokenType.AccessToken)
            key = ACCESS_GENERATION_KEY;

        if (type == TokenType.RefreshToken)
            key = REFRESH_GENERATION_KEY;

        try {

            Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJwt(token).getBody();

            String email = claims.get("email").toString();

            Date now = new Date();
            Date expiration = claims.getExpiration();
            Long longExpiration = expiration.getTime();

            Date refreshDate = new Date(longExpiration - TimeUnit.DAYS.toMillis(refreshDays));

            if (type == TokenType.RefreshToken && refreshDate.before(now))
                return new TokenValidationResult(TokenValidationType.UPDATE, email);

            return new TokenValidationResult(TokenValidationType.OK, email) ;

        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
            return new TokenValidationResult(TokenValidationType.EXPIRED, null);
        } catch (Exception ex) {
            log.error("Invalid JWT");
        }

        return new TokenValidationResult(TokenValidationType.INVALID, null);

    }

}
