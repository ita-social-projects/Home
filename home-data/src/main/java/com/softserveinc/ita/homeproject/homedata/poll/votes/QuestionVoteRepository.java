package com.softserveinc.ita.homeproject.homedata.poll.votes;

import java.util.List;

import com.softserveinc.ita.homeproject.homedata.poll.question.PollQuestion;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

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
