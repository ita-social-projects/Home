package com.softserveinc.ita.homeproject.homeservice.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.softserveinc.ita.homeproject.homedata.entity.Invitation;
import com.softserveinc.ita.homeproject.homedata.repository.InvitationRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.InvitationDto;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.service.InvitationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InvitationServiceImpl implements InvitationService {

    private final InvitationRepository invitationRepository;
    private final ServiceMapper mapper;

    @Override
    public InvitationDto createInvitation(InvitationDto invitationDto) {
        Invitation convert = mapper.convert(invitationDto, Invitation.class);
        convert.setCreateDateTime(LocalDateTime.now());

        Invitation invitation = invitationRepository.save(convert);
        return mapper.convert(invitation, InvitationDto.class);
    }

    @Override
    public void changeInvitationStatus(Set<Long> ids) {
//        TODO: implement updateStatus method in repo
        invitationRepository.updateStatus(ids);
    }

    @Override
    public InvitationDto getInvitation(Long id) {
        return invitationRepository.findById(id)
            .map(invitation -> mapper.convert(invitation, InvitationDto.class)).orElse(null);

//        Optional<Invitation> byId = invitationRepository.findById(id);
//        Invitation invitation = byId.get();
//        InvitationDto invitationDto = mapper.convert(invitation, InvitationDto.class);
//        return invitationDto;
    }

    @Override public List<InvitationDto> getAllActiveInvitations() {
//        TODO: implement getAllActiveInvitations method in repo
        List<Invitation> allNotSentInvitations = invitationRepository.getAllNotSentInvitations();

//        List<InvitationDto> invitationDtoList = new ArrayList<>();
//        for (Invitation invitation: allNotSentInvitations) {
//            InvitationDto invitationDto = mapper.convert(invitation, InvitationDto.class);
//            invitationDtoList.add(invitationDto);
//        }
//        return invitationDtoList;

        return allNotSentInvitations.stream()
            .map(invitation -> mapper.convert(invitation, InvitationDto.class))
            .collect(Collectors.toList());
    }
}
