package com.company.archon.controllers.parameters;

import com.company.archon.dto.ParameterDto;
import com.company.archon.dto.QuestionParameterDto;
import com.company.archon.entity.User;
import com.company.archon.services.ParameterService;
import com.company.archon.services.QuestionParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/questionParameter")
public class QuestionParameterController {

    private final QuestionParameterService questionParameterService;


    @Autowired
    public QuestionParameterController(QuestionParameterService questionParameterService) {
        this.questionParameterService = questionParameterService;
    }


    @PostMapping("/update/{gamePatternId}/{questionId}/{parameterId}")
    public String update(@PathVariable Long gamePatternId,
                         @PathVariable Long questionId,
                         @PathVariable Long parameterId,
                         @RequestParam Integer appear,
                         @RequestParam Integer disappear,
                         @AuthenticationPrincipal User user, Model model) {
        questionParameterService.update(parameterId, appear, disappear);
        List<QuestionParameterDto> questionParameters = questionParameterService.getParametersByQuestionId(questionId);
        model.addAttribute("gamePatternId", gamePatternId);
        model.addAttribute("questionId", questionId);
        model.addAttribute("parameters", questionParameters);
        return "parameter/questionParametersList";
    }

}
