package com.softserveinc.ita.homeproject.homeservice.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.softserveinc.ita.homeproject.homedata.entity.ApartmentInvitation;
import com.softserveinc.ita.homeproject.homedata.entity.InvitationStatus;
import com.softserveinc.ita.homeproject.homedata.repository.ApartmentInvitationRepository;
import com.softserveinc.ita.homeproject.homedata.repository.ApartmentRepository;
import com.softserveinc.ita.homeproject.homedata.repository.InvitationRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.ApartmentInvitationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.InvitationDto;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.service.ApartmentInvitationService;
import org.springframework.stereotype.Service;


@Service
public class ApartmentInvitationServiceImpl extends InvitationServiceImpl implements ApartmentInvitationService {

    private final ApartmentRepository apartmentRepository;
    private final ApartmentInvitationRepository apartmentInvitationRepository;

    public ApartmentInvitationServiceImpl(InvitationRepository invitationRepository,
                                          ServiceMapper mapper,
                                          ApartmentRepository apartmentRepository,
                                          ApartmentInvitationRepository apartmentInvitationRepository) {
        super(invitationRepository, mapper);
        this.apartmentRepository = apartmentRepository;
        this.apartmentInvitationRepository = apartmentInvitationRepository;
    }

    @Override
    protected InvitationDto saveInvitation(InvitationDto invitationDto) {
        ApartmentInvitationDto apartmentInvitationDto =
                mapper.convert(invitationDto, ApartmentInvitationDto.class);
        ApartmentInvitation apartmentInvitation =
                mapper.convert(apartmentInvitationDto, ApartmentInvitation.class);
        apartmentInvitation.setApartment(apartmentRepository.findApartmentByApartmentNumber(apartmentInvitationDto.getApartmentNumber()));
        apartmentInvitation.setStatus(InvitationStatus.PENDING);
        invitationRepository.save(apartmentInvitation);
        apartmentInvitationDto.setId(apartmentInvitation.getId());
        return apartmentInvitationDto;
    }


    @Override
    public List<ApartmentInvitationDto> getAllActiveInvitations() {
        List<ApartmentInvitation> allNotSentInvitations = apartmentInvitationRepository.findAllBySentDatetimeIsNullAndApartmentNotNullAndStatusEquals(InvitationStatus.PENDING);
        return allNotSentInvitations.stream()
                .map(invitation -> mapper.convert(invitation, ApartmentInvitationDto.class))
                .collect(Collectors.toList());
    }
}
