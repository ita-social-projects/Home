package com.softserveinc.ita.homeproject.homedata.repository;

import com.softserveinc.ita.homeproject.homedata.entity.polls.votes.VoteQuestionVariant;
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
}
