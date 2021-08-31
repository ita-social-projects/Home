package com.softserveinc.ita.homeproject.homedata.poll.repository;

import com.softserveinc.ita.homeproject.homedata.poll.entity.Poll;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PollRepository extends PagingAndSortingRepository<Poll, Long>,
    JpaSpecificationExecutor<Poll> {
}
