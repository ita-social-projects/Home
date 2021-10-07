package com.softserveinc.ita.homeproject.homeservice.service.user.ownership;

import java.math.BigDecimal;

import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.apartment.ApartmentInvitation;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.apartment.ApartmentInvitationRepository;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.enums.InvitationStatus;
import com.softserveinc.ita.homeproject.homedata.user.UserRepository;
import com.softserveinc.ita.homeproject.homedata.user.ownership.Ownership;
import com.softserveinc.ita.homeproject.homedata.user.ownership.OwnershipRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.OwnershipDto;
import com.softserveinc.ita.homeproject.homeservice.exception.BadRequestHomeException;
import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundHomeException;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OwnershipServiceImpl implements OwnershipService {

    private final OwnershipRepository ownershipRepository;

    private final UserRepository userRepository;

    private final ApartmentInvitationRepository invitationRepository;

    private final ServiceMapper mapper;

    private static final String OWNERSHIP_WITH_ID_NOT_FOUND = "Ownership with 'id: %d' is not found";

    @Override
    public Ownership createOwnership(ApartmentInvitation apartmentInvitation) {
        var ownership = new Ownership();
        ownership.setOwnershipPart(apartmentInvitation.getOwnershipPart());
        ownership.setApartment(apartmentInvitation.getApartment());
        ownership.setCooperation(apartmentInvitation.getApartment().getHouse().getCooperation());

        userRepository.findByEmail(apartmentInvitation.getEmail())
                .ifPresent(ownership::setUser);

        ownership.setEnabled(true);
        ownershipRepository.save(ownership);

        return ownership;
    }

    @Transactional
    @Override
    public OwnershipDto updateOwnership(Long apartmentId, Long id, OwnershipDto updateOwnershipDto) {
        Ownership toUpdate = ownershipRepository.findById(id)
                .filter(Ownership::getEnabled)
                .filter(ownership -> ownership.getApartment().getId().equals(apartmentId))
                .orElseThrow(() ->
                        new NotFoundHomeException(String.format(OWNERSHIP_WITH_ID_NOT_FOUND, id)));

        validateSumOwnershipPart(apartmentId, toUpdate, updateOwnershipDto);

        toUpdate.setOwnershipPart(updateOwnershipDto.getOwnershipPart());

        ownershipRepository.save(toUpdate);
        return mapper.convert(toUpdate, OwnershipDto.class);
    }

    @Override
    public void deactivateOwnershipById(Long apartmentId, Long id) {
        Ownership toDelete = ownershipRepository.findById(id)
                .filter(Ownership::getEnabled)
                .filter(ownership -> ownership.getApartment().getId().equals(apartmentId))
                .orElseThrow(() ->
                        new NotFoundHomeException(String.format(OWNERSHIP_WITH_ID_NOT_FOUND, id)));


        toDelete.setEnabled(false);
        ownershipRepository.save(toDelete);
    }

    @Transactional
    @Override
    public Page<OwnershipDto> findAll(Integer pageNumber, Integer pageSize, Specification<Ownership> specification) {
        Specification<Ownership> ownershipSpecification = specification
                .and((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("enabled"), true));
        return ownershipRepository.findAll(ownershipSpecification, PageRequest.of(pageNumber - 1, pageSize))
                .map(ownership -> mapper.convert(ownership, OwnershipDto.class));
    }

    public void validateSumOwnershipPart(Long apartmentId, Ownership toUpdate, OwnershipDto updateOwnershipDto) {
        BigDecimal activeInvitationsSumOwnerPart = invitationRepository
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
                    "Entered sum of ownerships parts = " + sumOfOwnerPartsWithNewInput
                            + " The sum of the entered ownership parts "
                            + "cannot be greater than 1");
        }
    }
}
