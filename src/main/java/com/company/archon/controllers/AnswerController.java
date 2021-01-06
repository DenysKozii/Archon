package com.company.archon.controllers;

import com.company.archon.dto.AnswerDto;
import com.company.archon.dto.AnswerParameterDto;
import com.company.archon.dto.ParameterDto;
import com.company.archon.dto.QuestionParameterDto;
import com.company.archon.entity.Parameter;
import com.company.archon.entity.User;
import com.company.archon.services.AnswerParameterService;
import com.company.archon.services.AnswerService;
import com.company.archon.services.ParameterService;
import com.company.archon.services.QuestionParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/answer")
public class AnswerController {
    private final AnswerService answerService;
    private final ParameterService parameterService;
    private final AnswerParameterService answerParameterService;

    @Autowired
    public AnswerController(AnswerService answerService, ParameterService parameterService, AnswerParameterService answerParameterService) {
        this.answerService = answerService;
        this.parameterService = parameterService;
        this.answerParameterService = answerParameterService;
    }

    @GetMapping("/new/{gamePatternId}/{questionId}")
    public String answersList(@PathVariable Long gamePatternId,
                              @PathVariable Long questionId,
                              @AuthenticationPrincipal User user, Model model) {
        return newAnswerModel(gamePatternId, questionId, model);
    }

    @PostMapping("/new/{gamePatternId}/{questionId}")
    public String newAnswer(@PathVariable Long gamePatternId,
                            @PathVariable Long questionId,
                            @RequestParam String context,
                            @AuthenticationPrincipal User user, Model model) {
        AnswerDto answerDto = answerService.createNewAnswer(questionId, context);
        List<AnswerParameterDto> parameters = answerParameterService.getParametersByAnswerId(answerDto.getId());
        model.addAttribute("gamePatternId", gamePatternId);
        model.addAttribute("parameters", parameters);
        model.addAttribute("answerId", answerDto.getId());
        return "parameter/answerParametersList";
    }

    @PostMapping("/delete/{gamePatternId}/{questionId}/{answerId}")
    public String deleteAnswer(@PathVariable Long answerId, @PathVariable Long gamePatternId, @PathVariable Long questionId, @AuthenticationPrincipal User user, Model model) {
        answerService.deleteAnswerByQuestionId(questionId, answerId);
        return newAnswerModel(gamePatternId,questionId,model);
    }

    private String newAnswerModel(Long gamePatternId, Long questionId, Model model) {
        List<AnswerDto> answers = answerService.getAnswersByQuestionId(questionId);
        List<ParameterDto> parameters = parameterService.getParametersByGamePatternId(gamePatternId);
        model.addAttribute("answers", answers);
        model.addAttribute("gamePatternId", gamePatternId);
        model.addAttribute("parameters", parameters);
        return "answer/answersCreator";
    }
}
