package com.company.archon.controllers.parameters;

import com.company.archon.dto.ConditionParameterDto;
import com.company.archon.services.ConditionParameterService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@AllArgsConstructor
@RequestMapping("/conditionParameter")
public class ConditionParameterController {

    private final ConditionParameterService conditionParameterService;

    @PostMapping("/update/{gamePatternId}/{parameterId}")
    public String newParameter(@PathVariable Long gamePatternId,
                               @PathVariable Long parameterId,
                               @RequestParam Integer value,
                               Model model) {
        conditionParameterService.update(value, parameterId);
        model.addAttribute("gamePatternId", gamePatternId);
        return "redirect:/conditionParameter/list/" + gamePatternId;
    }

    @GetMapping("/list/{gamePatternId}")
    public String conditionParametersList(@PathVariable Long gamePatternId, Model model) {
        List<ConditionParameterDto> parameters = conditionParameterService.getParametersByGamePatternId(gamePatternId);
        model.addAttribute("parameters", parameters);
        model.addAttribute("gamePatternId", gamePatternId);
        return "parameter/conditionsList";
    }

}
