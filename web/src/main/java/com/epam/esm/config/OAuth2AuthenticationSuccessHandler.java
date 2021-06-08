package com.epam.esm.config;

import com.epam.esm.model.User;
import com.epam.esm.service.UserService;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final UserService userService;
    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        DefaultOidcUser principal = (DefaultOidcUser) authentication.getPrincipal();
        String provider = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();
        Map<String, Object> claims = principal.getClaims();
        String email = (String) claims.get("email");
        String name = (String) claims.get("name");

        User user;
        if (userService.userExists(email)) {
            user = userService.findByLogin(email);
        } else {
            user = User.builder()
                    .login(email)
                    .name(name)
                    .password(UUID.randomUUID().toString())
                    .authenticationProvider(provider)
                    .build();
            userService.create(user);
        }

        String token = buildToken(user.getLogin(), user.getAuthorities());
        response.addHeader(jwtConfig.getAuthorizationHeader(), jwtConfig.getTokenPrefix() + token);
    }

    private String buildToken(String login, Set<? extends GrantedAuthority> authorities) {
        LocalDate expirationDate = LocalDate.now().plusDays(jwtConfig.getTokenExpirationDays());
        java.sql.Date expirationTime = java.sql.Date.valueOf(expirationDate);

        return Jwts.builder()
                .setSubject(login)
                .claim("authorities", authorities)
                .setIssuedAt(new Date())
                .setExpiration(expirationTime)
                .signWith(secretKey)
                .compact();
    }
}
