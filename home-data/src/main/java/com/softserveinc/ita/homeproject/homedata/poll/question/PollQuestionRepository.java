package com.softserveinc.ita.homeproject.homedata.poll.question;

import java.util.Optional;

import com.softserveinc.ita.homeproject.homedata.poll.Poll;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PollQuestionRepository extends PagingAndSortingRepository<PollQuestion, Long>,
        JpaSpecificationExecutor<PollQuestion> {

}
