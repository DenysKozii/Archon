package com.company.archon.controllers.parameters;

import com.company.archon.dto.ParameterDto;
import com.company.archon.pagination.PageDto;
import com.company.archon.services.ParameterService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@AllArgsConstructor
@RequestMapping("/parameter")
public class ParameterController {

    private final ParameterService parameterService;


    @PostMapping("/delete/{gamePatternId}/{parameterId}")
    public String delete(@PathVariable Long gamePatternId, @PathVariable Long parameterId, Model model) {
        parameterService.deleteById(parameterId);
        return "redirect:/parameter/list/" + gamePatternId;
    }

    @PostMapping("/new/{gamePatternId}")
    public String newParameter(@PathVariable Long gamePatternId,
                               @RequestParam String title,
                               @RequestParam Integer defaultValue,
                               @RequestParam Integer highestValue,
                               @RequestParam Integer lowestValue,
                               @RequestParam(defaultValue = "false") Boolean visible, Model model) {
        parameterService.create(title, defaultValue, highestValue, lowestValue, visible, gamePatternId);
        model.addAttribute("gamePatternId", gamePatternId);
        return "redirect:/parameter/list/" + gamePatternId;
    }

    @GetMapping("/list/{gamePatternId}")
    public String parametersList(@PathVariable Long gamePatternId, Model model) {
        PageDto<ParameterDto> parameters = parameterService.getParametersByGamePatternId(gamePatternId);
        model.addAttribute("parameters", parameters.getObjects());
        model.addAttribute("gamePatternId", gamePatternId);
        return "parametersList";
    }

}
