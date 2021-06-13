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
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PollHouseServiceImpl implements PollHouseService {
    private static final String WRONG_HOUSE = "Can't add or remove house, invalid poll_id or house_id";

    private final PollRepository pollRepository;

    private final HouseRepository houseRepository;

    private final ServiceMapper mapper;

    @Override
    @Transactional
    public PollDto add(Long houseId, Long pollId) {
        Poll poll = getDraftPollById(pollId);
        House house = getHouseById(houseId);

        if (validateHouse(poll, house)){
            List<House> houses = poll.getPolledHouses();
            houses.add(house);

            poll.setPolledHouses(houses);
            pollRepository.save(poll);
        }

        return mapper.convert(poll, PollDto.class);
    }

    @Override
    @Transactional
    public void remove(Long houseId, Long pollId) {
        Poll poll = getDraftPollById(pollId);
        House house = getHouseById(houseId);

        if (validateHouse(poll, house)) {
            List<House> houses = poll.getPolledHouses();
            houses.remove(house);
            pollRepository.save(poll);
        }
    }

    @Override
    public Page<PollDto> findAll(Integer pageNumber, Integer pageSize, Specification<Poll> specification) {
        specification = specification.and((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder
            .equal(root.get("polledHouses").get("enabled"), true));
        return pollRepository.findAll(specification, PageRequest.of(pageNumber - 1, pageSize))
            .map(news -> mapper.convert(news, PollDto.class));
    }

    private Poll getDraftPollById(Long id) {
        return pollRepository.findById(id).filter(Poll::getEnabled).filter(p -> p.getStatus().equals(PollStatus.DRAFT))
            .orElseThrow(() -> new NotFoundHomeException(String.format(NOT_FOUND_MESSAGE, "Poll", id)));
    }

    private House getHouseById(Long id) {
        return houseRepository.findById(id).filter(House::getEnabled)
            .orElseThrow(() -> new NotFoundHomeException(String.format(NOT_FOUND_MESSAGE, "House", id)));
    }

    private Boolean validateHouse(Poll poll, House house) {
        Long pollCooperationId = poll.getCooperation().getId();
        Long houseCooperationId = house.getCooperation().getId();
        if (pollCooperationId.equals(houseCooperationId)) {
            return true;
        } else {
            throw new NotFoundHomeException(WRONG_HOUSE);
        }
    }

}
