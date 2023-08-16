package com.example.elderlinker.User;

import lombok.Getter;

@Getter
public enum Userrole {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private final String value;
    // Userrole 열거형의 생성자
    Userrole(String value) {
        this.value = value;
    }
}
