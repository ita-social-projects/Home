package com.softserveinc.ita.homeproject.homedata.poll.results;


import java.util.Collection;

import com.softserveinc.ita.homeproject.homedata.poll.Poll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultQuestionRepository extends JpaRepository<ResultQuestion, Long> {
    Collection<ResultQuestion> findAllByPoll(Poll poll);
}
