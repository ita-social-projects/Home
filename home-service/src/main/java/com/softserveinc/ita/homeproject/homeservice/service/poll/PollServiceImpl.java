package com.softserveinc.ita.homeproject.homeservice.service.poll;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import com.softserveinc.ita.homeproject.homedata.cooperation.Cooperation;
import com.softserveinc.ita.homeproject.homedata.cooperation.CooperationRepository;
import com.softserveinc.ita.homeproject.homedata.cooperation.house.House;
import com.softserveinc.ita.homeproject.homedata.cooperation.house.HouseRepository;
import com.softserveinc.ita.homeproject.homedata.poll.Poll;
import com.softserveinc.ita.homeproject.homedata.poll.PollRepository;
import com.softserveinc.ita.homeproject.homedata.poll.enums.PollStatus;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.house.HouseDto;
import com.softserveinc.ita.homeproject.homeservice.dto.poll.PollDto;
import com.softserveinc.ita.homeproject.homeservice.dto.poll.PollShortenDto;
import com.softserveinc.ita.homeproject.homeservice.dto.poll.enums.PollStatusDto;
import com.softserveinc.ita.homeproject.homeservice.exception.BadRequestHomeException;
import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundHomeException;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:home-service.properties")
public class PollServiceImpl implements PollService {

    private static final String COMPLETION_DATE_VALIDATION_MESSAGE =
            "Completion date of the poll has not to be less than %s days after creation";

    private static final String NOT_FOUND_MESSAGE = "%s with 'id: %s' is not found";

    private static final String POLL_STATUS_VALIDATION_MESSAGE = "Can't update or delete poll with status: '%s'";

    @Value("${home.service.min.poll.duration.in.days:2}")
    private int minPollDurationInDays;

    private final PollRepository pollRepository;

    private final HouseRepository houseRepository;

    private final CooperationRepository cooperationRepository;

    private final ServiceMapper mapper;

    @Override
    @Transactional
    public PollDto create(Long cooperationId, PollDto pollDto) {
        pollDto.getPolledHouses().forEach(houseDto -> validateHouse(cooperationId, houseDto));
        pollDto.setCompletionDate(pollDto.getCreationDate().plusDays(15));
        Poll poll = mapper.convert(pollDto, Poll.class);
        Cooperation cooperation = getCooperationById(cooperationId);
        validateCompletionDate(poll.getCompletionDate(), poll.getCreationDate());
        poll.setCooperation(cooperation);
        poll.setStatus(PollStatus.DRAFT);
        poll.setEnabled(true);
        pollRepository.save(poll);
        return mapper.convert(poll, PollDto.class);
    }

    @Override
    @Transactional
    public PollDto update(Long cooperationId, Long id, PollDto pollDto,String description,
                          List<Long> housesID, LocalDateTime creationDate) {
        Poll poll = pollRepository.findById(id)
                .filter(Poll::getEnabled)
                .filter(poll1 -> poll1.getCooperation().getId().equals(cooperationId))
                .orElseThrow(() -> new NotFoundHomeException(String.format(NOT_FOUND_MESSAGE, "Poll", id)));
        validatePollStatus(poll, pollDto.getStatus());
        validateCreationDate(poll, creationDate);
        if (pollDto.getHeader() != null) {
            poll.setHeader(pollDto.getHeader());
        }
        List<House> houses = new ArrayList<>();
        houseRepository.findAllById(housesID).forEach(houses::add);
        poll.setDescription(description);
        poll.setUpdateDate(LocalDateTime.now());
        poll.setCreationDate(LocalDateTime.now());
        poll.setCompletionDate(LocalDateTime.now().plusDays(15L));
        pollRepository.save(poll);
        return mapper.convert(poll, PollDto.class);
    }

    @Override
    public void deactivate(Long id) {
        Poll poll = pollRepository.findById(id).filter(Poll::getEnabled)
                .orElseThrow(() -> new NotFoundHomeException(String.format(NOT_FOUND_MESSAGE, "Poll", id)));
        validatePollStatus(poll, null);
        poll.setEnabled(false);
        pollRepository.save(poll);
    }

    @Override
    public Page<PollDto> findAll(Integer pageNumber, Integer pageSize, Specification<Poll> specification) {
        specification = specification.and((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder
                .equal(root.get("cooperation").get("enabled"), true));
        return pollRepository.findAll(specification, PageRequest.of(pageNumber - 1, pageSize))
                .map(news -> mapper.convert(news, PollDto.class));
    }

    @Override
    @Transactional
    public Page<PollShortenDto> findAllShorten(Integer pageNumber,
                                               Integer pageSize,
                                               Specification<Poll> specification) {
        specification = specification.and((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder
                .equal(root.get("cooperation").get("enabled"), true));
        return pollRepository.findAll(specification, PageRequest.of(pageNumber - 1, pageSize))
                .map(news -> mapper.convert(news, PollShortenDto.class));
    }


    private Cooperation getCooperationById(Long id) {
        return cooperationRepository.findById(id).filter(Cooperation::getEnabled)
                .orElseThrow(() -> new NotFoundHomeException(String.format(NOT_FOUND_MESSAGE, "Cooperation", id)));
    }

    private void validateCompletionDate(LocalDateTime completionDate, LocalDateTime creationDate) {
        long days = ChronoUnit.DAYS.between(creationDate, completionDate);
        if (days < minPollDurationInDays) {
            throw new BadRequestHomeException(
                    String.format(COMPLETION_DATE_VALIDATION_MESSAGE, minPollDurationInDays));
        }
    }

    private void validateHouse(Long cooperationId, HouseDto houseDto) {
        Long id = houseDto.getId();
        Cooperation cooperation = getCooperationById(cooperationId);
        boolean isHousePresentInCooperation = cooperation.getHouses()
                .stream()
                .filter(House::getEnabled)
                .map(House::getId)
                .anyMatch(houseId -> houseId.equals(id));

        if (!isHousePresentInCooperation) {
            throw new NotFoundHomeException(String.format(NOT_FOUND_MESSAGE, "House", id));
        }
    }

    private void validatePollStatus(Poll poll, PollStatusDto pollStatus) {
        if (!poll.getStatus().equals(PollStatus.DRAFT)) {
            throw new BadRequestHomeException(
                    String.format(POLL_STATUS_VALIDATION_MESSAGE, poll.getStatus().toString()));
        } else if (pollStatus != null && pollStatus.equals(PollStatusDto.COMPLETED)) {
            throw new BadRequestHomeException(
                    "Poll status can't be changed to 'completed'");
        }
    }

    private void validateCreationDate(Poll poll, LocalDateTime creationDate) {
        if (poll.getCreationDate().isBefore(LocalDateTime.now())) {
            throw new BadRequestHomeException(
                    "Poll has already started");
        } else if (creationDate.isBefore(LocalDateTime.now())) {
            throw new BadRequestHomeException(
                    "Poll has already started");
        }
    }
}
