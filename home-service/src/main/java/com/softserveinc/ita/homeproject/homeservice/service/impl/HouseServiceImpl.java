package com.softserveinc.ita.homeproject.homeservice.service.impl;

import java.time.LocalDateTime;

import com.softserveinc.ita.homeproject.homedata.entity.Cooperation;
import com.softserveinc.ita.homeproject.homedata.entity.House;
import com.softserveinc.ita.homeproject.homedata.repository.CooperationRepository;
import com.softserveinc.ita.homeproject.homedata.repository.HouseRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.CooperationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.HouseDto;
import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundHomeException;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.service.HouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HouseServiceImpl implements HouseService {

    private static final String HOUSE_WITH_ID_NOT_FOUND = "House with id: %d not found in this cooperation";

    private final HouseRepository houseRepository;

    private final ServiceMapper mapper;

    private final CooperationRepository cooperationRepository;

    @Transactional
    @Override
    public HouseDto createHouse(CooperationDto cooperationDto, HouseDto createHouseDto) {
        House house = mapper.convert(createHouseDto, House.class);
        Long cooperationId = cooperationDto.getId();
        Cooperation cooperation = cooperationRepository.findById(cooperationId)
            .filter(Cooperation::getEnabled)
            .orElseThrow(() -> new NotFoundHomeException(
                String.format("Can't find cooperation with given ID: %d", cooperationId)));
        house.setCooperation(cooperation);
        house.setCreateDate(LocalDateTime.now());
        house.setEnabled(true);
        houseRepository.save(house);

        return mapper.convert(house, HouseDto.class);
    }

    @Override
    public HouseDto updateHouse(HouseDto oldHouseDto, HouseDto updateHouseDto) {
        Long id = oldHouseDto.getId();
        House oldHouse = houseRepository.findById(id)
                .filter(House::getEnabled)
                .orElseThrow(() -> new NotFoundHomeException(String.format(HOUSE_WITH_ID_NOT_FOUND, id)));

        if (updateHouseDto.getQuantityFlat() != null) {
            oldHouse.setQuantityFlat(updateHouseDto.getQuantityFlat());
        }
        if (updateHouseDto.getAdjoiningArea() != null) {
            oldHouse.setAdjoiningArea(updateHouseDto.getAdjoiningArea());
        }
        if (updateHouseDto.getHouseArea() != null) {
            oldHouse.setHouseArea(updateHouseDto.getHouseArea());
        }
        if (updateHouseDto.getAddress() != null) {
            oldHouse.setAddress(updateHouseDto.getAddress());
        }
        oldHouse.setUpdateDate(LocalDateTime.now());
        houseRepository.save(oldHouse);
        return mapper.convert(oldHouse, HouseDto.class);
    }

    @Override
    public Page<HouseDto> findAll(Integer pageNumber, Integer pageSize, Specification<House> specification) {
        return houseRepository.findAll(specification, PageRequest.of(pageNumber - 1, pageSize))
            .map(house -> mapper.convert(house, HouseDto.class));
    }

    @Override
    public void deactivate(HouseDto houseDto) {
        Long id = houseDto.getId();
        House toDelete = houseRepository.findById(id)
                .filter(House::getEnabled)
                .orElseThrow(() -> new NotFoundHomeException(String.format(HOUSE_WITH_ID_NOT_FOUND, id)));
        toDelete.setEnabled(false);
        houseRepository.save(toDelete);
    }
}
