package com.company.archon.services;


import com.company.archon.dto.AnswerDto;
import com.company.archon.dto.GameDto;
import com.company.archon.dto.QuestionDto;
import com.company.archon.pagination.PageDto;

import java.util.List;

public interface AnswerService {

    List<AnswerDto> getAnswersByQuestionId(Long questionId);

//    PageDto<AnswerDto> getAnswersByQuestionId(Long questionId, int page, int pageSize);

    boolean deleteById(Long answerId);

    AnswerDto createNewAnswer(Long questionId, String context);

    List<AnswerDto> getAll();

    List<QuestionDto> getQuestionsByAnswers();

    void updateCounter(Long answerId, Integer counter);
}
