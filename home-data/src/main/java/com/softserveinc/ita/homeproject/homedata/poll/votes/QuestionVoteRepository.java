package com.softserveinc.ita.homeproject.homedata.poll.votes;

import com.softserveinc.ita.homeproject.homedata.poll.question.PollQuestion;
import org.aspectj.weaver.patterns.TypePatternQuestions;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * QuestionVoteRepository is the interface that is needed
 * for interaction with QuestionVotes in the database.
 *
 * @author Ihor Samoshost
 */
@Repository
public interface QuestionVoteRepository extends PagingAndSortingRepository<QuestionVote, Long> {
    List<QuestionVote> findAllByQuestion(PollQuestion question);

}
