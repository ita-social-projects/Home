package com.softserveinc.ita.homeproject.homedata.poll.question;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * AnswerVariantRepository is the interface that is needed
 * for interaction with AnswerVariants in the database.
 *
 * @author Ihor Samoshost
 */
@Repository
public interface AnswerVariantRepository extends JpaRepository<AnswerVariant, Long> {
    List<AnswerVariant> findAllByQuestion(PollQuestion question);

    Optional<AnswerVariant> findByQuestionAndAnswer(PollQuestion pollQuestion, String answer);
}
