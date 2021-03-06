package com.company.archon.controllers.game;

import com.company.archon.dto.*;
import com.company.archon.entity.Question;
import com.company.archon.pagination.PageDto;
import com.company.archon.services.AnswerService;
import com.company.archon.services.ParameterService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/answer")
public class AnswerController {
    private final AnswerService answerService;
    private final ParameterService parameterService;

    @GetMapping("/new/{gamePatternId}/{questionId}")
    public String answersList(@PathVariable Long gamePatternId,
                              @PathVariable Long questionId, Model model) {
        return newAnswerModel(gamePatternId, questionId, model);
    }

    @PostMapping("/new/{gamePatternId}/{questionId}")
    public String newAnswer(@PathVariable Long gamePatternId,
                            @PathVariable Long questionId,
                            @RequestParam String context, Model model) {
        AnswerDto answerDto = answerService.createNewAnswer(questionId, context);
        model.addAttribute("gamePatternId", gamePatternId);
        model.addAttribute("parameters", answerDto.getParameters());
        model.addAttribute("userParameters", answerDto.getUserParameters());
        model.addAttribute("answerId", answerDto.getId());
        return "answerParametersList";
    }

    @PostMapping("/delete/{gamePatternId}/{questionId}/{answerId}")
    public String deleteAnswer(@PathVariable Long answerId, @PathVariable Long gamePatternId, @PathVariable Long questionId, Model model) {
        answerService.deleteById(answerId);
        return newAnswerModel(gamePatternId,questionId,model);
    }

    @GetMapping("/statistics")
    public String getStatistics(Model model) {
        List<AnswerDto> answers = answerService.getAll();
        model.addAttribute("answers", answers);
        return "answerStatistics";
    }

    @PostMapping("/statistics/update/{answerId}")
    public String getStatistics(@PathVariable Long answerId, @RequestParam Integer counter,Model model) {
        answerService.updateCounter(answerId, counter);
        return "redirect:/answer/statistics";
    }

    private String newAnswerModel(Long gamePatternId, Long questionId, Model model) {
        List<AnswerDto> answers = answerService.getAnswersByQuestionId(questionId);
        PageDto<ParameterDto> parameters = parameterService.getParametersByGamePatternId(gamePatternId);
        model.addAttribute("answers", answers);
        model.addAttribute("gamePatternId", gamePatternId);
        model.addAttribute("parameters", parameters.getObjects());
        return "answersCreator";
    }
}
