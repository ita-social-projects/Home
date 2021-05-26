package com.softserveinc.ita.homeproject.homeservice.service.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;

import com.softserveinc.ita.homeproject.homedata.entity.Cooperation;
import com.softserveinc.ita.homeproject.homedata.entity.House;
import com.softserveinc.ita.homeproject.homedata.entity.Poll;
import com.softserveinc.ita.homeproject.homedata.entity.PollStatus;
import com.softserveinc.ita.homeproject.homedata.repository.CooperationRepository;
import com.softserveinc.ita.homeproject.homedata.repository.PollRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.HouseDto;
import com.softserveinc.ita.homeproject.homeservice.dto.PollDto;
import com.softserveinc.ita.homeproject.homeservice.dto.PollStatusDto;
import com.softserveinc.ita.homeproject.homeservice.exception.BadRequestHomeException;
import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundHomeException;
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

    @Mock
    private static CooperationRepository cooperationRepository;

    @InjectMocks
    private PollServiceImpl pollService;

    @BeforeAll
    static void getConstants() {
        cooperation = new Cooperation();
        cooperation.setId(1L);
        cooperation.setEnabled(true);
        House house = new House();
        house.setEnabled(false);
        cooperation.setHouses(Collections.singletonList(house));
    }

    @Test
    void updateTestWhenPollStatusIsNotDraftTest() {
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
    void updateTestWhenPollStatusChangedToCompleteTest() {
        Poll poll = new Poll();
        poll.setStatus(PollStatus.DRAFT);
        poll.setCooperation(cooperation);
        poll.setEnabled(true);
        PollDto pollDto = new PollDto();
        pollDto.setStatus(PollStatusDto.COMPLETED);
        when(pollRepository.findById(anyLong())).thenReturn(Optional.of(poll));
        assertThrows(BadRequestHomeException.class, () -> pollService.update(1L, 1L, pollDto));
    }

    @Test
    void createPollWithDeletedHouseTest() {
        Poll poll = new Poll();
        poll.setStatus(PollStatus.DRAFT);
        poll.setCooperation(cooperation);
        poll.setEnabled(true);
        HouseDto houseDto = new HouseDto();
        houseDto.setId(1L);
        PollDto pollDto = new PollDto();
        pollDto.setStatus(PollStatusDto.DRAFT);
        pollDto.setPolledHouses(Collections.singletonList(houseDto));
        when(cooperationRepository.findById(anyLong())).thenReturn(Optional.of(cooperation));
        assertThrows(NotFoundHomeException.class, () -> pollService.create(1L, pollDto));
    }
}
