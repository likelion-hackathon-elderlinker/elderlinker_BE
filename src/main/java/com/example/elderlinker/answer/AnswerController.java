package com.example.elderlinker.answer;

import java.security.Principal;

import com.example.elderlinker.User.UserService;
import com.example.elderlinker.question.QuestionDto;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;


import com.example.elderlinker.User.SiteUser;


import lombok.RequiredArgsConstructor;
@RequestMapping("/answer")
@RequiredArgsConstructor
@Controller
public class AnswerController {

    private final com.example.elderlinker.question.QuestionService questionService; // 질문 서비스
    private final AnswerService answerService; // 답변 서비스
    private final UserService userService; // 사용자 서비스

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String createAnswer(Model model, @PathVariable("id") Integer id,
                               @Valid AnswerForm answerForm, BindingResult bindingResult, Principal principal) {
        // 지정된 ID로부터 질문 정보 가져오기
        QuestionDto questionDto = this.questionService.getQuestion(id);
        // 현재 사용자 정보 가져오기
        SiteUser siteUser = this.userService.getUser(principal.getName());

        if (bindingResult.hasErrors()) {
            // 바인딩 오류가 있는 경우, 질문 정보를 모델에 추가하고 상세 페이지로 이동
            model.addAttribute("question", questionDto);
            return "question_detail";
        }

        // 답변 생성
        AnswerDto answerDto = this.answerService.create(questionDto,
                answerForm.getContent(), siteUser);

        // 답변이 생성된 질문의 상세 페이지로 리다이렉트
        return String.format("redirect:/question/detail/%s#answer_%s",
                answerDto.getQuestion().getId(), answerDto.getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String answerModify(AnswerForm answerForm, @PathVariable("id") Integer id, Principal principal) {
        // 지정된 ID로부터 답변 정보 가져오기
        AnswerDto answerDto = this.answerService.getAnswer(id);

        // 답변 작성자와 현재 로그인한 사용자 비교하여 수정권한 확인
        if (!answerDto.getAuthor().getUserName().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        // 답변 내용을 폼에 설정하여 수정 폼으로 이동
        answerForm.setContent(answerDto.getContent());
        return "answer_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String answerModify(@Valid AnswerForm answerForm, @PathVariable("id") Integer id,
                               BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "answer_form";
        }

        // 지정된 ID로부터 답변 정보 가져오기
        AnswerDto answerDto = this.answerService.getAnswer(id);

        // 답변 작성자와 현재 로그인한 사용자 비교하여 수정권한 확인
        if (!answerDto.getAuthor().getUserName().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        // 답변 수정
        this.answerService.modify(answerDto, answerForm.getContent());

        // 수정된 답변이 속한 질문의 상세 페이지로 리다이렉트
        return String.format("redirect:/question/detail/%s#answer_%s",
                answerDto.getQuestion().getId(), answerDto.getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String answerDelete(Principal principal, @PathVariable("id") Integer id) {
        // 지정된 ID로부터 답변 정보 가져오기
        AnswerDto answerDto = this.answerService.getAnswer(id);

        // 답변 작성자와 현재 로그인한 사용자 비교하여 삭제권한 확인
        if (!answerDto.getAuthor().getUserName().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }

        // 답변 삭제
        this.answerService.delete(answerDto);

        // 답변이 속한 질문의 상세 페이지로 리다이렉트
        return String.format("redirect:/question/detail/%s", answerDto.getQuestion().getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String answerVote(Principal principal, @PathVariable("id") Integer id) {
        // 지정된 ID로부터 답변 정보 가져오기
        AnswerDto answerDto = this.answerService.getAnswer(id);

        // 현재 로그인한 사용자 정보 가져오기
        SiteUser siteUser = this.userService.getUser(principal.getName());

        // 답변에 투표
        this.answerService.vote(answerDto, siteUser);

        // 투표된 답변이 속한 질문의 상세 페이지로 리다이렉트
        return String.format("redirect:/question/detail/%s#answer_%s",
                answerDto.getQuestion().getId(), answerDto.getId());
    }
}
