package com.softserveinc.ita.homeproject.homedata.repository;

import com.softserveinc.ita.homeproject.homedata.entity.polls.votes.QuestionVote;
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
}
