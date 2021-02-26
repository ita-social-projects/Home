package com.softserveinc.ita.homeproject.homeservice.service.impl;

import java.time.LocalDateTime;
import com.softserveinc.ita.homeproject.homedata.entity.Cooperation;
import com.softserveinc.ita.homeproject.homedata.repository.CooperationRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.CooperationDto;
import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundHomeException;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.service.CooperationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CooperationServiceImpl implements CooperationService {

    private final CooperationRepository cooperationRepository;

    private final ServiceMapper mapper;

    @Override
    public CooperationDto createCooperation(CooperationDto createCooperationDto) {
        Cooperation cooperation = mapper.convert(createCooperationDto, Cooperation.class);
        cooperation.setEnabled(true);
        cooperation.setRegisterDate(LocalDateTime.now());
        cooperationRepository.save(cooperation);
        return mapper.convert(cooperation, CooperationDto.class);
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
            if (updateCooperationDto.getHouses() != null) {
                fromDb.setHouses(updateCooperationDto.getHouses());
            }
            fromDb.setUpdateDate(LocalDateTime.now());
            cooperationRepository.save(fromDb);
            return mapper.convert(fromDb, CooperationDto.class);
        } else {
            throw new NotFoundHomeException("Can't find osbb with given ID:" + id);
        }
    }

    @Override
    public Page<CooperationDto> getAllCooperation(Integer pageNumber, Integer pageSize) {
        return null;
    }

    @Override
    public CooperationDto getCooperationById(Long id) {
        return null;
    }

    @Override
    public void deactivateCooperation(Long id) {

    }
}
