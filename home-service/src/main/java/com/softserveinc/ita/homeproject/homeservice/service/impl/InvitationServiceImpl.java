package com.softserveinc.ita.homeproject.homeservice.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.softserveinc.ita.homeproject.homedata.entity.Invitation;
import com.softserveinc.ita.homeproject.homedata.repository.InvitationRepository;
import com.softserveinc.ita.homeproject.homedata.repository.RoleRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.CooperationInvitationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.InvitationDto;
import com.softserveinc.ita.homeproject.homeservice.exception.InvitationException;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.service.InvitationService;
import org.springframework.stereotype.Service;


@Service
public abstract class InvitationServiceImpl implements InvitationService {

    protected final InvitationRepository invitationRepository;

    protected final ServiceMapper mapper;

    protected final RoleRepository roleRepository;

    public InvitationServiceImpl(InvitationRepository invitationRepository, ServiceMapper mapper, RoleRepository roleRepository) {
        this.invitationRepository = invitationRepository;
        this.mapper = mapper;
        this.roleRepository = roleRepository;
    }

    @Override
    public InvitationDto createInvitation(InvitationDto invitationDto) {
        return mapper.convert(fillFieldsByTheType(invitationDto), InvitationDto.class);
    }

    protected abstract Invitation fillFieldsByTheType(InvitationDto invitationDto);

    @Override
    public void updateSentDateTime(Long id, LocalDateTime dateTime) {
        Invitation invitation = findInvitationById(id);
        invitation.setSentDatetime(dateTime);
        invitationRepository.save(invitation);
    }

    @Override
    public InvitationDto getInvitation(Long id) {
        Invitation invitation = findInvitationById(id);
        return mapper.convert(invitation, CooperationInvitationDto.class);
    }



    private Invitation findInvitationById(Long id) {
        return invitationRepository.findById(id).orElseThrow(() ->
                new InvitationException("Invitation with id " + id + " was not found"));
    }

}
