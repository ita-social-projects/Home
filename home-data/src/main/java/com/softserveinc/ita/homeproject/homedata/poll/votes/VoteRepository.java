package com.softserveinc.ita.homeproject.homedata.poll.votes;

import com.softserveinc.ita.homeproject.homedata.user.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * VoteRepository is the interface that is needed
 * for interaction with Votes in the database.
 *
 * @author Ihor Samoshost
 */
@Repository
public interface VoteRepository extends PagingAndSortingRepository<Vote, Long> {
    Vote findByPollIdAndUser(Long pollId, User user);
}
