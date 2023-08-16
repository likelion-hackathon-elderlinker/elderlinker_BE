package com.example.elderlinker.question;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.elderlinker.DataNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import com.example.elderlinker.question.QuestionDto;
import com.example.elderlinker.question.*;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.elderlinker.answer.Answer;
import com.example.elderlinker.User.SiteUser;
import com.example.elderlinker.User.SiteUser;

import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@Service
public class QuestionService {

    private final com.example.elderlinker.question.QuestionRepository questionRepository;

    // 검색 조건에 대한 Specification을 생성하는 메소드
    private Specification<Question> search(String kw) {
        return new Specification<Question>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거
                // 질문 작성자와 답변 작성자와의 조인을 수행
                Join<Question, SiteUser> u1 = q.join("author", JoinType.LEFT);
                Join<Question, Answer> a = q.join("answerList", JoinType.LEFT);
                Join<Answer, SiteUser> u2 = a.join("author", JoinType.LEFT);
                return cb.or(cb.like(q.get("subject"), "%" + kw + "%"), // 제목
                        cb.like(q.get("content"), "%" + kw + "%"),      // 내용
                        cb.like(u1.get("username"), "%" + kw + "%"),    // 질문 작성자
                        cb.like(a.get("content"), "%" + kw + "%"),      // 답변 내용
                        cb.like(u2.get("username"), "%" + kw + "%"));   // 답변 작성자
            }
        };
    }

    private final ModelMapper modelMapper;

    // Question을 QuestionDto로 변환하는 메소드
    private QuestionDto of(Question question) {
        return modelMapper.map(question, QuestionDto.class);
    }

    // QuestionDto를 Question으로 변환하는 메소드
    private Question of(QuestionDto questionDto) {
        return modelMapper.map(questionDto, Question.class);
    }

    // 페이징된 질문 목록을 가져오는 메소드
    public Page<QuestionDto> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Specification<Question> spec = search(kw);
        Page<Question> questionList = this.questionRepository.findAll(spec, pageable);
        Page<QuestionDto> questionDtoList = questionList.map(q -> of(q));
        return questionDtoList;
    }

    // 특정 id의 질문을 가져오는 메소드
    public QuestionDto getQuestion(Integer id) {
        Optional<Question> question = this.questionRepository.findById(id);
        if (question.isPresent()) {
            return of(question.get());
        } else {
            throw new DataNotFoundException("question not found");
        }
    }

    // 질문을 생성하는 메소드
    public QuestionDto create(String subject, String content, SiteUser user) {
        QuestionDto questionDto = new QuestionDto();
        questionDto.setSubject(subject);
        questionDto.setContent(content);
        questionDto.setCreateDate(LocalDateTime.now());
        questionDto.setAuthor(user);
        Question question = of(questionDto);
        this.questionRepository.save(question);
        return questionDto;
    }

    // 질문을 수정하는 메소드
    public QuestionDto modify(QuestionDto questionDto, String subject, String content) {
        questionDto.setSubject(subject);
        questionDto.setContent(content);
        questionDto.setModifyDate(LocalDateTime.now());
        Question question = of(questionDto);
        this.questionRepository.save(question);
        return questionDto;
    }

    // 질문을 삭제하는 메소드
    public void delete(QuestionDto questionDto) {
        this.questionRepository.delete(of(questionDto));
    }

    // 질문에 투표하는 메소드
    public QuestionDto vote(QuestionDto questionDto, SiteUser siteUserDto) {
        questionDto.getVoter().add(siteUserDto);
        this.questionRepository.save(of(questionDto));
        return questionDto;
    }
}
