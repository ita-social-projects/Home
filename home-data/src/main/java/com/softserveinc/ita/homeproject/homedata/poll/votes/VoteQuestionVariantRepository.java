package com.softserveinc.ita.homeproject.homedata.poll.votes;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * VoteQuestionVariantRepository is the interface that is needed
 * for interaction with VoteQuestionVariants in the database.
 *
 * @author Ihor Samoshost
 */
@Repository
public interface VoteQuestionVariantRepository extends PagingAndSortingRepository<VoteQuestionVariant, Long> {
    VoteQuestionVariant findByQuestionVote(QuestionVote questionVote);
}
