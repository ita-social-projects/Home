package com.softserveinc.ita.homeproject.homeservice.service.cooperation.invitation;

import java.time.LocalDateTime;

import com.softserveinc.ita.homeproject.homedata.entity.cooperation.invitation.Invitation;
import com.softserveinc.ita.homeproject.homedata.entity.cooperation.invitation.InvitationStatus;
import com.softserveinc.ita.homeproject.homedata.repository.cooperation.invitation.InvitationRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation.InvitationDto;
import com.softserveinc.ita.homeproject.homeservice.exception.InvitationException;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public abstract class InvitationServiceImpl implements InvitationService {

    protected final InvitationRepository invitationRepository;

    protected final ServiceMapper mapper;

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

    private Invitation findInvitationById(Long id) {
        return invitationRepository.findById(id).orElseThrow(() ->
                new InvitationException("Invitation with id " + id + " was not found"));
    }
}
