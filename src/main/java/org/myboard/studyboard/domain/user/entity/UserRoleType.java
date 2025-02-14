package org.myboard.studyboard.domain.user.entity;

import lombok.Getter;

@Getter
public enum UserRoleType {

    ADMIN("관리자"),
    USER("사용자");

    private final String description;

    UserRoleType(String description) {
        this.description = description;
    }
}

