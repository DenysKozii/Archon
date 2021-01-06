package com.company.archon.controllers;

import com.company.archon.dto.ParameterDto;
import com.company.archon.dto.QuestionDto;
import com.company.archon.dto.QuestionParameterDto;
import com.company.archon.entity.Parameter;
import com.company.archon.entity.Question;
import com.company.archon.entity.QuestionParameter;
import com.company.archon.entity.User;
import com.company.archon.services.ParameterService;
import com.company.archon.services.QuestionParameterService;
import com.company.archon.services.QuestionService;
import liquibase.pro.packaged.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/question")
public class QuestionController {

    private final QuestionService questionService;
    private final QuestionParameterService questionParameterService;
    private final ParameterService parameterService;

    @Autowired
    public QuestionController(QuestionService questionService, QuestionParameterService questionParameterService, ParameterService parameterService) {
        this.questionService = questionService;
        this.questionParameterService = questionParameterService;
        this.parameterService = parameterService;
    }

    @GetMapping("/new/{gamePatternId}")
    public String newQuestion(@PathVariable Long gamePatternId,
                              @AuthenticationPrincipal User user, Model model) {
        QuestionDto question = questionService.createNewQuestion(gamePatternId);
        model.addAttribute("gamePatternId",gamePatternId);
        model.addAttribute("id", question.getId());
        return "question/questionCreator";
    }

    @PostMapping("/delete/{gamePatternId}/{questionId}")
    public String deleteQuestion(@PathVariable Long gamePatternId,
                                 @PathVariable Long questionId,
                                 @AuthenticationPrincipal User user, Model model) {
        questionService.deleteQuestion(questionId);
        return "redirect:/question/list/" + gamePatternId;
    }

    @PostMapping("/new/{gamePatternId}/{questionId}")
    public String newQuestion(@PathVariable Long gamePatternId,
                              @PathVariable Long questionId,
                              @RequestParam String title,
                              @RequestParam String context,
                              @RequestParam Integer weight,
//                              @RequestParam("fileImage") MultipartFile multipartFile,
                              @AuthenticationPrincipal User user, Model model) throws IOException {
        questionService.updateQuestion(gamePatternId, questionId,
                title,
                context,
                weight,
                null);
        List<QuestionParameterDto> questionParameters = questionParameterService.getParametersByQuestionId(questionId);
        model.addAttribute("questionId", questionId);
        model.addAttribute("gamePatternId", gamePatternId);
        model.addAttribute("parameters", questionParameters);
        return "parameter/questionParametersList";
//        return "answer/answersCreator";
    }

    @GetMapping("/list/{gamePatternId}")
    public String questionsList(@PathVariable Long gamePatternId, @AuthenticationPrincipal User user, Model model) {
        List<QuestionDto> questions = questionService.getQuestionsByGamePatternId(gamePatternId);
        model.addAttribute("questions", questions);
        model.addAttribute("gamePatternId",gamePatternId);
        return "question/questionsList";
    }

    @GetMapping("/relativeQuestions/{gamePatternId}/{questionId}")
    public String relativeQuestionsList(@PathVariable Long questionId, @PathVariable Long gamePatternId, @AuthenticationPrincipal User user, Model model) {
        List<QuestionDto> questions = questionService.getQuestionsByGamePatternId(gamePatternId,questionId);
        model.addAttribute("questions", questions);
        model.addAttribute("gamePatternId",gamePatternId);
        return "question/relativeQuestionsList";
    }

    @GetMapping("/addRelativeQuestion/{gamePatternId}/{questionId}/{relativeId}")
    public String addRelativeQuestion(@PathVariable Long questionId,@PathVariable Long relativeId, @PathVariable Long gamePatternId, @AuthenticationPrincipal User user, Model model) {
        List<QuestionDto> questions = questionService.addRelativeQuestion(questionId, relativeId,gamePatternId);
        model.addAttribute("questions", questions);
        model.addAttribute("gamePatternId",gamePatternId);
        return "question/relativeQuestionsList";
    }
}
