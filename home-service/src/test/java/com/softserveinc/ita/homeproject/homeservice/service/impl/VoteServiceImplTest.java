package com.softserveinc.ita.homeproject.homeservice.service.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.softserveinc.ita.homeproject.homedata.cooperation.Cooperation;
import com.softserveinc.ita.homeproject.homedata.cooperation.house.House;
import com.softserveinc.ita.homeproject.homedata.poll.Poll;
import com.softserveinc.ita.homeproject.homedata.poll.PollRepository;
import com.softserveinc.ita.homeproject.homedata.poll.enums.PollStatus;
import com.softserveinc.ita.homeproject.homedata.poll.votes.VoteRepository;
import com.softserveinc.ita.homeproject.homedata.user.UserRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.poll.votes.VoteDto;
import com.softserveinc.ita.homeproject.homeservice.exception.BadRequestHomeException;
import com.softserveinc.ita.homeproject.homeservice.service.poll.vote.VoteServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(SpringExtension.class)
public class VoteServiceImplTest {

    private static Cooperation cooperation;

    @Mock
    private PollRepository pollRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private VoteRepository voteRepository;


    @InjectMocks
    private VoteServiceImpl voteService;

    @BeforeAll
    static void init() {
        House house = new House();
        cooperation = new Cooperation();

        house.setEnabled(true);
        cooperation.setId(1L);
        cooperation.setEnabled(true);
        cooperation.setHouses(List.of(house));
    }

    @Test
    void voteOnExpiredPollThrowsException() {
        Poll createdPoll = new Poll();
        VoteDto voteDto = new VoteDto();
        createdPoll.setId(1L);
        createdPoll.setEnabled(true);
        createdPoll.setStatus(PollStatus.ACTIVE);
        createdPoll.setCompletionDate(LocalDateTime.now().minusDays(1L));
        voteDto.setPollId(createdPoll.getId());
        when(pollRepository.findById(voteDto.getPollId())).thenReturn(Optional.of(createdPoll));

        assertThrows(BadRequestHomeException.class, () -> voteService.createVote(voteDto));
    }
}
