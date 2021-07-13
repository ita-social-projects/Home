package com.softserveinc.ita.homeproject.homedata.repository;

import com.softserveinc.ita.homeproject.homedata.entity.QuestionVote;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface QuestionVoteRepository extends PagingAndSortingRepository<QuestionVote, Long> {
}
