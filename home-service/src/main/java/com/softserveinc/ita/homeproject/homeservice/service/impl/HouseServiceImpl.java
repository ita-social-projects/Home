package com.softserveinc.ita.homeproject.homeservice.service.impl;

import com.softserveinc.ita.homeproject.homedata.entity.Houses;
import com.softserveinc.ita.homeproject.homedata.repository.HousesRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.HouseDto;
import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundHomeException;
import com.softserveinc.ita.homeproject.homeservice.mapperentity.HouseMapper;
import com.softserveinc.ita.homeproject.homeservice.service.HouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class HouseServiceImpl implements HouseService {
    private final HousesRepository housesRepository;
    private final HouseMapper houseMapper;


    @Override
    @Transactional
    public HouseDto createHouse(HouseDto createHouseDto) {
            Houses houses = houseMapper.convertDtoToEntity(createHouseDto);
            housesRepository.save(houses);
            return houseMapper.convertEntityToDto(houses);
        }


    @Override
    @Transactional
    public HouseDto updateHouse(Long id, HouseDto updateHouseDto) {
        if (housesRepository.findById(id).isPresent()) {

            Houses fromDB = housesRepository.findById(id).get();
//            if (updateHouseDto.getAddresses() != null) {
//                fromDB.setAddress(updateHouseDto.getAddresses());
//            }

            if (updateHouseDto.getQuantityFlat() != null) {
                fromDB.setQuantityFlat(updateHouseDto.getQuantityFlat());
            }

            if (updateHouseDto.getHouseArea() != null) {
                fromDB.setHouseArea(updateHouseDto.getHouseArea());
            }

            if (updateHouseDto.getAdjoiningArea() != null) {
                fromDB.setAdjoiningArea(updateHouseDto.getAdjoiningArea());
            }

            housesRepository.save(fromDB);
            return houseMapper.convertEntityToDto(fromDB);

        } else {
            throw new NotFoundHomeException("Can't find house with given ID:" + id);
        }
    }

    @Override
    public Page<HouseDto> getAllHouses(Integer pageNumber, Integer pageSize) {
        return housesRepository.findAll(PageRequest.of(pageNumber - 1, pageSize))
                .map(houseMapper::convertEntityToDto);
    }

    @Override
    public void deleteById(Long houseId) {
        housesRepository.findById(houseId)
                .orElseThrow(() -> new NotFoundHomeException("Can't find house with given ID:" + houseId));
        housesRepository.deleteById(houseId);
    }


    @Override
    public HouseDto getHouseById(Long id) {
        Houses housesResponse = housesRepository.findById(id)
                .orElseThrow(() -> new NotFoundHomeException("House with id:" + id + " is not found"));
        return houseMapper.convertEntityToDto(housesResponse);
    }

}
