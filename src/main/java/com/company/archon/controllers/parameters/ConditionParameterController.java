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

    @PostMapping("/update/start/{gamePatternId}/{parameterId}")
    public String updateStart(@PathVariable Long gamePatternId,
                               @PathVariable Long parameterId,
                               @RequestParam(defaultValue = "0") Integer valueStart,
                               Model model) {
        conditionParameterService.updateStart(valueStart, parameterId);
        model.addAttribute("gamePatternId", gamePatternId);
        return "redirect:/conditionParameter/list/start/" + gamePatternId;
    }

    @PostMapping("/update/finish/{gamePatternId}/{parameterId}")
    public String updateFinish(@PathVariable Long gamePatternId,
                               @PathVariable Long parameterId,
                               @RequestParam(defaultValue = "0") Integer valueFinish,
                               Model model) {
        conditionParameterService.updateFinish(valueFinish, parameterId);
        model.addAttribute("gamePatternId", gamePatternId);
        return "redirect:/conditionParameter/list/finish/" + gamePatternId;
    }

    @GetMapping("/list/start/{gamePatternId}")
    public String conditionParametersStartList(@PathVariable Long gamePatternId, Model model) {
        List<ConditionParameterDto> parameters = conditionParameterService.getParametersByGamePatternId(gamePatternId);
        model.addAttribute("parameters", parameters);
        model.addAttribute("gamePatternId", gamePatternId);
        return "/parameter/conditionsStartList";
    }

    @GetMapping("/list/finish/{gamePatternId}")
    public String conditionParametersFinishList(@PathVariable Long gamePatternId, Model model) {
        List<ConditionParameterDto> parameters = conditionParameterService.getParametersByGamePatternId(gamePatternId);
        model.addAttribute("parameters", parameters);
        model.addAttribute("gamePatternId", gamePatternId);
        return "/parameter/conditionsFinishList";
    }

}
