package com.softserveinc.ita.homeproject.homeservice.service.impl;

import com.softserveinc.ita.homeproject.homedata.entity.Cooperation;
import com.softserveinc.ita.homeproject.homedata.entity.House;
import com.softserveinc.ita.homeproject.homedata.repository.CooperationRepository;
import com.softserveinc.ita.homeproject.homedata.repository.HouseRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.CooperationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.HouseDto;
import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundHomeException;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.service.CooperationService;
import com.softserveinc.ita.homeproject.homeservice.service.HouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HouseServiceImpl implements HouseService {

    private final HouseRepository houseRepository;
    private final ServiceMapper mapper;
    private final CooperationRepository cooperationRepository;

    @Transactional
    @Override
    public HouseDto createHouse(Long cooperationId, HouseDto createHouseDto) {
        House house = mapper.convert(createHouseDto, House.class);
        Cooperation cooperation = cooperationRepository.findById(cooperationId).get();
        house.setCooperation(cooperation);
        house.setCreateDate(LocalDateTime.now());
        houseRepository.save(house);
        return mapper.convert(house, HouseDto.class);
    }

    @Override
    public HouseDto updateHouse(Long id, HouseDto updateHouseDto) {
        if (houseRepository.findById(id).isPresent()) {
            House fromDb = houseRepository.findById(id).get();

            if (updateHouseDto.getQuantityFlat() != null) {
                fromDb.setQuantityFlat(updateHouseDto.getQuantityFlat());
            }
            if (updateHouseDto.getAdjoiningArea() != null) {
                fromDb.setAdjoiningArea(updateHouseDto.getQuantityFlat());
            }
            if (updateHouseDto.getHouseArea() != null) {
                fromDb.setHouseArea(updateHouseDto.getHouseArea());
            }
            if (updateHouseDto.getAddress() != null) {
                fromDb.setAddress(updateHouseDto.getAddress());
            }
            fromDb.setUpdateDate(LocalDateTime.now());
            houseRepository.save(fromDb);
            return mapper.convert(fromDb, HouseDto.class);
        } else {
            throw new NotFoundHomeException("Can't find house with given ID:" + id);
        }
    }

    @Override
    public Page<HouseDto> getAllHouses(Integer pageNumber, Integer pageSize, Specification<House> specification) {
        Specification<House> houseSpecification = specification
                .and((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("enabled"), true));
        return houseRepository.findAll(houseSpecification, PageRequest.of(pageNumber - 1, pageSize))
                .map(house -> mapper.convert(house, HouseDto.class));
    }

    @Override
    public HouseDto getHouseById(Long coopId, Long id) {
        House toGet = houseRepository.findById(id)
                .orElseThrow(() -> new NotFoundHomeException("House with id: " + id + " is not found"));
        if (!toGet.getCooperation().getId().equals(coopId)) {
            throw new NotFoundHomeException("House with id: " + id + " not found in this cooperation");
        }
        return mapper.convert(toGet, HouseDto.class);
    }


    @Override
    public void deleteById(Long coopId, Long id) {
        House toDelete = houseRepository.findById(id)
                .orElseThrow(() -> new NotFoundHomeException("House with id: " + id + " is not found"));
        if (!toDelete.getCooperation().getId().equals(coopId)){
            throw new NotFoundHomeException("House with id: " + id + " not found in this cooperation");
        }
        houseRepository.delete(toDelete);
    }
}
