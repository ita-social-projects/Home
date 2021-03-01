package com.softserveinc.ita.homeproject.homeservice.service.impl;

import com.softserveinc.ita.homeproject.homedata.entity.House;
import com.softserveinc.ita.homeproject.homedata.repository.HouseRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.HouseDto;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.service.HouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HouseServiceImpl implements HouseService {

    private final HouseRepository houseRepository;
    private final ServiceMapper mapper;

    @Override
    public HouseDto createHouse(Long cooperationId, HouseDto createHouseDto) {
        House house = mapper.convert(createHouseDto, House.class);
        houseRepository.save(house);
        return mapper.convert(house, HouseDto.class);
    }

    @Override
    public HouseDto updateHouse(Long id, HouseDto updateHouseDto) {
        return null;
    }

    @Override
    public Page<HouseDto> getAllHouses(Long cooperationId, Integer pageNumber, Integer pageSize) {
        return null;
    }

    @Override
    public HouseDto getHouseById(Long id) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }
}
