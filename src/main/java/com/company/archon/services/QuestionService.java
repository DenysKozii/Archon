package com.company.archon.services;

import com.company.archon.dto.GameDto;
import com.company.archon.dto.QuestionDto;
import com.company.archon.dto.QuestionParameterDto;
import com.company.archon.entity.Parameter;
import com.company.archon.entity.QuestionParameter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface QuestionService {
    List<QuestionDto> getQuestionsByGamePatternId(Long gamePatternId, Long questionId);

    List<QuestionDto> getQuestionsByGamePatternId(Long gamePatternId);

    QuestionDto createNewQuestion(Long gamePatternId);

    List<QuestionDto> addRelativeQuestion(Long questionId, Long relativeId, Long gamePatternId);

    boolean deleteQuestion(Long questionId);

    boolean updateQuestion(Long gamePatternId, Long questionId, String title, String context, Integer weight, MultipartFile multipartFile) throws IOException;
}
