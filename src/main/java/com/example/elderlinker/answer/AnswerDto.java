package com.example.elderlinker.answer;

import java.time.LocalDateTime;
import java.util.Set;

import com.example.elderlinker.question.QuestionDto;
import com.example.elderlinker.User.SiteUser;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerDto {
    private Integer id;
    private String content;
    private LocalDateTime createDate;
    private QuestionDto question;
    private SiteUser author;
    private LocalDateTime modifyDate;
    private Set<SiteUser> voter;
}
