package com.company.archon.services;

import com.company.archon.dto.AnswerDto;
import com.company.archon.dto.GameDto;

import java.util.List;

public interface AnswerService {

    boolean answerInfluence(Long answerId, GameDto gameDto);

    List<AnswerDto> getAnswersByQuestionId(Long questionId);

    boolean deleteAnswerByQuestionId(Long questionId, Long answerId);

    AnswerDto createNewAnswer(Long questionId, String context);
}
