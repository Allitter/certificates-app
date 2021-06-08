package com.epam.esm.model;

public enum UserPermission {
    CERTIFICATE_READ("certificate:read"),
    CERTIFICATE_CREATE("certificate:create"),
    CERTIFICATE_UPDATE("certificate:update"),
    CERTIFICATE_DELETE("certificate:delete"),

    TAG_READ("tag:read"),
    TAG_CREATE("tag:create"),
    TAG_UPDATE("tag:update"),
    TAG_DELETE("tag:delete"),

    PURCHASE_READ("purchase:read"),
    PURCHASE_CREATE("purchase:create"),
    PURCHASE_UPDATE("purchase:update"),
    PURCHASE_DELETE("purchase:delete"),

    USER_READ("user:read"),

    SIGNIN("signin"),
    SIGNUP("signup");

    private final String permission;

    UserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
