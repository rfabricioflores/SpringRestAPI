package se.fabricioflores.springrestapi.config;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtil {

    private final String jwtSecret;

    @Autowired
    public JwtUtil(Environment environment) {
        String jwtSecret = environment.getProperty("JWT_SECRET", String.class);

        if (jwtSecret == null || jwtSecret.isEmpty())
            throw new RuntimeException("Missing JWT_SECRET environment variable");

        this.jwtSecret = jwtSecret;
    }

    // ** Generates token with username and roles as data
    public String generateToken(Authentication authentication) {

        List<String> roles = authentication
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts
                .builder()
                .claim("roles", roles)
                .setSubject(authentication.getName())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5)))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    // ** Validates token and returns its claims (data)
    public Claims validateToken(String token) throws ExpiredJwtException, UnsupportedJwtException,
            MalformedJwtException, SignatureException, IllegalArgumentException {
        return Jwts
                .parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
    }
}
