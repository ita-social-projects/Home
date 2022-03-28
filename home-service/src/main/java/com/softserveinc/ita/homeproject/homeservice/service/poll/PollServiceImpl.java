package com.softserveinc.ita.homeproject.homeservice.service.poll;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.softserveinc.ita.homeproject.homedata.cooperation.Cooperation;
import com.softserveinc.ita.homeproject.homedata.cooperation.CooperationRepository;
import com.softserveinc.ita.homeproject.homedata.cooperation.house.House;
import com.softserveinc.ita.homeproject.homedata.poll.Poll;
import com.softserveinc.ita.homeproject.homedata.poll.PollRepository;
import com.softserveinc.ita.homeproject.homedata.poll.enums.PollStatus;
import com.softserveinc.ita.homeproject.homedata.poll.enums.PollType;
import com.softserveinc.ita.homeproject.homedata.poll.question.DoubleChoiceQuestion;
import com.softserveinc.ita.homeproject.homedata.poll.question.PollQuestion;
import com.softserveinc.ita.homeproject.homedata.poll.results.ResultQuestion;
import com.softserveinc.ita.homeproject.homedata.poll.results.ResultQuestionRepository;
import com.softserveinc.ita.homeproject.homedata.poll.votes.MultipleChoiceQuestionVote;
import com.softserveinc.ita.homeproject.homedata.poll.votes.QuestionVote;
import com.softserveinc.ita.homeproject.homedata.poll.votes.QuestionVoteRepository;
import com.softserveinc.ita.homeproject.homedata.poll.votes.VoteQuestionVariant;
import com.softserveinc.ita.homeproject.homedata.poll.votes.VoteQuestionVariantRepository;
import com.softserveinc.ita.homeproject.homedata.user.User;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.house.HouseDto;
import com.softserveinc.ita.homeproject.homeservice.dto.poll.PollDto;
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

    private final CooperationRepository cooperationRepository;

    private final QuestionVoteRepository questionVoteRepository;

    private final VoteQuestionVariantRepository voteQuestionVariantRepository;

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
    public PollDto update(Long cooperationId, Long id, PollDto pollDto) {
        Poll poll = pollRepository.findById(id)
            .filter(Poll::getEnabled)
            .filter(poll1 -> poll1.getCooperation().getId().equals(cooperationId))
            .orElseThrow(() -> new NotFoundHomeException(String.format(NOT_FOUND_MESSAGE, "Poll", id)));
        validatePollStatus(poll, pollDto.getStatus());

        if (pollDto.getHeader() != null) {
            poll.setHeader(pollDto.getHeader());
        }

        if (pollDto.getCompletionDate() != null) {
            LocalDateTime completionDate = pollDto.getCompletionDate();
            validateCompletionDate(completionDate, poll.getCreationDate());
            poll.setCompletionDate(completionDate);
        }

        if (pollDto.getStatus() != null) {
            poll.setStatus(PollStatus.valueOf(pollDto.getStatus().name()));
        }

        poll.setUpdateDate(LocalDateTime.now());
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
        calculateCompletedPollsResults();
        specification = specification.and((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder
            .equal(root.get("cooperation").get("enabled"), true));
        return pollRepository.findAll(specification, PageRequest.of(pageNumber - 1, pageSize))
            .map(news -> mapper.convert(news, PollDto.class));
    }

    private Cooperation getCooperationById(Long id) {
        return cooperationRepository.findById(id).filter(Cooperation::getEnabled)
            .orElseThrow(() -> new NotFoundHomeException(String.format(NOT_FOUND_MESSAGE, "Cooperation", id)));
    }
//TODO
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

    private void calculateCompletedPollsResults() {
        List<Poll> polls = new ArrayList<>();

        pollRepository.findAll().forEach(polls::add);
        polls.stream()
            .filter(poll -> poll.getResult() == null)
            .filter(poll -> poll.getStatus().equals(PollStatus.ACTIVE))
            .filter(poll -> poll.getCompletionDate().isBefore(LocalDateTime.now()))
            .filter(poll -> poll.getEnabled().equals(true))
            .filter(poll -> poll.getPollQuestions().size() == 1)
            .filter(poll -> poll.getPollQuestions().get(0) instanceof DoubleChoiceQuestion)
            .forEach(this::calculatePollResult);
    }

    private void calculatePollResult(Poll poll) {
        int votesCount = questionVoteRepository.findAllByQuestion(poll.getPollQuestions().get(0)).size();
        int amountOfNeededPeople = 0;
        Map<User, BigDecimal> ownedAreaByUser = getAllAreaOwnedByUser(poll);
        PollQuestion pollQuestion = poll.getPollQuestions().get(0);

        if (poll.getType().equals(PollType.MAJOR)) {
            amountOfNeededPeople = votesCount / 3 * 2;
        } else if (poll.getType().equals(PollType.SIMPLE)) {
            amountOfNeededPeople = votesCount / 2 + 1;
        }

        if (votesCount >= amountOfNeededPeople) {
            poll.setStatus(PollStatus.COMPLETED);
            if (isOverHalfAreaOwner(ownedAreaByUser, poll)) {
                saveResultByVotesQuantity(pollQuestion);
            } else {
                saveResultByOwnershipArea(pollQuestion, ownedAreaByUser);
            }
        }
    }

    private Map<User, BigDecimal> getAllAreaOwnedByUser(Poll poll) {
        Map<User, BigDecimal> ownedAreaByUser = new HashMap<>();
        poll.getPolledHouses().forEach(
            house -> house.getApartments().forEach(
                apartment -> apartment.getOwnerships().forEach(
                    ownership -> {
                        if (ownedAreaByUser.containsKey(ownership.getUser())) {
                            ownedAreaByUser.replace(ownership.getUser(),
                                ownedAreaByUser.get(ownership.getUser())
                                    .add(ownership.getApartment()
                                        .getApartmentArea())
                            );
                        } else {
                            ownedAreaByUser.put(ownership.getUser(),
                                (ownership.getOwnershipPart()).multiply(ownership.getApartment().getApartmentArea()));
                        }
                    }
                )
            )
        );

        return ownedAreaByUser;
    }

    private boolean isOverHalfAreaOwner(Map<User, BigDecimal> totalOwnershipsArea, Poll poll) {
        double halfHouseArea = poll.getPolledHouses().get(0).getHouseArea() / 2;

        for (Map.Entry<User, BigDecimal> entry : totalOwnershipsArea.entrySet()) {
            if (entry.getValue().doubleValue() >= halfHouseArea) {
                return true;
            }
        }

        return false;
    }

    private void saveResultByOwnershipArea(PollQuestion question, Map<User, BigDecimal> totalOwnershipsArea) {
        List<QuestionVote> votes = questionVoteRepository.findAllByQuestion(question);
        BigDecimal totalAreaOfPositiveVotes = new BigDecimal(0);
        BigDecimal votedHouseArea = BigDecimal.valueOf(question.getPoll().getPolledHouses().get(0).getHouseArea());

        for (QuestionVote questionVote : votes) {
            VoteQuestionVariant voteQuestionVariant = voteQuestionVariantRepository.findByQuestionVote(questionVote);
            if (voteQuestionVariant.getAnswerVariant().getAnswer().equals("yes")) {
                totalAreaOfPositiveVotes = totalAreaOfPositiveVotes
                    .add(totalOwnershipsArea.get(questionVote.getVote().getUser()));
            }
        }

        question.getPoll().setResult(String.valueOf(
                totalAreaOfPositiveVotes
                    .multiply(new BigDecimal(100))
                    .divide(votedHouseArea, 10, RoundingMode.CEILING)
        ));
        pollRepository.save(question.getPoll());
    }

    private void saveResultByVotesQuantity(PollQuestion question) {
        List<QuestionVote> votes = questionVoteRepository.findAllByQuestion(question);
        BigDecimal positiveAnswers = new BigDecimal(0);

        for (QuestionVote questionVote : votes) {
            VoteQuestionVariant voteQuestionVariant = voteQuestionVariantRepository.findByQuestionVote(questionVote);
            if (voteQuestionVariant.getAnswerVariant().getAnswer().equals("yes")) {
                positiveAnswers = positiveAnswers.add(new BigDecimal(0));
            }
        }

        question.getPoll().setResult(String.valueOf(
            positiveAnswers
                .multiply(new BigDecimal(100))
                .divide(BigDecimal.valueOf(votes.size()), 10, RoundingMode.CEILING)
        ));
        pollRepository.save(question.getPoll());
    }
}
