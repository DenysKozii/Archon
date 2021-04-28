package com.company.archon.controllers.game;

import com.company.archon.dto.QuestionDto;
import com.company.archon.dto.QuestionParameterDto;
import com.company.archon.entity.User;
import com.company.archon.pagination.PageDto;
import com.company.archon.services.ParameterService;
import com.company.archon.services.QuestionParameterService;
import com.company.archon.services.QuestionService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/question")
public class QuestionController {

    private final QuestionService questionService;
    private final QuestionParameterService questionParameterService;
    private final ParameterService parameterService;

    @GetMapping("/new/{gamePatternId}")
    public String newQuestion(@PathVariable Long gamePatternId, Model model) {
        QuestionDto question = questionService.createNewQuestion(gamePatternId);
        model.addAttribute("gamePatternId",gamePatternId);
        model.addAttribute("id", question.getId());
        return "question/questionCreator";
    }

    @RequestMapping("/delete/{gamePatternId}/{questionId}")
    public String deleteQuestion(@PathVariable Long gamePatternId,
                                 @PathVariable Long questionId) {
        questionService.deleteById(questionId);
        return "redirect:/question/list/" + gamePatternId;
    }

    @PostMapping("/new/{gamePatternId}/{questionId}")
    public String newQuestion(@PathVariable Long gamePatternId,
                              @PathVariable Long questionId,
                              @RequestParam String title,
                              @RequestParam String context,
                              @RequestParam Integer weight,
//                              @RequestParam("fileImage") MultipartFile multipartFile,
                              @AuthenticationPrincipal User user, Model model) throws IOException {
        questionService.updateQuestion(gamePatternId, questionId,
                title,
                context,
                weight,
                null);
        PageDto<QuestionParameterDto> questionParameters = questionParameterService.getParametersByQuestionId(questionId,0,150);
        model.addAttribute("questionId", questionId);
        model.addAttribute("gamePatternId", gamePatternId);
        model.addAttribute("parameters", questionParameters.getObjects());
        return "parameter/questionParametersList";
    }

    @GetMapping("/list/{gamePatternId}")
    public String questionsList(@PathVariable Long gamePatternId, Model model) {
        PageDto<QuestionDto> questions = questionService.getQuestionsByGamePatternId(gamePatternId, 0, 150);
        model.addAttribute("questions", questions.getObjects());
        model.addAttribute("gamePatternId",gamePatternId);
        return "question/questionsList";
    }

    @GetMapping("/relativeQuestions/{gamePatternId}/{questionId}")
    public String relativeQuestionsList(@PathVariable Long gamePatternId,@PathVariable Long questionId, Model model) {
        PageDto<QuestionDto> questions = questionService.getRelativeQuestionsByGamePatternId(gamePatternId,questionId,0, 150);
        model.addAttribute("questions", questions.getObjects());
        model.addAttribute("gamePatternId",gamePatternId);
        return "question/relativeQuestionsList";
    }

    @GetMapping("/addRelativeQuestion/{gamePatternId}/{questionId}/{relativeId}")
    public String addRelativeQuestion(@PathVariable Long questionId,@PathVariable Long relativeId, @PathVariable Long gamePatternId, @AuthenticationPrincipal User user, Model model) {
        PageDto<QuestionDto> questions = questionService.addRelativeQuestion(questionId, relativeId,0,150);
        model.addAttribute("questions", questions.getObjects());
        model.addAttribute("gamePatternId",gamePatternId);
        return "question/relativeQuestionsList";
    }
}
