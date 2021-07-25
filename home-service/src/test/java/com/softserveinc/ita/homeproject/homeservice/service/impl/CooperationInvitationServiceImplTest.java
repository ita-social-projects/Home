package com.softserveinc.ita.homeproject.homeservice.service.impl;

import com.softserveinc.ita.homeproject.homedata.entity.CooperationInvitation;
import com.softserveinc.ita.homeproject.homedata.entity.InvitationStatus;
import com.softserveinc.ita.homeproject.homedata.repository.CooperationInvitationRepository;
import com.softserveinc.ita.homeproject.homedata.repository.InvitationRepository;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(MockitoExtension.class)
class CooperationInvitationServiceImplTest {

    private static List<CooperationInvitation> cooperationInvitations;

    private CooperationInvitationServiceImpl cooperationInvitationService;

    @Mock
    CooperationInvitationRepository cooperationInvitationRepository;

    @MockBean
    InvitationRepository invitationRepository;
    @MockBean
    ServiceMapper mapper;

    @BeforeEach
    public void init() {
        cooperationInvitationService = new CooperationInvitationServiceImpl(invitationRepository,
                mapper, cooperationInvitationRepository);
    }

    @BeforeAll
    private static void addCooperationInvitations() {
        cooperationInvitations = new ArrayList<>();

        for (int i = 1; i < 5; i++) {
            CooperationInvitation invitation = new CooperationInvitation();
            invitation.setId((long) i);
            invitation.setStatus(InvitationStatus.PROCESSING);
            invitation.setSentDatetime(LocalDateTime.now().minusDays(18));
            invitation.setRequestEndTime(LocalDateTime.now().minusDays(8));
            cooperationInvitations.add(invitation);
        }
    }

    @Test
    void deactivateApartmentInvitations() {
        for (CooperationInvitation invitation :
                cooperationInvitationService.deactivateCooperationInvitations()) {
            assertEquals(invitation.getStatus(), InvitationStatus.DEACTIVATED);
        }
    }
}
