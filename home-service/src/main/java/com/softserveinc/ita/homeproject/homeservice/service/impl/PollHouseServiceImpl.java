package com.softserveinc.ita.homeproject.homeservice.service.impl;

import java.util.List;

import com.softserveinc.ita.homeproject.homedata.entity.House;
import com.softserveinc.ita.homeproject.homedata.entity.Poll;
import com.softserveinc.ita.homeproject.homedata.entity.PollStatus;
import com.softserveinc.ita.homeproject.homedata.repository.HouseRepository;
import com.softserveinc.ita.homeproject.homedata.repository.PollRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.PollDto;
import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundHomeException;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.service.PollHouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PollHouseServiceImpl implements PollHouseService {
    private final PollRepository pollRepository;

    private final HouseRepository houseRepository;

    private final ServiceMapper mapper;

    @Override
    public PollDto add(Long houseId, Long pollId) {
        Poll poll = getEnablePollById(pollId);
        House house = getHouseById(houseId);

        List<House> houses = poll.getPolledHouses();
        houses.add(house);

        poll.setPolledHouses(houses);
        pollRepository.save(poll);
        return mapper.convert(poll, PollDto.class);
    }

    @Override
    public void remove(Long houseId, Long pollId) {
        Poll poll = pollRepository.findById(pollId).filter(Poll::getEnabled)
            .orElseThrow(() -> new NotFoundHomeException(String.format(NOT_FOUND_MESSAGE, "Poll", pollId)));
        House house = getHouseById(houseId);

        List<House> houses = poll.getPolledHouses();
        houses.remove(house);
        pollRepository.save(poll);
    }

    //TODO validator to understand is our house in the same cooperation

    @Override
    public Page<PollDto> findAll(Integer pageNumber, Integer pageSize, Specification<Poll> specification) {
        specification = specification.and((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder
            .equal(root.get("polledHouses").get("enabled"), true));
        return pollRepository.findAll(specification, PageRequest.of(pageNumber - 1, pageSize))
            .map(news -> mapper.convert(news, PollDto.class));
    }

    private Poll getEnablePollById(Long id) {
        return pollRepository.findById(id).filter(Poll::getEnabled).filter(p -> p.getStatus().equals(PollStatus.DRAFT))
            .orElseThrow(() -> new NotFoundHomeException(String.format(NOT_FOUND_MESSAGE, "Poll", id)));
    }

    private House getHouseById(Long id) {
        return houseRepository.findById(id).filter(House::getEnabled)
            .orElseThrow(() -> new NotFoundHomeException(String.format(NOT_FOUND_MESSAGE, "House", id)));
    }

}
