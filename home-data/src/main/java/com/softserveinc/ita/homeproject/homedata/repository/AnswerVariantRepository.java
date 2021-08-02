package com.softserveinc.ita.homeproject.homedata.repository;

import com.softserveinc.ita.homeproject.homedata.entity.AnswerVariant;
import com.softserveinc.ita.homeproject.homedata.entity.PollQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * AnswerVariantRepository is the interface that is needed
 * for interaction with AnswerVariants in the database.
 *
 * @author Ihor Samoshost
 */
public interface AnswerVariantRepository extends JpaRepository<AnswerVariant, Long> {

    AnswerVariant findAnswerVariantByAnswerAndQuestion(String answer, PollQuestion question);
}
