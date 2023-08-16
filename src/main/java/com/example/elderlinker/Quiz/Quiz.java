package com.example.elderlinker.Quiz;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.List;

@Entity
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String question;
    private List<String> options;
    private int correctOptionIndex;

    // 생성자, 게터/세터 등 필요한 코드 추가

    // Getter, Setter, Constructors, and other methods
}
