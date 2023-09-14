package org.standard.dreamcalendar.global.util.token;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.standard.dreamcalendar.domain.user.dto.TokenValidationResult;
import org.standard.dreamcalendar.domain.user.enums.TokenValidationStatus;
import org.standard.dreamcalendar.global.util.Encryptor;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Component
public class AccessTokenProvider implements TokenProvider{

    @Value("${access-key}")
    private String ACCESS_GENERATION_KEY;
    @Value("${access-expiration-hours}")
    private long accessTokenExpirationHours;

    public String generate(Long id) throws Exception {
        return Jwts.builder()
                .claim("user_id", Encryptor.AES256Encode(String.valueOf(id)))
                .setIssuedAt(new Date())
                .setExpiration(getExpirationDate())
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact()
                .replace("=", "");
    }

    public String generate(Long id, String timeUnit, Long duration) throws Exception {
        return Jwts.builder()
                .claim("user_id", Encryptor.AES256Encode(String.valueOf(id)))
                .setIssuedAt(new Date())
                .setExpiration(getCustomExpirationDate(getTimeUnit(timeUnit), duration))
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact()
                .replace("=", "");
    }

    public TokenValidationResult validateToken(String token) {
        try {
            return new TokenValidationResult(TokenValidationStatus.VALID, extractId(token));
        } catch (ExpiredJwtException e) {
            return new TokenValidationResult(TokenValidationStatus.EXPIRED, null);
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            return new TokenValidationResult(TokenValidationStatus.INVALID, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Long extractId(String token) throws Exception {
        return Long.valueOf(Encryptor.AES256Decode(getClaims(getSecretKey(), token).get("user_id", String.class)));
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(ACCESS_GENERATION_KEY.getBytes(StandardCharsets.UTF_8));
    }

    private Date getExpirationDate() {
        return Date.from(Instant.now().plus(Duration.ofHours(accessTokenExpirationHours)));
    }

    private ChronoUnit getTimeUnit(String unit) {
        switch (unit) {
            case "millis":
                return ChronoUnit.MILLIS;
            case "minutes":
                return ChronoUnit.MINUTES;
            case "hours":
                return ChronoUnit.HOURS;
            case "days":
                return ChronoUnit.DAYS;
            default:
                return ChronoUnit.SECONDS;
        }
    }

    protected Date getCustomExpirationDate(ChronoUnit timeUnit, long duration) {
        return Date.from(Instant.now().plus(duration, timeUnit));
    }

    private Claims getClaims(SecretKey key, String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}