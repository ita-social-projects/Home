package com.softserveinc.ita.homeproject.homedata.poll;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PollRepository extends PagingAndSortingRepository<Poll, Long>,
    JpaSpecificationExecutor<Poll> {
}
