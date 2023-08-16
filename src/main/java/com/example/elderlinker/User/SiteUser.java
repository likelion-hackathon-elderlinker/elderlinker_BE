package com.example.elderlinker.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class SiteUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 엔터티의 고유 식별자(ID)를 나타내는 필드.

    @Column(unique = true)
    private String userID; // 사용자의 아이디를 나타내는 필드. 중복되지 않아야 함.

    private String password; // 사용자의 비밀번호를 나타내는 필드.

    private String phoneNumber; // 사용자의 전화번호를 나타내는 필드.

    private String userName; // 사용자의 이름을 나타내는 필드.
}
