package com.softserveinc.ita.homeproject.homedata.poll.votes;

import java.util.List;

import com.softserveinc.ita.homeproject.homedata.poll.Poll;
import com.softserveinc.ita.homeproject.homedata.user.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
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

    List<Vote> findAllByPoll(Poll poll);
}
