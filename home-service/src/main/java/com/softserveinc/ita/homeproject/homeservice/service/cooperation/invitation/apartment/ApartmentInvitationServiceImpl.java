package com.softserveinc.ita.homeproject.homeservice.service.cooperation.invitation.apartment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.uuid.Generators;
import com.querydsl.jpa.impl.JPAQuery;
import com.softserveinc.ita.homeproject.homedata.cooperation.apatment.ApartmentRepository;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.Invitation;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.InvitationRepository;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.apartment.ApartmentInvitation;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.apartment.ApartmentInvitationRepository;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.apartment.QApartmentInvitation;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.enums.InvitationStatus;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.enums.InvitationType;
import com.softserveinc.ita.homeproject.homedata.user.ownership.Ownership;
import com.softserveinc.ita.homeproject.homedata.user.ownership.OwnershipRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation.InvitationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation.apartment.ApartmentInvitationDto;
import com.softserveinc.ita.homeproject.homeservice.exception.BadRequestHomeException;
import com.softserveinc.ita.homeproject.homeservice.exception.InvitationException;
import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundHomeException;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.service.cooperation.invitation.InvitationServiceImpl;
import com.softserveinc.ita.homeproject.homeservice.service.user.UserCooperationService;
import com.softserveinc.ita.homeproject.homeservice.service.user.ownership.OwnershipService;
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

    private final OwnershipService ownershipService;

    private final UserCooperationService userCooperationService;

    private static final String INVALID_SUM_OWNERSHIP_AREA = "Entered sum of ownership parts = %.3f "
        + "The sum of the entered ownership parts cannot be greater than 1";

    public ApartmentInvitationServiceImpl(InvitationRepository invitationRepository,
                                          ServiceMapper mapper,
                                          ApartmentInvitationRepository apartmentInvitationRepository,
                                          OwnershipRepository ownershipRepository,
                                          ApartmentRepository apartmentRepository,
                                          OwnershipService ownershipService,
                                          UserCooperationService userCooperationService) {
        super(invitationRepository, mapper);
        this.apartmentInvitationRepository = apartmentInvitationRepository;
        this.ownershipRepository = ownershipRepository;
        this.apartmentRepository = apartmentRepository;
        this.ownershipService = ownershipService;
        this.userCooperationService = userCooperationService;
    }

    @Override
    public ApartmentInvitation updateInvitation(Long apartmentId,
                                                Long id,
                                                ApartmentInvitationDto updateInvitationDto) {
        ApartmentInvitation toUpdate = apartmentInvitationRepository.findById(id)
            .filter(invitation -> invitation.getSentDatetime() == null
                && invitation.getEnabled().equals(true)
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
        BigDecimal sumOfOwnerPartsWithNewInput = ownershipRepository.findAllByApartmentId(apartmentId)
            .stream()
            .filter(Ownership::getEnabled)
            .map(Ownership::getOwnershipPart)
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .add(getAllActiveInvitationsByApartmentId(apartmentId))
            .subtract(toUpdate.getOwnershipPart())
            .add(updateOwnershipDto.getOwnershipPart());

        if (sumOfOwnerPartsWithNewInput.compareTo(BigDecimal.valueOf(1)) > 0) {
            throw new BadRequestHomeException(String.format(INVALID_SUM_OWNERSHIP_AREA, sumOfOwnerPartsWithNewInput));
        }
    }

    private void validateSumOwnershipPart(Long apartmentId,
                                          ApartmentInvitation toCreate) {
        BigDecimal sumOfOwnerPartsWithNewInput = ownershipRepository.findAllByApartmentId(apartmentId)
            .stream()
            .filter(Ownership::getEnabled)
            .map(Ownership::getOwnershipPart)
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .add(getAllActiveInvitationsByApartmentId(apartmentId))
            .add(toCreate.getOwnershipPart());

        if (sumOfOwnerPartsWithNewInput.compareTo(BigDecimal.valueOf(1)) > 0) {
            throw new BadRequestHomeException(String.format(INVALID_SUM_OWNERSHIP_AREA, sumOfOwnerPartsWithNewInput));
        }
    }

    private BigDecimal getAllActiveInvitationsByApartmentId(Long apartmentId) {
        return Stream.concat(apartmentInvitationRepository
                                .findAllByApartmentIdAndStatus(apartmentId, InvitationStatus.PROCESSING)
                                .stream().filter(invitation -> invitation.getEnabled().equals(true)),
                        apartmentInvitationRepository
                                .findAllByApartmentIdAndStatus(apartmentId, InvitationStatus.PENDING)
                                .stream().filter(invitation -> invitation.getEnabled().equals(true)))
            .map(ApartmentInvitation::getOwnershipPart)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public InvitationDto saveInvitation(InvitationDto invitationDto) {
        var apartmentInvitation = mapper
            .convert(invitationDto, ApartmentInvitation.class);
        apartmentInvitation.setStatus(InvitationStatus.PENDING);
        apartmentInvitation.setRegistrationToken(Generators.timeBasedGenerator().generate().toString());
        apartmentInvitation.setRequestEndTime(LocalDateTime.now().plusDays(EXPIRATION_TERM));
        apartmentInvitation.setEnabled(true);

        var apartmentId = mapper.convert(apartmentInvitation, ApartmentInvitationDto.class).getApartmentId();
        apartmentInvitation.setApartment(apartmentRepository
            .findById(apartmentId)
            .orElseThrow(() -> new NotFoundHomeException("Apartment with id: " + apartmentId + " not found")));

        if (isApartmentInvitationNonExists(invitationDto.getEmail(), apartmentId)) {
            validateSumOwnershipPart(apartmentId, apartmentInvitation);
            invitationRepository.save(apartmentInvitation);
            return mapper.convert(apartmentInvitation, ApartmentInvitationDto.class);
        }
        throw new BadRequestHomeException("Invitation already exist for apartment");
    }

    @Override
    public void acceptUserInvitation(Invitation invitation) {
        var ownership = ownershipService.createOwnership((ApartmentInvitation) invitation);
        userCooperationService.createUserCooperationForOwnership(ownership);
        invitation.setStatus(InvitationStatus.ACCEPTED);
        invitationRepository.save(invitation);
    }

    private boolean isApartmentInvitationNonExists(String email, Long id) {
        return apartmentInvitationRepository.findApartmentInvitationsByEmail(email).stream()
            .filter(invitation -> invitation.getStatus().equals(InvitationStatus.PROCESSING)
                || invitation.getStatus().equals(InvitationStatus.ACCEPTED))
            .filter(invitation -> invitation.getType().equals(InvitationType.APARTMENT))
            .filter(invitation -> invitation.getApartment().getId().equals(id)).findAny().isEmpty();
    }

    @Override
    public List<ApartmentInvitationDto> getAllActiveInvitations() {
        List<ApartmentInvitation> allNotSentInvitations = apartmentInvitationRepository
            .findAllBySentDatetimeIsNullAndStatusEquals(
                InvitationStatus.PENDING);
        return allNotSentInvitations.stream()
            .map(invitation -> mapper.convert(invitation, ApartmentInvitationDto.class))
            .collect(Collectors.toList());
    }

    @Override
    public void deactivateInvitationById(Long apartmentId, Long id) {
        ApartmentInvitation apartmentInvitation = apartmentInvitationRepository.findById(id)
            .filter(invitation -> invitation.getEnabled() != null
                    & invitation.getEnabled().equals(true)
                    & invitation.getApartment().getId().equals(apartmentId))
            .orElseThrow(() ->
                new NotFoundHomeException("Invitation with id:" + id + "not found."));
        if (apartmentInvitation.getStatus().equals(InvitationStatus.ACCEPTED)) {
            throw new BadRequestHomeException("Deactivating invitations with status ACCEPTED is forbidden");
        }
        apartmentInvitation.setEnabled(false);
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

    @Override
    public void markInvitationsAsOverdue() {
        var qApartmentInvitation = QApartmentInvitation.apartmentInvitation;
        JPAQuery<?> query = new JPAQuery<>(entityManager);
        var overdueApartmentInvitations = query.select(qApartmentInvitation).from(qApartmentInvitation)
            .where((qApartmentInvitation.status.eq(InvitationStatus.PENDING))
                    .or(qApartmentInvitation.status.eq(InvitationStatus.PROCESSING)),
                qApartmentInvitation.requestEndTime.before(LocalDateTime.now())).fetch();
        overdueApartmentInvitations.forEach(invitation -> {
            invitation.setStatus(InvitationStatus.OVERDUE);
            apartmentInvitationRepository.save(invitation);
        });
    }
}
