package com.example.elderlinker.question;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.example.elderlinker.answer.AnswerDto;
import com.example.elderlinker.User.SiteUser;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionDto {
    private Integer id;
    private String subject;
    private String content;
    private LocalDateTime createDate;
    private List<AnswerDto> answerList;
    private SiteUser author;
    private LocalDateTime modifyDate;
    private Set<SiteUser> voter;
}
