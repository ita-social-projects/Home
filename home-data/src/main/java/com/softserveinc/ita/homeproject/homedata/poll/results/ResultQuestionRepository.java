package com.softserveinc.ita.homeproject.homedata.poll.results;


import java.util.Optional;

import com.softserveinc.ita.homeproject.homedata.poll.question.AnswerVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultQuestionRepository extends JpaRepository<ResultQuestion, Long> {
}
