package com.softserveinc.ita.homeproject.homeservice.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.softserveinc.ita.homeproject.homedata.entity.Apartment;
import com.softserveinc.ita.homeproject.homedata.entity.House;
import com.softserveinc.ita.homeproject.homedata.entity.InvitationStatus;
import com.softserveinc.ita.homeproject.homedata.repository.ApartmentRepository;
import com.softserveinc.ita.homeproject.homedata.repository.HouseRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.ApartmentDto;
import com.softserveinc.ita.homeproject.homeservice.dto.InvitationDto;
import com.softserveinc.ita.homeproject.homeservice.exception.BadRequestHomeException;
import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundHomeException;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.service.ApartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApartmentServiceImpl implements ApartmentService {

    private final ApartmentRepository apartmentRepository;

    private final HouseRepository houseRepository;

    private final ServiceMapper mapper;


    @Transactional
    @Override
    public ApartmentDto createApartment(Long houseId, ApartmentDto createApartmentDto) {
        var apartment = mapper.convert(createApartmentDto, Apartment.class);
        var house = houseRepository.findById(houseId)
                .filter(House::getEnabled)
                .orElseThrow(() -> new NotFoundHomeException(
                        String.format("Can't find house with given ID: %d", houseId)));

        BigDecimal invitationSummaryOwnerPart = createApartmentDto.getInvitations().stream()
                .map(InvitationDto::getOwnershipPart)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (invitationSummaryOwnerPart.compareTo(BigDecimal.valueOf(1)) > 0) {
            throw new BadRequestHomeException("The sum of the entered area of the apartment = " + invitationSummaryOwnerPart + ". Area cannot be greater than 1");
        }

        apartment.getInvitations().forEach(element -> {
            element.setApartment(apartment);
            // TODO change after invitation implementation will be added
            element.setSentDatetime(LocalDateTime.now());
            element.setStatus(InvitationStatus.PENDING);
            element.setName("apartment invite");
        });

        apartment.setHouse(house);
        apartment.setCreateDate(LocalDateTime.now());
        apartment.setEnabled(true);
        apartmentRepository.save(apartment);

        return mapper.convert(apartment, ApartmentDto.class);
    }


    @Transactional
    @Override
    public ApartmentDto getApartmentById(Long houseId, Long id) {
        Apartment toGet = apartmentRepository.findById(id).filter(Apartment::getEnabled)
                .orElseThrow(() ->
                        new NotFoundHomeException(String.format("Can't find apartment with given ID: %d", id)));
        if (!toGet.getHouse().getId().equals(houseId)) {
            throw new NotFoundHomeException(String.format("Can't find house with given ID: %d",
                    houseId));
        }
        return mapper.convert(toGet, ApartmentDto.class);
    }

    @Override
    @Transactional
    public Page<ApartmentDto> findAll(Integer pageNumber, Integer pageSize, Specification<Apartment> specification) {
        Specification<Apartment> apartmentSpecification = specification
                .and((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("enabled"), true));
        return apartmentRepository.findAll(apartmentSpecification, PageRequest.of(pageNumber - 1, pageSize))
                .map(apartment -> mapper.convert(apartment, ApartmentDto.class));
    }
}
