package com.epam.esm.config;

import com.epam.esm.model.User;
import com.epam.esm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserSecurity {
    private final UserService userService;

    public boolean hasSameId(Authentication authentication, Long userToAccessId) {
        String login = (String) authentication.getPrincipal();
        User authorizedUser = userService.findByLogin(login);
        return authorizedUser.getId().equals(userToAccessId);
    }
}
