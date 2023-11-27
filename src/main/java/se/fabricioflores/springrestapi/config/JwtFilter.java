package se.fabricioflores.springrestapi.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Autowired
    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            try {
                Claims claims = jwtUtil.validateToken(token.replace("Bearer ", ""));

                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    // ** Retrieve data from valid token
                    String username = claims.getSubject();
                    List<String> roles = claims.get("roles", List.class);

                    Collection<SimpleGrantedAuthority> authorities = roles
                            .stream()
                            .map(SimpleGrantedAuthority::new)
                            .toList();

                    // ** Credentials field is set to null since it's not needed for JWT based authentication
                    var auth = new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }

            } catch (JwtException e) {
                logger.error("Token auth failed, error: " + e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
}

