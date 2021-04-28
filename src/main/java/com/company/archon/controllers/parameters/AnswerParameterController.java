package com.company.archon.controllers.parameters;

import com.company.archon.dto.AnswerParameterDto;
import com.company.archon.pagination.PageDto;
import com.company.archon.services.AnswerParameterService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
@RequestMapping("/answerParameter")
public class AnswerParameterController {

    private final AnswerParameterService answerParameterService;

    @PostMapping("/update/{gamePatternId}/{questionId}/{answerId}/{parameterId}")
    public String update(@PathVariable Long gamePatternId,
                         @PathVariable Long questionId,
                         @PathVariable Long answerId,
                         @PathVariable Long parameterId,
                         @RequestParam Integer influence, Model model) {
        answerParameterService.update(parameterId, influence);
        PageDto<AnswerParameterDto> parameters = answerParameterService.getParametersByAnswerId(answerId,0,150);
        model.addAttribute("gamePatternId", gamePatternId);
        model.addAttribute("questionId", questionId);
        model.addAttribute("parameters", parameters.getObjects());
        return "parameter/answerParametersList";
    }
}
