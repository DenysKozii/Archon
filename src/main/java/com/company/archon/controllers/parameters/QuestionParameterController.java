package com.company.archon.controllers.parameters;

import com.company.archon.dto.QuestionParameterDto;
import com.company.archon.dto.QuestionUserParameterDto;
import com.company.archon.pagination.PageDto;
import com.company.archon.services.QuestionParameterService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/questionParameter")
public class QuestionParameterController {

    private final QuestionParameterService questionParameterService;


    @PostMapping("/update/{gamePatternId}/{questionId}/{parameterId}")
    public String update(@PathVariable Long gamePatternId,
                         @PathVariable Long questionId,
                         @PathVariable Long parameterId,
                         @RequestParam Integer appear,
                         @RequestParam Integer disappear, Model model) {
        questionParameterService.update(parameterId, appear, disappear);
        PageDto<QuestionParameterDto> questionParameters = questionParameterService.getParametersByQuestionId(questionId,0,150);
        List<QuestionUserParameterDto> questionUserParameters = questionParameterService.getUserParametersByQuestionId(questionId);
        model.addAttribute("gamePatternId", gamePatternId);
        model.addAttribute("questionId", questionId);
        model.addAttribute("parameters", questionParameters.getObjects());
        model.addAttribute("userParameters", questionUserParameters);
        return "questionParametersList";
    }

    @PostMapping("/update/user/{gamePatternId}/{questionId}/{parameterId}")
    public String updateUserParameter(@PathVariable Long gamePatternId,
                         @PathVariable Long questionId,
                         @PathVariable Long parameterId,
                         @RequestParam Integer appear, Model model) {
        questionParameterService.updateUserParameter(parameterId, appear);
        PageDto<QuestionParameterDto> questionParameters = questionParameterService.getParametersByQuestionId(questionId,0,150);
        List<QuestionUserParameterDto> questionUserParameters = questionParameterService.getUserParametersByQuestionId(questionId);
        model.addAttribute("gamePatternId", gamePatternId);
        model.addAttribute("questionId", questionId);
        model.addAttribute("parameters", questionParameters.getObjects());
        model.addAttribute("userParameters", questionUserParameters);
        return "questionParametersList";
    }

}
