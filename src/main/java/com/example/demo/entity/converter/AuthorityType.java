package com.example.demo.entity.converter;

import lombok.Getter;

public enum AuthorityType {

    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_USER("ROLE_USER");

    @Getter
    private String roleName;

    AuthorityType(String roleName) {
        this.roleName = roleName;
    }
}