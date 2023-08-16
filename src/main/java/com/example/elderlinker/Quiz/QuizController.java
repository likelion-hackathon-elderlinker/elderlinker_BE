package com.example.elderlinker.Quiz;

import com.example.elderlinker.Quiz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/quizzes")
public class QuizController {
    @Autowired
    private QuizRepository quizRepository;

    @GetMapping
    public String listQuizzes(Model model) {
        Iterable<Quiz> quizzes = quizRepository.findAll();
        model.addAttribute("quizzes", quizzes);
        return "quiz/list";
    }

    @GetMapping("/{id}")
    public String viewQuiz(@PathVariable Long id, Model model) {
        Quiz quiz = quizRepository.findById(id).orElse(null);
        model.addAttribute("quiz", quiz);
        return "quiz/view";
    }
}
