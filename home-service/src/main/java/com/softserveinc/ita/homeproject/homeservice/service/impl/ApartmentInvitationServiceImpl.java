package com.softserveinc.ita.homeproject.homeservice.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import com.softserveinc.ita.homeproject.homedata.entity.ApartmentInvitation;
import com.softserveinc.ita.homeproject.homedata.entity.InvitationStatus;
import com.softserveinc.ita.homeproject.homedata.entity.Ownership;
import com.softserveinc.ita.homeproject.homedata.repository.ApartmentInvitationRepository;
import com.softserveinc.ita.homeproject.homedata.repository.ApartmentRepository;
import com.softserveinc.ita.homeproject.homedata.repository.InvitationRepository;
import com.softserveinc.ita.homeproject.homedata.repository.OwnershipRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.ApartmentInvitationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.InvitationDto;
import com.softserveinc.ita.homeproject.homeservice.exception.BadRequestHomeException;
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

    private final ApartmentInvitationRepository apartmentInvitationRepository;

    private final OwnershipRepository ownershipRepository;

    private final ApartmentRepository apartmentRepository;

    public ApartmentInvitationServiceImpl(InvitationRepository invitationRepository,
                                          ServiceMapper mapper,
                                          ApartmentInvitationRepository apartmentInvitationRepository,
                                          OwnershipRepository ownershipRepository,
                                          ApartmentRepository apartmentRepository) {
        super(invitationRepository, mapper);
        this.apartmentInvitationRepository = apartmentInvitationRepository;
        this.ownershipRepository = ownershipRepository;
        this.apartmentRepository = apartmentRepository;
    }

    @Override
    public ApartmentInvitation updateInvitation(Long apartmentId,
                                                Long id,
                                                ApartmentInvitationDto updateInvitationDto) {
        ApartmentInvitation toUpdate = apartmentInvitationRepository.findById(id)
                .filter(invitation -> invitation.getSentDatetime() == null
                        && invitation.getApartment().getId().equals(apartmentId)
                        && invitation.getStatus().equals(InvitationStatus.PENDING))
                .orElseThrow(() ->
                        new NotFoundHomeException("Invitation with id:" + id + "not found."));

        validateSumOwnershipPart(apartmentId, toUpdate, updateInvitationDto);

        toUpdate.setOwnershipPart(updateInvitationDto.getOwnershipPart());
        toUpdate.setEmail(updateInvitationDto.getEmail());

        invitationRepository.save(toUpdate);
        return toUpdate;
    }

    private void validateSumOwnershipPart(Long apartmentId,
                                          ApartmentInvitation toUpdate,
                                          ApartmentInvitationDto updateOwnershipDto) {
        BigDecimal activeInvitationsSumOwnerPart = apartmentInvitationRepository
                .findAllByApartmentIdAndStatus(apartmentId, InvitationStatus.PENDING)
                .stream()
                .map(ApartmentInvitation::getOwnershipPart)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal sumOfOwnerPartsWithNewInput = ownershipRepository.findAllByApartmentId(apartmentId)
                .stream()
                .filter(Ownership::getEnabled)
                .map(Ownership::getOwnershipPart)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .add(activeInvitationsSumOwnerPart)
                .subtract(toUpdate.getOwnershipPart())
                .add(updateOwnershipDto.getOwnershipPart());

        if (sumOfOwnerPartsWithNewInput.compareTo(BigDecimal.valueOf(1)) > 0) {
            throw new BadRequestHomeException(
                    "Entered sum of area = "
                            + sumOfOwnerPartsWithNewInput + " The sum of the entered area cannot be greater than 1");
        }

    }

    @Override
    public InvitationDto saveInvitation(InvitationDto invitationDto) {
        ApartmentInvitation apartmentInvitation = mapper
                .convert(invitationDto, ApartmentInvitation.class);
        apartmentInvitation.setStatus(InvitationStatus.PENDING);
        var apartmentId = mapper.convert(apartmentInvitation, ApartmentInvitationDto.class).getApartmentId();
        apartmentInvitation.setApartment(apartmentRepository
                .findById(apartmentId)
                .orElseThrow(() -> new NotFoundHomeException("Apartment with id: " + apartmentId + " not found")));
        invitationRepository.save(apartmentInvitation);
        return mapper.convert(apartmentInvitation, ApartmentInvitationDto.class);
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
        ApartmentInvitation apartmentInvitation = apartmentInvitationRepository.findById(id)
                .filter(invitation -> invitation.getSentDatetime() == null
                        && invitation.getApartment().getId().equals(apartmentId)
                        && invitation.getStatus().equals(InvitationStatus.PENDING))
                .orElseThrow(() ->
                        new NotFoundHomeException("Invitation with id:" + id + "not found."));
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

