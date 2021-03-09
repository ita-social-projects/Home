package com.softserveinc.ita.homeproject.homeservice.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.softserveinc.ita.homeproject.homedata.entity.Cooperation;
import com.softserveinc.ita.homeproject.homedata.entity.House;
import com.softserveinc.ita.homeproject.homedata.repository.CooperationRepository;
import com.softserveinc.ita.homeproject.homedata.repository.HouseRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.CooperationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.HouseDto;
import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundHomeException;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.query.EntitySpecificationService;
import com.softserveinc.ita.homeproject.homeservice.service.CooperationService;
import com.softserveinc.ita.homeproject.homeservice.service.HouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CooperationServiceImpl implements CooperationService {

    private final CooperationRepository cooperationRepository;
    private final HouseService houseService;
    private final ServiceMapper mapper;
    private final EntitySpecificationService entitySpecificationService;
    private final HouseRepository houseRepository;

    @Transactional
    @Override
    public CooperationDto createCooperation(CooperationDto createCooperationDto) {
        Cooperation cooperation = mapper.convert(createCooperationDto, Cooperation.class);
        cooperation.setEnabled(true);
        cooperation.setRegisterDate(LocalDate.now());
//        List<HouseDto> houseDtoList = createCooperationDto.getHouses();
//        cooperation.setHouses(new ArrayList<>());
//        for (HouseDto houseDto: houseDtoList){
//            HouseDto createdHouseDto = houseService.createHouse(createCooperationDto.getId(), houseDto);
//            House createdHouse = mapper.convert(createdHouseDto, House.class);
//            cooperation.getHouses().add(createdHouse);
//        }
        cooperationRepository.save(cooperation);
        CooperationDto createdCoopDto = mapper.convert(cooperation, CooperationDto.class);
        if (createCooperationDto.getHouses() != null) {
            createdCoopDto.setHouses(new ArrayList<>());
            List<HouseDto> houseDtoList = createCooperationDto.getHouses();
            for (HouseDto houseDto : houseDtoList) {
                HouseDto createdHouseDto = houseService.createHouse(cooperation.getId(), houseDto);
                createdCoopDto.getHouses().add(createdHouseDto);
            }
        }
        return createdCoopDto;
    }

    @Override
    public CooperationDto updateCooperation(Long id, CooperationDto updateCooperationDto) {
        if (cooperationRepository.findById(id).isPresent()) {
            Cooperation fromDb = cooperationRepository.findById(id).get();

            if (updateCooperationDto.getName() != null) {
                fromDb.setName(updateCooperationDto.getName());
            }
            if (updateCooperationDto.getUsreo() != null) {
                fromDb.setUsreo(updateCooperationDto.getUsreo());
            }
            if (updateCooperationDto.getIban() != null) {
                fromDb.setIban(updateCooperationDto.getIban());
            }
//            if (updateCooperationDto.getHouses() != null) {
//                fromDb.setHouses(updateCooperationDto.getHouses());
//            }
            if (updateCooperationDto.getAddress() != null) {
                fromDb.setAddress(updateCooperationDto.getAddress());
            }
            fromDb.setUpdateDate(LocalDateTime.now());
            cooperationRepository.save(fromDb);
            return mapper.convert(fromDb, CooperationDto.class);
        } else {
            throw new NotFoundHomeException("Can't find cooperation with given ID:" + id);
        }
    }

    @Override
    public Page<CooperationDto> getAllCooperation(Integer pageNumber, Integer pageSize,
                                                  Specification<Cooperation> specification) {
        Specification<Cooperation> cooperationSpecification = specification
                .and((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("enabled"), true));
        return cooperationRepository.findAll(cooperationSpecification, PageRequest.of(pageNumber - 1, pageSize))
                .map(cooperation -> mapper.convert(cooperation, CooperationDto.class));
    }

    @Override
    public CooperationDto getCooperationById(Long id) {
        Cooperation toGet = cooperationRepository.findById(id)
                .orElseThrow(() -> new NotFoundHomeException("Cooperation with id: " + id + "is not found"));
        List<House> houseList = houseRepository.findHousesByCooperation(toGet);
        toGet.setHouses(houseList);
        return mapper.convert(toGet, CooperationDto.class);
    }

    @Override
    public void deactivateCooperation(Long id) {
        Cooperation toDelete = cooperationRepository.findById(id)
                .orElseThrow(() -> new NotFoundHomeException("Cooperation with id: " + id + "is not found"));
        toDelete.setEnabled(false);
        cooperationRepository.save(toDelete);
    }
}
