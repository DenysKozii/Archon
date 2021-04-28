package com.company.archon.services;

import com.company.archon.dto.QuestionDto;
import com.company.archon.pagination.PageDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface QuestionService {

    PageDto<QuestionDto> getQuestionsByGamePatternId(Long gamePatternId, int page, int pageSize);

    PageDto<QuestionDto> addRelativeQuestion(Long questionId, Long relativeId, int page, int pageSize);

    boolean deleteById(Long questionId);

//    QuestionDto createNewQuestion(Long gamePatternId, String title, String context, Integer weight, MultipartFile multipartFile) throws IOException;

    PageDto<QuestionDto> getRelativeQuestions(Long questionId, int page, int pageSize);

    QuestionDto createNewQuestion(Long gamePatternId);

    boolean updateQuestion(Long gamePatternId, Long questionId, String title, String context, Integer weight, MultipartFile multipartFile) throws IOException;


    PageDto<QuestionDto> getRelativeQuestionsByGamePatternId(Long gamePatternId, Long questionId, int page, int pageSize);
}
