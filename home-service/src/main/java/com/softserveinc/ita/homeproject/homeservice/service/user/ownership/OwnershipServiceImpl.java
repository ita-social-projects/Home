package com.softserveinc.ita.homeproject.homeservice.service.user.ownership;

import static com.softserveinc.ita.homeproject.homeservice.exception.ExceptionMessages.NOT_FOUND_OWNERSHIP_WITH_ID_MESSAGE;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.apartment.ApartmentInvitation;
import com.softserveinc.ita.homeproject.homedata.general.contact.Contact;
import com.softserveinc.ita.homeproject.homedata.user.User;
import com.softserveinc.ita.homeproject.homedata.user.UserRepository;
import com.softserveinc.ita.homeproject.homedata.user.ownership.Ownership;
import com.softserveinc.ita.homeproject.homedata.user.ownership.OwnershipRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.user.ownership.OwnershipDto;
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

    private final ServiceMapper mapper;

    @Override
    public Ownership createOwnership(ApartmentInvitation apartmentInvitation) {
        Ownership ownership = new Ownership();
        ownership.setOwnershipPart(BigDecimal.ZERO);
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
                        new NotFoundHomeException(String.format(NOT_FOUND_OWNERSHIP_WITH_ID_MESSAGE, id)));

        validateSumOwnershipPart(apartmentId, toUpdate, updateOwnershipDto);

        toUpdate.setOwnershipPart(new BigDecimal(updateOwnershipDto.getOwnershipPart()));

        ownershipRepository.save(toUpdate);
        return mapper.convert(toUpdate, OwnershipDto.class);
    }

    @Override
    public void deactivateOwnershipById(Long apartmentId, Long id) {
        Ownership toDelete = ownershipRepository.findById(id)
                .filter(Ownership::getEnabled)
                .filter(ownership -> ownership.getApartment().getId().equals(apartmentId))
                .orElseThrow(() ->
                        new NotFoundHomeException(String.format(NOT_FOUND_OWNERSHIP_WITH_ID_MESSAGE, id)));


        toDelete.setEnabled(false);
        ownershipRepository.save(toDelete);
    }

    @Transactional
    @Override
    public Page<OwnershipDto> findAll(Integer pageNumber, Integer pageSize, Specification<Ownership> specification) {
        Specification<Ownership> ownershipSpecification = specification
            .and((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("enabled"), true));
        Page<Ownership> ownerships = ownershipRepository.findAll(ownershipSpecification,
                PageRequest.of(pageNumber - 1, pageSize));
        List<User> users = ownerships.stream().map(Ownership::getUser).collect(Collectors.toList());
        users.forEach(user -> user.setContacts(user.getContacts().stream()
            .filter(Contact::getEnabled).collect(Collectors.toList())));
        return ownerships.map(ownership -> mapper.convert(ownership, OwnershipDto.class));
    }

    public void validateSumOwnershipPart(Long apartmentId, Ownership toUpdate, OwnershipDto updateOwnershipDto) {
        BigDecimal sumOfOwnerPartsWithNewInput = ownershipRepository.findAllByApartmentId(apartmentId)
                .stream()
                .filter(Ownership::getEnabled)
                .map(Ownership::getOwnershipPart)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .subtract(toUpdate.getOwnershipPart())
                .add(new BigDecimal(updateOwnershipDto.getOwnershipPart()));

        if (sumOfOwnerPartsWithNewInput.compareTo(BigDecimal.valueOf(1)) > 0) {
            throw new BadRequestHomeException(
                    "Entered sum of ownerships parts = " + sumOfOwnerPartsWithNewInput
                            + " The sum of the entered ownership parts "
                            + "cannot be greater than 1");
        }
    }
}
