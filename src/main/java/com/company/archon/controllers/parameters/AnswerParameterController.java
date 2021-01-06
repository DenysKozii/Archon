package com.company.archon.controllers.parameters;

import com.company.archon.dto.AnswerParameterDto;
import com.company.archon.dto.QuestionParameterDto;
import com.company.archon.entity.User;
import com.company.archon.services.AnswerParameterService;
import com.company.archon.services.QuestionParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/answerParameter")
public class AnswerParameterController {

    private final AnswerParameterService answerParameterService;

    @Autowired
    public AnswerParameterController(AnswerParameterService answerParameterService) {
        this.answerParameterService = answerParameterService;
    }

    @PostMapping("/update/{gamePatternId}/{questionId}/{answerId}/{parameterId}")
    public String update(@PathVariable Long gamePatternId,
                         @PathVariable Long questionId,
                         @PathVariable Long answerId,
                         @PathVariable Long parameterId,
                         @RequestParam Integer influence,
                         @AuthenticationPrincipal User user, Model model) {
        answerParameterService.update(parameterId, influence);
        List<AnswerParameterDto> parameters = answerParameterService.getParametersByAnswerId(answerId);
        model.addAttribute("gamePatternId", gamePatternId);
        model.addAttribute("questionId", questionId);
        model.addAttribute("parameters", parameters);
        return "parameter/answerParametersList";
    }
}
