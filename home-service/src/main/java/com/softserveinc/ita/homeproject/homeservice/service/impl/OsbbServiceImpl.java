package com.softserveinc.ita.homeproject.homeservice.service.impl;

import com.softserveinc.ita.homeproject.homedata.entity.Osbb;
import com.softserveinc.ita.homeproject.homedata.repository.OsbbRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.OsbbDto;
import com.softserveinc.ita.homeproject.homeservice.exception.AlreadyExistHomeException;
import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundHomeException;
import com.softserveinc.ita.homeproject.homeservice.mapperentity.OsbbMapper;
import com.softserveinc.ita.homeproject.homeservice.service.OsbbService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OsbbServiceImpl implements OsbbService {

    private final OsbbRepository osbbRepository;
    private final OsbbMapper osbbMapper;

    @Override
    @Transactional
    public OsbbDto createOsbb(OsbbDto createOsbbDto) {
        if (osbbRepository.findByFullName(createOsbbDto.getFullName()).isPresent()) {
            throw new AlreadyExistHomeException("Osbb with this name" + createOsbbDto.getFullName() + "is already exist");
        } else {
            Osbb osbb = osbbMapper.convertDtoToEntity(createOsbbDto);
            osbb.setIsActive(true);
            osbb.setRegisterDate(LocalDateTime.now());

            osbbRepository.save(osbb);

            return osbbMapper.convertEntityToDto(osbb);
        }
    }

    @Override
    public OsbbDto updateOsbb(Long id, OsbbDto updateOsbbDto) {

        if (osbbRepository.findById(id).isPresent()) {

            Osbb fromDB = osbbRepository.findById(id).get();

            if (updateOsbbDto.getShortName() != null) {
                fromDB.setShortName(updateOsbbDto.getShortName());
            }

            if (updateOsbbDto.getManagerPerson() != null) {
                fromDB.setManagerPerson(updateOsbbDto.getManagerPerson());
            }

            if (updateOsbbDto.getPhone() != null) {
                fromDB.setPhone(updateOsbbDto.getPhone());
            }

            if (updateOsbbDto.getEmail() != null) {
                fromDB.setEmail(updateOsbbDto.getEmail());
            }

            if (updateOsbbDto.getAddress() != null) {
                fromDB.setAddress(updateOsbbDto.getAddress());
            }

            if (updateOsbbDto.getCode() != null) {
                fromDB.setCode(updateOsbbDto.getCode());
            }

            if (updateOsbbDto.getAccount() != null) {
                fromDB.setAccount(updateOsbbDto.getAccount());
            }

            fromDB.setUpdateDate(LocalDateTime.now());
            osbbRepository.save(fromDB);
            return osbbMapper.convertEntityToDto(fromDB);

        } else {
            throw new NotFoundHomeException("Can't find osbb with given ID:" + id);
        }
    }

    @Override
    public Page<OsbbDto> getAllOsbb(Integer pageNumber, Integer pageSize) {
        return osbbRepository.findAllByEnabledTrue(PageRequest.of(pageNumber - 1, pageSize))
                .map(osbbMapper::convertEntityToDto);
    }

    @Override
    public OsbbDto getOsbbById(Long id) {
        Osbb osbbResponse = osbbRepository.findById(id)
                .orElseThrow(() -> new NotFoundHomeException("Osbb with id:" + id + " is not found"));
        return osbbMapper.convertEntityToDto(osbbResponse);
    }

    @Override
    public void deactivateOsbb(Long id) {
        Osbb osbbDelete = osbbRepository.findById(id)
                .orElseThrow(() -> new NotFoundHomeException("Osbb with id:" + id + " is not found"));
        osbbDelete.setIsActive(false);
        osbbRepository.save(osbbDelete);
    }

}
