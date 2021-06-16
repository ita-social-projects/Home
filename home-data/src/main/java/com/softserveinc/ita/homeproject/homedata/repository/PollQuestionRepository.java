package com.softserveinc.ita.homeproject.homedata.repository;

import com.softserveinc.ita.homeproject.homedata.entity.PollQuestion;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PollQuestionRepository extends PagingAndSortingRepository<PollQuestion, Long>,
        JpaSpecificationExecutor<PollQuestion> {

}
