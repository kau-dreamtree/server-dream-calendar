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

    @Value("${access-key}")
    private String ACCESS_GENERATION_KEY;
    @Value("${refresh-key}")
    private String REFRESH_GENERATION_KEY;

    public final long ACCESS_EXPIRATION_MS = TimeUnit.HOURS.toMillis(2);
    public final long REFRESH_EXPIRATION_MS = TimeUnit.DAYS.toMillis(14);

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

        return Jwts.builder()
                .setSubject(claim)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();

    }

    public TokenValidationResult validateToken(String token, TokenType type)  {

        String key = null;

        if (type == TokenType.AccessToken)
            key = ACCESS_GENERATION_KEY;

        if (type == TokenType.RefreshToken)
            key = REFRESH_GENERATION_KEY;

        try {

            Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJwt(token).getBody();

            Date now = new Date();
            Date expiration = claims.getExpiration();
            Long longExpiration = expiration.getTime();

            Date refreshDate = new Date(longExpiration - TimeUnit.DAYS.toMillis(2));

            if (type == TokenType.RefreshToken && refreshDate.before(now))
                return new TokenValidationResult(TokenValidationType.UPDATE, null);

            Integer userId = Integer.valueOf(claims.getSubject());

            return new TokenValidationResult(TokenValidationType.OK, userId);

        } catch (ExpiredJwtException ex) {

            log.error("Expired JWT token");
            return new TokenValidationResult(TokenValidationType.EXPIRED, null);

        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }

        return new TokenValidationResult(TokenValidationType.INVALID, null);
    }

}
