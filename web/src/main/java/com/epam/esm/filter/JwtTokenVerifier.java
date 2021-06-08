package com.epam.esm.filter;

import com.epam.esm.config.JwtConfig;
import com.epam.esm.exception.UntrustedTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class JwtTokenVerifier extends OncePerRequestFilter implements Filter {
    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = request.getHeader(jwtConfig.getAuthorizationHeader());

        if (StringUtils.isBlank(authorizationHeader) || !authorizationHeader.startsWith(jwtConfig.getTokenPrefix())) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = StringUtils.removeStart(authorizationHeader, jwtConfig.getTokenPrefix());

        try {
            Authentication authentication = parsenToAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            throw new UntrustedTokenException(String.format("Token %s cannot be trusted", token));
        }
    }

    private Authentication parsenToAuthentication(String token) {
        Jws<Claims> claimsJws = Jwts.parserBuilder()
                .setSigningKey(secretKey).build()
                .parseClaimsJws(token);

        Claims body = claimsJws.getBody();
        String username = body.getSubject();
        List<Map<String, String>> authorities = (List<Map<String, String>>) body.get("authorities");

        Set<SimpleGrantedAuthority> grantedAuthorities = authorities.stream()
                .map(map -> map.get("authority"))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        return new UsernamePasswordAuthenticationToken(username, null, grantedAuthorities);
    }
}
