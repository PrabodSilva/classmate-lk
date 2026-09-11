package lk.classmate.listing.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Component
public class JwtUtil {

    // MUST be exactly the same secret as auth-service's JwtUtil.
    // (Learning shortcut: in a real system this would come from an environment variable.)
    private static final String SECRET =
            "classmate-lk-super-secret-key-change-me-please-1234567890";

    private final SecretKey key =
            Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    /**
     * Reads "Authorization: Bearer <token>".
     * Returns everything inside the token (email, role, expiry) if it is real and not expired.
     * Returns null if there is no token, or it is fake/expired.
     */
    public Claims getClaims(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        String token = authHeader.substring(7).trim();
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }

    /** The student's or teacher's email, or null. */
    public String getEmailFromHeader(String authHeader) {
        Claims claims = getClaims(authHeader);
        return claims == null ? null : claims.getSubject();
    }

    /** "STUDENT" or "TEACHER", or null. */
    public String getRoleFromHeader(String authHeader) {
        Claims claims = getClaims(authHeader);
        return claims == null ? null : claims.get("role", String.class);
    }
}