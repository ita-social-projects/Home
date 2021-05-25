package com.softserveinc.ita.homeproject.homeservice.service.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.softserveinc.ita.homeproject.homedata.entity.Cooperation;
import com.softserveinc.ita.homeproject.homedata.entity.Poll;
import com.softserveinc.ita.homeproject.homedata.entity.PollStatus;
import com.softserveinc.ita.homeproject.homedata.repository.PollRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.PollDto;
import com.softserveinc.ita.homeproject.homeservice.dto.PollStatusDto;
import com.softserveinc.ita.homeproject.homeservice.exception.BadRequestHomeException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class PollServiceImplTest {
    private static Cooperation cooperation;

    @Mock
    private static PollRepository pollRepository;

    @InjectMocks
    private PollServiceImpl pollService;

    @BeforeAll
    static void getConstants() {
        cooperation = new Cooperation();
        cooperation.setId(1L);
        cooperation.setEnabled(true);
    }

    @Test
    void updateTestWhenPollStatusIsNotDraft() {
        Poll poll = new Poll();
        poll.setStatus(PollStatus.ACTIVE);
        poll.setCooperation(cooperation);
        poll.setEnabled(true);
        PollDto pollDto = new PollDto();
        pollDto.setStatus(PollStatusDto.SUSPENDED);
        when(pollRepository.findById(anyLong())).thenReturn(Optional.of(poll));
        assertThrows(BadRequestHomeException.class, () -> pollService.update(1L, 1L, pollDto));
    }

    @Test
    void updateTestWhenPollStatusChangedToComplete() {
        Poll poll = new Poll();
        poll.setStatus(PollStatus.DRAFT);
        poll.setCooperation(cooperation);
        poll.setEnabled(true);
        PollDto pollDto = new PollDto();
        pollDto.setStatus(PollStatusDto.COMPLETED);
        when(pollRepository.findById(anyLong())).thenReturn(Optional.of(poll));
        assertThrows(BadRequestHomeException.class, () -> pollService.update(1L, 1L, pollDto));
    }

}