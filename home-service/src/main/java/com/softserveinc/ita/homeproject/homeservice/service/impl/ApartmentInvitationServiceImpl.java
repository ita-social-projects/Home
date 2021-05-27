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
import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundHomeException;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.service.ApartmentInvitationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ApartmentInvitationServiceImpl extends InvitationServiceImpl implements ApartmentInvitationService {

    private final ApartmentRepository apartmentRepository;

    @Override
    public ApartmentInvitation getInvitation(Long apartmentId, Long id) {
        return apartmentRepository.findById(apartmentId)
                .orElseThrow(() ->
                        new NotFoundHomeException("Apartment with id: " + apartmentId + "not exist."))
                .getInvitations()
                .stream()
                .filter((invitation) -> invitation.getId().equals(id))
                .findFirst()
                .orElseThrow(() ->
                        new NotFoundHomeException(
                                "Invitation with id: " + id + "for apartment with id: " + apartmentId + "not exist."));
    }

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
        apartmentInvitation.setStatus(InvitationStatus.PENDING);
        invitationRepository.save(apartmentInvitation);
        return apartmentInvitationDto;
    }


    @Override
    public List<ApartmentInvitationDto> getAllActiveInvitations() {
        List<ApartmentInvitation> allNotSentInvitations = apartmentInvitationRepository
                .findAllBySentDatetimeIsNullAndApartmentNotNullAndStatusEquals(
                        InvitationStatus.PENDING);
        return allNotSentInvitations.stream()
                .map(invitation -> mapper.convert(invitation, ApartmentInvitationDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deactivateInvitationById(Long apartmentId, Long id) {
        var apartmentInvitation = getInvitation(apartmentId, id);
        apartmentInvitation.setStatus(InvitationStatus.DEACTIVATED);
        apartmentInvitationRepository.save(apartmentInvitation);
    }

    @Transactional
    @Override
    public Page<ApartmentInvitationDto> findAll(Integer pageNumber,
                                                Integer pageSize,
                                                Specification<ApartmentInvitation> specification) {
        Specification<ApartmentInvitation> apartmentInvitationSpecification = specification
                .and((root, criteriaQuery, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get("enabled"), true));
        return apartmentInvitationRepository
                .findAll(apartmentInvitationSpecification, PageRequest
                        .of(pageNumber - 1, pageSize))
                .map(invitation -> mapper.convert(invitation, ApartmentInvitationDto.class));
    }
}
