package com.softserveinc.ita.homeproject.homedata.repository;

import com.softserveinc.ita.homeproject.homedata.entity.QuestionVote;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * QuestionVoteRepository is the interface that is needed
 * for interaction with QuestionVotes in the database.
 *
 * @author Ihor Samoshost
 */
public interface QuestionVoteRepository extends PagingAndSortingRepository<QuestionVote, Long> {
}
