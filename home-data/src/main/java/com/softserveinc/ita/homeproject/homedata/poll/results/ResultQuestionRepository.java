package com.softserveinc.ita.homeproject.homedata.poll.results;

import com.softserveinc.ita.homeproject.homedata.poll.question.PollQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ResultQuestionRepository is the interface that is needed
 * for interaction with ResultQuestions in the database.
 *
 * @author Ihor Samoshost
 */
public interface ResultQuestionRepository extends JpaRepository<ResultQuestion, Long> {

    ResultQuestion findByQuestion(PollQuestion question);
}
