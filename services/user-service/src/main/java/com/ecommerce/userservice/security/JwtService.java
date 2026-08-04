package com.ecommerce.userservice.security;

import com.ecommerce.userservice.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;


@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties jwtProperties;


    /**
     * Generates a JWT token for an authenticated user.
     *
     * @param email authenticated user's email
     * @return signed JWT token
     */

    public String generateToken(String email) {

        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(
                        new Date(
                                System.currentTimeMillis() + jwtProperties.expiration()
                        )
                )
                .signWith(getSigningKey())
                .compact();

    }

    /**
     * Returns username(email) from token.
     */
    public String extractUsername(String token) {
        return extractClaim(
                token,
                Claims::getSubject //claims -> claims.getSubject()
        );
    }

    /**
     * Generic method to extract any claim.
     */
    public <T> T extractClaim(
            String token,
            Function<Claims, T> claimsResolver
    ) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Returns expiration date.
     */
    public Date extractExpiration(String token) {
        return extractClaim(
                token,
                Claims::getExpiration
        );
    }

    /**
     * Checks whether token has expired.
     */
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }


    /**
     * Validates token.
     */
    public boolean isTokenValid(
            String token,
            UserDetails userDetails
    ) {
        String username = extractUsername(token);

        return username.equals(userDetails.getUsername())
        && !isTokenExpired(token);
    }


    /**
     * Reads all JWT claims.
     */
    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    private SecretKey getSigningKey() {

        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.secret());

        return Keys.hmacShaKeyFor(keyBytes);

    }

}
