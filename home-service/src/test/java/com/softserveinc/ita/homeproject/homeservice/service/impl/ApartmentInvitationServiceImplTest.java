package com.softserveinc.ita.homeproject.homeservice.service.impl;

import com.softserveinc.ita.homeproject.homedata.entity.ApartmentInvitation;
import com.softserveinc.ita.homeproject.homedata.entity.InvitationStatus;
import com.softserveinc.ita.homeproject.homedata.repository.ApartmentInvitationRepository;
import com.softserveinc.ita.homeproject.homedata.repository.ApartmentRepository;
import com.softserveinc.ita.homeproject.homedata.repository.InvitationRepository;
import com.softserveinc.ita.homeproject.homedata.repository.OwnershipRepository;
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
import org.springframework.data.jpa.domain.Specification;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ApartmentInvitationServiceImplTest {

    private static List<ApartmentInvitation> apartmentInvitations;

    private ApartmentInvitationServiceImpl apartmentInvitationService;
    @Mock
    ApartmentInvitationRepository apartmentInvitationRepository;

    @MockBean
    private OwnershipRepository ownershipRepository;
    @MockBean
    private ApartmentRepository apartmentRepository;
    @MockBean
    InvitationRepository invitationRepository;
    @MockBean
    ServiceMapper mapper;

    @BeforeEach
    public void init() {
        apartmentInvitationService = new ApartmentInvitationServiceImpl(invitationRepository, mapper,
                apartmentInvitationRepository, ownershipRepository, apartmentRepository);
    }

    @BeforeAll
    private static void addApartmentInvitations() {
        apartmentInvitations = new ArrayList<>();

        for (int i = 1; i < 5; i++) {
            ApartmentInvitation invitation = new ApartmentInvitation();
            invitation.setId((long) i);
            invitation.setStatus(InvitationStatus.PROCESSING);
            invitation.setSentDatetime(LocalDateTime.now().minusDays(18));
            invitation.setRequestEndTime(LocalDateTime.now().minusDays(8));
            apartmentInvitations.add(invitation);
        }
    }

    @Test
    void deactivateApartmentInvitations() {
        when(apartmentInvitationRepository.findAll(any(Specification.class)))
                .thenReturn(apartmentInvitations);

        for (ApartmentInvitation invitation :
                apartmentInvitationService.deactivateApartmentInvitations()) {
            assertEquals(InvitationStatus.OVERDUE, invitation.getStatus());
        }
    }
}
