package com.example.demo.config;


import com.example.demo.common.JwtProperties;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@RequiredArgsConstructor
@Component
public class TokenHelper {

    @Value("${app.name}")
    private String appName;

    private final JwtProperties jwtProperties;

    private SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;

    public String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token, jwtProperties.getSecret());
        String username = claims.getSubject();
        return username;
    }

    public Claims getClaims(String token) {
        Claims claims = getClaimsFromToken(token, jwtProperties.getSecret());
        return claims;
    }

    public String generateToken(String username, String[] authorities) {
        Map<String, Object> claim = new HashMap<>();
        claim.put("iss", appName);
        claim.put("sub", username);
        claim.put("role", authorities);
        claim.put("exp", generateExpirationDate());
        claim.put("iat", generateCurrentDate());

        return Jwts.builder()
                .setClaims(claim)
                .signWith(SIGNATURE_ALGORITHM, jwtProperties.getSecret())
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtProperties.getSecret()).parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    public Claims getClaimsFromToken(String token, String signingKey) {
        try {
            return Jwts.parser()
                    .setSigningKey(signingKey)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    private long getCurrentTimeMillis() {
        return new DateTime().getMillis();
    }

    private Date generateCurrentDate() {
        return new Date(getCurrentTimeMillis());
    }

    private Date generateExpirationDate() {
        Integer expiresIn = jwtProperties.getExpiresIn();
        return new Date(getCurrentTimeMillis() + Duration.ofDays(expiresIn).toMillis());
    }
}
