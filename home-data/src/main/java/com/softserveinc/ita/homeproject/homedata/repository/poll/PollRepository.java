package com.softserveinc.ita.homeproject.homedata.repository.poll;

import com.softserveinc.ita.homeproject.homedata.entity.poll.Poll;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PollRepository extends PagingAndSortingRepository<Poll, Long>,
    JpaSpecificationExecutor<Poll> {
}
