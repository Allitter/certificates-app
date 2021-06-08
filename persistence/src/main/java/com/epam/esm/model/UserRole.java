package com.epam.esm.model;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.epam.esm.model.UserPermission.*;

public enum UserRole {
    USER(EnumSet.of(
            CERTIFICATE_READ,
            TAG_READ,
            PURCHASE_READ,
            USER_READ,
            PURCHASE_CREATE
    )),
    ADMIN(EnumSet.allOf(UserPermission.class));

    private final Set<UserPermission> permissions;

    UserRole(Set<UserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> authorities = permissions.stream()
                .map(UserPermission::getPermission)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + name()));

        return authorities;
    }
}
