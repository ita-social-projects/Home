package com.softserveinc.ita.homeproject.homeservice.service.cooperation.invitation;

import java.time.LocalDateTime;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.Invitation;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.InvitationRepository;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.enums.InvitationStatus;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation.InvitationDto;
import com.softserveinc.ita.homeproject.homeservice.exception.InvitationException;
import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundHomeException;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public abstract class InvitationServiceImpl implements InvitationService {

    protected static final int EXPIRATION_TERM = 7;

    protected final InvitationRepository invitationRepository;

    protected final ServiceMapper mapper;

    @PersistenceContext
    protected EntityManager entityManager;

    @Override
    public InvitationDto createInvitation(InvitationDto invitationDto) {
        return saveInvitation(invitationDto);
    }

    protected abstract InvitationDto saveInvitation(InvitationDto invitationDto);

    @Override
    public void updateSentDateTimeAndStatus(Long id) {
        var invitation = findInvitationById(id);
        invitation.setStatus(InvitationStatus.PROCESSING);
        invitation.setSentDatetime(LocalDateTime.now());
        invitationRepository.save(invitation);
    }

    public abstract void markInvitationsAsOverdue();

    private Invitation findInvitationById(Long id) {
        return invitationRepository.findById(id).orElseThrow(() ->
            new InvitationException("Invitation with id " + id + " was not found"));
    }

    @Override
    public InvitationDto findInvitationByRegistrationToken(String token) {
        Invitation invitation = invitationRepository.findInvitationByRegistrationToken(token)
            .orElseThrow(() -> new NotFoundHomeException("Registration token not found"));
        return mapper.convert(invitation, InvitationDto.class);
    }

    @Override
    public void registerWithRegistrationToken(String token) {
        Invitation invitation = invitationRepository.findInvitationByRegistrationToken(token)
            .orElseThrow(() -> new NotFoundHomeException("Registration token not found"));

        if (!invitation.getEnabled().equals(true)) {
            throw new InvitationException("Invitation is not active");
        }
        acceptUserInvitation(invitation);
    }
}
