package com.company.archon.controllers.game;

import com.company.archon.dto.QuestionDto;
import com.company.archon.entity.User;
import com.company.archon.enums.GameStatus;
import com.company.archon.pagination.PageDto;
import com.company.archon.services.ParameterService;
import com.company.archon.services.QuestionParameterService;
import com.company.archon.services.QuestionService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
        model.addAttribute("question",question);
        model.addAttribute("id", question.getId());
        return "questionCreator";
    }

    @GetMapping("/{gamePatternId}/{questionId}")
    public String newQuestion(@PathVariable Long gamePatternId,
                              @PathVariable Long questionId, Model model) {
        QuestionDto question = questionService.getById(questionId);
        model.addAttribute("gamePatternId", gamePatternId);
        model.addAttribute("question",question);
        model.addAttribute("id", question.getId());
        return "questionCreator";
    }

    @RequestMapping("/delete/{gamePatternId}/{questionId}")
    public String deleteQuestion(@PathVariable Long gamePatternId,
                                 @PathVariable Long questionId) {
        questionService.deleteById(questionId);
        return "redirect:/question/list/" + gamePatternId;
    }

    @PostMapping("/update/{gamePatternId}/{questionId}")
    public String updateQuestion(@PathVariable Long gamePatternId,
                              @PathVariable Long questionId,
                              @RequestParam(required = false) String title,
                              @RequestParam(required = false) String context,
                              @RequestParam(required = false) Integer weight,
                              @RequestParam GameStatus status,
                              @RequestParam(value = "fileImage", required = false) MultipartFile multipartFile,
                              Model model) throws IOException {
        QuestionDto questionDto = questionService.updateQuestion(gamePatternId, questionId,
                title,
                context,
                weight,
                status,
                multipartFile);
        model.addAttribute("questionId", questionId);
        model.addAttribute("gamePatternId", gamePatternId);
        model.addAttribute("userParameters", questionDto.getQuestionUserParameters());
        model.addAttribute("parameters", questionDto.getQuestionParameters());
        return "questionParametersList";
    }

    @GetMapping("/list/{gamePatternId}")
    public String questionsList(@PathVariable Long gamePatternId, Model model) {
        PageDto<QuestionDto> questions = questionService.getQuestionsByGamePatternId(gamePatternId, 0, 150);
        model.addAttribute("questions", questions.getObjects());
        model.addAttribute("gamePatternId",gamePatternId);
        return "questionsList";
    }

    @GetMapping("/relativeQuestions/{gamePatternId}/{questionId}")
    public String relativeQuestionsList(@PathVariable Long gamePatternId,@PathVariable Long questionId, Model model) {
        PageDto<QuestionDto> questions = questionService.getRelativeQuestionsByGamePatternId(gamePatternId,questionId,0, 150);
        model.addAttribute("questions", questions.getObjects());
        model.addAttribute("gamePatternId",gamePatternId);
        return "relativeQuestionsList";
    }

    @GetMapping("/addRelativeQuestion/{gamePatternId}/{questionId}/{relativeId}")
    public String addRelativeQuestion(@PathVariable Long questionId,@PathVariable Long relativeId, @PathVariable Long gamePatternId, @AuthenticationPrincipal User user, Model model) {
        PageDto<QuestionDto> questions = questionService.addRelativeQuestion(questionId, relativeId,0,150);
        model.addAttribute("questions", questions.getObjects());
        model.addAttribute("gamePatternId",gamePatternId);
        return "relativeQuestionsList";
    }
}
