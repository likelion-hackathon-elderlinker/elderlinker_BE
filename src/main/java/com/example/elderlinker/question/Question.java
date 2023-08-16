package com.example.elderlinker.question;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.example.elderlinker.User.SiteUser;
import com.example.elderlinker.answer.Answer;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import com.example.elderlinker.answer.Answer;
import com.example.elderlinker.User.SiteUser;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 200)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Answer> answerList;
    
    @ManyToOne
    private SiteUser author;
    
    private LocalDateTime modifyDate;
    
    @ManyToMany
    Set<SiteUser> voter;

    /*
    * Question 엔티티 클래스는 데이터베이스의 질문 정보를 나타냅니다.
    id: 질문의 고유한 식별자입니다.
    subject: 질문의 제목입니다. 최대 200자까지 저장 가능합니다.
    content: 질문의 내용입니다. TEXT 형식으로 저장됩니다.
    createDate: 질문이 생성된 일시입니다.
    answerList: 해당 질문에 대한 답변들의 리스트입니다.
    author: 질문을 작성한 사용자를 나타냅니다.
    modifyDate: 질문이 수정된 일시입니다.
    voter: 질문에 투표한 사용자들의 집합입니다.*/
}