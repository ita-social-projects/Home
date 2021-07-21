package com.softserveinc.ita.homeproject.homeservice.service.impl;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import com.softserveinc.ita.homeproject.homedata.entity.Cooperation;
import com.softserveinc.ita.homeproject.homedata.entity.House;
import com.softserveinc.ita.homeproject.homedata.entity.Poll;
import com.softserveinc.ita.homeproject.homedata.entity.PollStatus;
import com.softserveinc.ita.homeproject.homedata.repository.CooperationRepository;
import com.softserveinc.ita.homeproject.homedata.repository.PollRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.CooperationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.HouseDto;
import com.softserveinc.ita.homeproject.homeservice.dto.PollDto;
import com.softserveinc.ita.homeproject.homeservice.dto.PollStatusDto;
import com.softserveinc.ita.homeproject.homeservice.exception.BadRequestHomeException;
import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundHomeException;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.service.PollService;
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

    private final CooperationRepository cooperationRepository;

    private final ServiceMapper mapper;

    @Override
    @Transactional
    public PollDto create(CooperationDto cooperationDto, PollDto pollDto) {
        //we need it for set cooperation in 60 line (can we set CooperationDto?)
        Long cooperationId = cooperationDto.getId();
        pollDto.getPolledHouses().forEach(houseDto -> validateHouse(cooperationId, houseDto));
        Poll poll = mapper.convert(pollDto, Poll.class);
        Cooperation cooperation = getCooperationById(cooperationId);
        poll.setCreationDate(LocalDateTime.now());
        validateCompletionDate(pollDto.getCompletionDate(), poll.getCreationDate());
        poll.setCooperation(cooperation);
        poll.setStatus(PollStatus.DRAFT);
        poll.setEnabled(true);
        pollRepository.save(poll);
        return mapper.convert(poll, PollDto.class);
    }

    @Override
    @Transactional
    public PollDto update(PollDto oldPollDto, PollDto updatePollDto) {
        //we need it for find cooperation
        Long pollId = oldPollDto.getId();
        Poll poll = pollRepository.findById(pollId)
            .filter(Poll::getEnabled)
            .orElseThrow(() -> new NotFoundHomeException(String.format(NOT_FOUND_MESSAGE, "Poll", pollId)));
        validatePollStatus(poll, updatePollDto.getStatus());

        if (updatePollDto.getHeader() != null) {
            poll.setHeader(updatePollDto.getHeader());
        }

        if (updatePollDto.getCompletionDate() != null) {
            LocalDateTime completionDate = updatePollDto.getCompletionDate();
            validateCompletionDate(completionDate, poll.getCreationDate());
            poll.setCompletionDate(completionDate);
        }

        if (updatePollDto.getStatus() != null) {
            poll.setStatus(PollStatus.valueOf(updatePollDto.getStatus().name()));
        }

        poll.setUpdateDate(LocalDateTime.now());
        pollRepository.save(poll);
        return mapper.convert(poll, PollDto.class);
    }

    @Override
    public void deactivate(PollDto pollDto) {
        //only for saving binding with cooperation
        Long id = pollDto.getId();
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
}
