package com.softserveinc.ita.homeproject.homeservice.service.impl;

import com.softserveinc.ita.homeproject.homedata.entity.Cooperation;
import com.softserveinc.ita.homeproject.homedata.repository.CooperationRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.CooperationDto;
import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundHomeException;
import com.softserveinc.ita.homeproject.homeservice.mapperentity.CooperationMapper;
import com.softserveinc.ita.homeproject.homeservice.service.CooperationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CooperationServiceImpl implements CooperationService {

    private final CooperationRepository cooperationRepository;
    private final CooperationMapper cooperationMapper;

    @Override
    @Transactional
    public CooperationDto createCooperation(CooperationDto createCooperationDto) {

        Cooperation cooperation = cooperationMapper.convertDtoToEntity(createCooperationDto);
        cooperation.setIsActive(true);
        cooperation.setRegisterDate(LocalDateTime.now());

        cooperationRepository.save(cooperation);

        return cooperationMapper.convertEntityToDto(cooperation);
    }

    @Override
    public CooperationDto updateCooperation(Long id, CooperationDto updateCooperationDto) {

        if (cooperationRepository.findById(id).isPresent()) {

            Cooperation fromDB = cooperationRepository.findById(id).get();

            if (updateCooperationDto.getName() != null) {
                fromDB.setName(updateCooperationDto.getName());
            }

            if (updateCooperationDto.getPhoneCooperations() != null) {
                fromDB.setPhoneCooperations(updateCooperationDto.getPhoneCooperations());
            }

            if (updateCooperationDto.getEmailCooperations() != null) {
                fromDB.setEmailCooperations(updateCooperationDto.getEmailCooperations());
            }

            if (updateCooperationDto.getAddressCooperation() != null) {
                fromDB.setAddressCooperation(updateCooperationDto.getAddressCooperation());
            }

            if (updateCooperationDto.getUSREO() != null) {
                fromDB.setUSREO(updateCooperationDto.getUSREO());
            }

            if (updateCooperationDto.getIBAN() != null) {
                fromDB.setIBAN(updateCooperationDto.getIBAN());
            }

            if (updateCooperationDto.getHouses() != null) {
                fromDB.setHouses(updateCooperationDto.getHouses());
            }

            fromDB.setUpdateDate(LocalDateTime.now());
            cooperationRepository.save(fromDB);
            return cooperationMapper.convertEntityToDto(fromDB);

        } else {
            throw new NotFoundHomeException("Can't find osbb with given ID:" + id);
        }
    }

    @Override
    public Page<CooperationDto> getAllCooperation(Integer pageNumber, Integer pageSize) {
        return cooperationRepository.findAllByEnabledTrue(PageRequest.of(pageNumber - 1, pageSize))
                .map(cooperationMapper::convertEntityToDto);
    }

    @Override
    public CooperationDto getCooperationById(Long id) {
        Cooperation cooperationResponse = cooperationRepository.findById(id)
                .orElseThrow(() -> new NotFoundHomeException("Osbb with id:" + id + " is not found"));
        return cooperationMapper.convertEntityToDto(cooperationResponse);
    }

    @Override
    public void deactivateCooperation(Long id) {
        Cooperation osbbDelete = cooperationRepository.findById(id)
                .orElseThrow(() -> new NotFoundHomeException("Osbb with id:" + id + " is not found"));
        osbbDelete.setIsActive(false);
        cooperationRepository.save(osbbDelete);
    }

}
