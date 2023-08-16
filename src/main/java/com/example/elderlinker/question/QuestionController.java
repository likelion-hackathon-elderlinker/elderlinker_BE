package com.example.elderlinker.question;

import java.security.Principal;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.example.elderlinker.answer.AnswerForm;
import com.example.elderlinker.User.SiteUser;
import com.example.elderlinker.User.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
/*
    QuestionController 클래스는 질문 관련 웹 페이지와 기능을 처리합니다.
    list: 질문 목록 조회 페이지로 이동하고, 페이징 및 검색 기능을 제공합니다.
    detail: 특정 질문의 상세 페이지로 이동하고, 해당 질문의 답변 정보도 보여줍니다.
    questionCreate: 새 질문을 작성하는 페이지로 이동합니다.
    questionModify: 기존 질문을 수정하는 페이지로 이동합니다.
    questionDelete: 질문을 삭제합니다.
    questionVote: 질문에 투표합니다.
* */
@Slf4j
@RequestMapping("/question")
@RequiredArgsConstructor
@Controller
public class QuestionController {

    private final com.example.elderlinker.question.QuestionService questionService;
    private final UserService userService;

    @RequestMapping("/list")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "kw", defaultValue = "") String kw) {
        log.info("page:{}, kw:{}", page, kw);
        Page<com.example.elderlinker.question.QuestionDto> paging = this.questionService.getList(page, kw);
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
        return "question_list";
    }

    @RequestMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm) {
        com.example.elderlinker.question.QuestionDto question = this.questionService.getQuestion(id);
        model.addAttribute("question", question);
        return "question_detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String questionCreate(com.example.elderlinker.question.QuestionForm questionForm) {
        return "question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String questionCreate(@Valid com.example.elderlinker.question.QuestionForm questionForm,
            BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.questionService.create(questionForm.getSubject(), questionForm.getContent(), siteUser);
        return "redirect:/question/list";
    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String questionModify(com.example.elderlinker.question.QuestionForm questionForm, @PathVariable("id") Integer id, Principal principal) {
        com.example.elderlinker.question.QuestionDto questionDto = this.questionService.getQuestion(id);
        if(!questionDto.getAuthor().getUserName().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        questionForm.setSubject(questionDto.getSubject());
        questionForm.setContent(questionDto.getContent());
        return "question_form";
    }
    
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String questionModify(@Valid com.example.elderlinker.question.QuestionForm questionForm, BindingResult bindingResult,
                                 Principal principal, @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        com.example.elderlinker.question.QuestionDto questionDto = this.questionService.getQuestion(id);
        if (!questionDto.getAuthor().getUserName().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.questionService.modify(questionDto, questionForm.getSubject(), questionForm.getContent());
        return String.format("redirect:/question/detail/%s", id);
    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String questionDelete(Principal principal, @PathVariable("id") Integer id) {
        com.example.elderlinker.question.QuestionDto questionDto = this.questionService.getQuestion(id);
        if (!questionDto.getAuthor().getUserName().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.questionService.delete(questionDto);
        return "redirect:/";
    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String questionVote(Principal principal, @PathVariable("id") Integer id) {
        com.example.elderlinker.question.QuestionDto questionDto = this.questionService.getQuestion(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.questionService.vote(questionDto, siteUser);
        return String.format("redirect:/question/detail/%s", id);
    }
}