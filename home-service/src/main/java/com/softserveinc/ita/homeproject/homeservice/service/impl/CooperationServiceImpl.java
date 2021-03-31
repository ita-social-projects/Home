package com.softserveinc.ita.homeproject.homeservice.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import com.softserveinc.ita.homeproject.homedata.entity.Cooperation;
import com.softserveinc.ita.homeproject.homedata.entity.House;
import com.softserveinc.ita.homeproject.homedata.repository.CooperationRepository;
import com.softserveinc.ita.homeproject.homedata.repository.HouseRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.CooperationDto;
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
        cooperation.getHouses().forEach(element -> {
            element.setCooperation(cooperation);
            element.setCreateDate(LocalDateTime.now());
            element.setEnabled(true);
        });
        cooperationRepository.save(cooperation);
        return mapper.convert(cooperation, CooperationDto.class);
    }

    @Transactional
    @Override
    public CooperationDto updateCooperation(Long id, CooperationDto updateCooperationDto) {
        Optional<Cooperation> optional = cooperationRepository.findById(id).filter(Cooperation::getEnabled);
        if (optional.isPresent()) {
            Cooperation fromDb = optional.get();

            if (updateCooperationDto.getName() != null) {
                fromDb.setName(updateCooperationDto.getName());
            }
            if (updateCooperationDto.getUsreo() != null) {
                fromDb.setUsreo(updateCooperationDto.getUsreo());
            }
            if (updateCooperationDto.getIban() != null) {
                fromDb.setIban(updateCooperationDto.getIban());
            }
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

    @Transactional
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
        Cooperation toGet = cooperationRepository.findById(id).filter(Cooperation::getEnabled)
            .orElseThrow(() -> new NotFoundHomeException("Cooperation with id: " + id + "is not found"));
        List<House> houseList = houseRepository.findHousesByCooperationId(toGet.getId());
        toGet.setHouses(houseList);
        return mapper.convert(toGet, CooperationDto.class);
    }

    @Override
    public void deactivateCooperation(Long id) {
        Cooperation toDelete = cooperationRepository.findById(id).filter(Cooperation::getEnabled)
            .orElseThrow(() -> new NotFoundHomeException("Cooperation with id: " + id + "is not found"));
        toDelete.setEnabled(false);
        cooperationRepository.save(toDelete);
    }
}
