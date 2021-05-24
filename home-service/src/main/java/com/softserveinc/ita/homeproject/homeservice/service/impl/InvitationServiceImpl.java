package com.softserveinc.ita.homeproject.homeservice.service.impl;

import java.time.LocalDateTime;

import com.softserveinc.ita.homeproject.homedata.entity.Invitation;
import com.softserveinc.ita.homeproject.homedata.entity.InvitationStatus;
import com.softserveinc.ita.homeproject.homedata.repository.InvitationRepository;
import com.softserveinc.ita.homeproject.homedata.repository.RoleRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.CooperationInvitationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.InvitationDto;
import com.softserveinc.ita.homeproject.homeservice.exception.InvitationException;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.service.InvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public abstract class InvitationServiceImpl implements InvitationService {

    protected final InvitationRepository invitationRepository;

    protected final ServiceMapper mapper;

    protected final RoleRepository roleRepository;

    @Override
    public InvitationDto createInvitation(InvitationDto invitationDto) {
        return fillFieldsByTheType(invitationDto);
    }

    protected abstract InvitationDto fillFieldsByTheType(InvitationDto invitationDto);

    @Override
    public void updateSentDateTimeAndStatus(Long id, LocalDateTime dateTime) {
        var invitation = findInvitationById(id);
        invitation.setStatus(InvitationStatus.PROCESSING);
        invitation.setSentDatetime(dateTime);
        invitationRepository.save(invitation);
    }

    @Override
    public InvitationDto getInvitation(Long id) {
        var invitation = findInvitationById(id);
        return mapper.convert(invitation, CooperationInvitationDto.class);
    }

    private Invitation findInvitationById(Long id) {
        return invitationRepository.findById(id).orElseThrow(() ->
                new InvitationException("Invitation with id " + id + " was not found"));
    }

}
