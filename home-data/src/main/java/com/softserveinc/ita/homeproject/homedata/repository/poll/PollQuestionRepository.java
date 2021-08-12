package com.softserveinc.ita.homeproject.homedata.repository.poll;

import com.softserveinc.ita.homeproject.homedata.entity.poll.PollQuestion;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PollQuestionRepository extends PagingAndSortingRepository<PollQuestion, Long>,
        JpaSpecificationExecutor<PollQuestion> {

}
