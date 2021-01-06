package com.company.archon.controllers.parameters;

import com.company.archon.dto.ParameterDto;
import com.company.archon.entity.User;
import com.company.archon.services.ParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/parameter")
public class ParameterController {

    private final ParameterService parameterService;


    @Autowired
    public ParameterController(ParameterService parameterService) {
        this.parameterService = parameterService;
    }


    @PostMapping("/delete/{gamePatternId}/{parameterId}")
    public String deleteQuestion(@PathVariable Long gamePatternId, @PathVariable Long parameterId, @AuthenticationPrincipal User user, Model model) {
        parameterService.deleteParameter(parameterId);
        return "redirect:/parameter/list/"+gamePatternId;
    }

    @PostMapping("/new/{gamePatternId}")
    public String newParameter(@PathVariable Long gamePatternId,
                              @RequestParam String title,
                              @RequestParam Integer defaultValue,
                              @RequestParam Integer highestValue,
                              @RequestParam Integer lowestValue,
                              @AuthenticationPrincipal User user, Model model) {
        parameterService.createParameter(title, defaultValue,highestValue,lowestValue , gamePatternId);
        model.addAttribute("gamePatternId",gamePatternId);
        return "redirect:/parameter/list/"+gamePatternId;
    }

    @GetMapping("/list/{gamePatternId}")
    public String parametersList(@PathVariable Long gamePatternId, @AuthenticationPrincipal User user, Model model) {
        List<ParameterDto> parameters = parameterService.getParametersByGamePatternId(gamePatternId);
        model.addAttribute("parameters", parameters);
        model.addAttribute("gamePatternId",gamePatternId);
        return "parameter/parametersList";
    }

}
