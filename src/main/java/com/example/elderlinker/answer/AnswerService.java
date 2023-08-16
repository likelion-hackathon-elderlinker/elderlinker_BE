package com.example.elderlinker.answer;

import java.time.LocalDateTime;
import java.util.Optional;

import com.example.elderlinker.User.SiteUser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.example.elderlinker.DataNotFoundException;
import com.example.elderlinker.question.QuestionDto;
import com.example.elderlinker.User.SiteUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final ModelMapper modelMapper;

    private Answer of(AnswerDto answerDto) {
        return modelMapper.map(answerDto, Answer.class);
    }
    
    private AnswerDto of(Answer answer) {
        return modelMapper.map(answer, AnswerDto.class);
    }
    
    public AnswerDto create(QuestionDto questionDto, String content, SiteUser author) {
        AnswerDto answerDto = new AnswerDto();
        answerDto.setContent(content);
        answerDto.setCreateDate(LocalDateTime.now());
        answerDto.setQuestion(questionDto);
        answerDto.setAuthor(author);
        Answer answer = of(answerDto);
        answer = this.answerRepository.save(answer);
        answerDto.setId(answer.getId());
        return answerDto;
    }
    
    public AnswerDto getAnswer(Integer id) {
        Optional<Answer> answer = this.answerRepository.findById(id);
        if (answer.isPresent()) {
            return of(answer.get());
        } else {
            throw new DataNotFoundException("question not found");
        }
    }

    public AnswerDto modify(AnswerDto answerDto, String content) {
        answerDto.setContent(content);
        answerDto.setModifyDate(LocalDateTime.now());
        this.answerRepository.save(of(answerDto));
        return answerDto;
    }// 반환값 수정 필요
    
    public void delete(AnswerDto answerDto) {
        this.answerRepository.delete(of(answerDto));
    }
    
    public AnswerDto vote(AnswerDto answerDto, SiteUser siteUserDto) {
        answerDto.getVoter().add(siteUserDto);
        this.answerRepository.save(of(answerDto));
        return answerDto;
    }// 반환 값 수정 필요
    public String getUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;
    }
}