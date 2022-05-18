package com.softserveinc.ita.homeproject.homeservice.service.poll;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import com.softserveinc.ita.homeproject.homedata.cooperation.Cooperation;
import com.softserveinc.ita.homeproject.homedata.cooperation.CooperationRepository;
import com.softserveinc.ita.homeproject.homedata.cooperation.house.House;
import com.softserveinc.ita.homeproject.homedata.cooperation.house.HouseRepository;
import com.softserveinc.ita.homeproject.homedata.poll.Poll;
import com.softserveinc.ita.homeproject.homedata.poll.PollRepository;
import com.softserveinc.ita.homeproject.homedata.poll.enums.PollStatus;
import com.softserveinc.ita.homeproject.homedata.poll.enums.PollType;
import com.softserveinc.ita.homeproject.homedata.poll.question.AnswerVariant;
import com.softserveinc.ita.homeproject.homedata.poll.question.AnswerVariantRepository;
import com.softserveinc.ita.homeproject.homedata.poll.question.MultipleChoiceQuestion;
import com.softserveinc.ita.homeproject.homedata.poll.results.ResultQuestion;
import com.softserveinc.ita.homeproject.homedata.poll.results.ResultQuestionRepository;
import com.softserveinc.ita.homeproject.homedata.poll.votes.Vote;
import com.softserveinc.ita.homeproject.homedata.poll.votes.VoteRepository;
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

    private final HouseRepository houseRepository;

    private final CooperationRepository cooperationRepository;

    private final AnswerVariantRepository answerVariantRepository;

    private final VoteRepository voteRepository;

    private final ResultQuestionRepository resultQuestionRepository;

    private final ServiceMapper mapper;

    @Override
    @Transactional
    public PollDto create(Long cooperationId, PollDto pollDto) {
        validateCreationDate(pollDto.getCreationDate(), "Poll can`t be created in the past");
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
        validateCreationDate(poll.getCreationDate(), "Poll has already started");
        if (pollDto.getHeader() != null) {
            poll.setHeader(pollDto.getHeader());
        }
        List<House> houses = new ArrayList<>();
        List<Long> housesId = pollDto.getIncludedHouses();
        houseRepository.findAllById(housesId).forEach(houses::add);
        poll.setDescription(pollDto.getDescription());
        poll.setUpdateDate(LocalDateTime.now());
        if (pollDto.getStatus() == null) {
            poll.setCreationDate(pollDto.getCreationDate().truncatedTo(ChronoUnit.MINUTES));
            poll.setCompletionDate(pollDto.getCreationDate().plusDays(15L));
        } else if (pollDto.getStatus().equals(PollStatusDto.ACTIVE)) {
            poll.setCreationDate(LocalDateTime.now());
            poll.setCompletionDate(LocalDateTime.now().plusDays(15L));
            poll.setStatus(PollStatus.ACTIVE);
        }
        poll.setPolledHouses(houses);
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

        Page<Poll> foundPollsPage = pollRepository.findAll(specification, PageRequest.of(pageNumber - 1, pageSize));
        List<ResultQuestion> resultOfFoundedPolls = new ArrayList<>();

        foundPollsPage.getContent()
            .forEach(poll -> resultOfFoundedPolls.addAll(resultQuestionRepository.findAllByPoll(poll)));
        calculateCompletedPollsResults(foundPollsPage.getContent(), resultOfFoundedPolls);

        return setPositiveResultIfPresent(foundPollsPage, resultOfFoundedPolls);
    }

    private Page<PollDto> setPositiveResultIfPresent(Page<Poll> foundPollsPage,
                                                     List<ResultQuestion> resultOfFoundedPolls) {
        return foundPollsPage.map(poll -> {
            PollDto dto = mapper.convert(poll, PollDto.class);
            Optional<ResultQuestion> positiveResultOptional = resultOfFoundedPolls.stream()
                .filter(resultQuestion -> resultQuestion.getPoll().equals(poll))
                .filter(resultQuestion -> resultQuestion.getAnswerVariant().getAnswer().equals("yes"))
                .findAny();
            positiveResultOptional.ifPresent(resultQuestion -> dto.setResult(resultQuestion.getPercentVotes()));
            return dto;
        });
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

    private void validateCreationDate(LocalDateTime creationDate, String message) {
        if (creationDate.isBefore(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS))){
            throw new BadRequestHomeException(message);
        }
    }

    /**
     * Method receive list of polls to be calculated and result list of polls which was calculated already.
     * Calculates only polls that satisfy criteria defined by filtering using below. Calculation can
     * be carried out with a sufficient number of votes in relation to the
     * {@link com.softserveinc.ita.homeproject.homedata.poll.enums.PollType}. The
     * calculation is made by next methods:
     * <ul>
     *     <li>By Ownership Area</li>
     *     <p>Calculates the result by the area of ownership in the house for each
     *     user who voted, if there is no user who owns more than half of the area
     *     of the whole house</p>
     *     <br/>
     *     <li>By Votes Quantity</li>
     *     <p>Calculates the result by the count of voted users if there is a user
     *     ho owns more than half the area of the whole house</p>
     * </ul>
     * Creates the {@link com.softserveinc.ita.homeproject.homedata.poll.results.ResultQuestion}
     * objects for each answer variant and records its result for each question contained
     * in the poll. Saves each object of
     * {@link com.softserveinc.ita.homeproject.homedata.poll.results.ResultQuestion} in the database.
     * <br/>
     * <br/>
     *
     * @param polls           list of polls to be calculated
     * @param resultQuestions result of votes which was calculated already
     */
    public void calculateCompletedPollsResults(List<Poll> polls, List<ResultQuestion> resultQuestions) {

        polls.stream()
            //below filter returns true if the poll has no results
            .filter(poll -> {
                AtomicBoolean isNotCalculated = new AtomicBoolean(false);
                if (resultQuestions.isEmpty()) {
                    isNotCalculated.set(true);
                } else {
                    resultQuestions.forEach(resultQuestion -> {
                        if (!resultQuestion.getPoll().equals(poll)) {
                            isNotCalculated.set(true);
                        }
                    });
                }
                return isNotCalculated.get();
            })
            .filter(poll -> poll.getStatus().equals(PollStatus.ACTIVE))
            .filter(poll -> poll.getCompletionDate().isBefore(LocalDateTime.now()))
            .filter(poll -> poll.getEnabled().equals(true))
            .filter(poll -> poll.getPollQuestions().size() == 1)
            .filter(poll -> poll.getPollQuestions().get(0) instanceof MultipleChoiceQuestion)
            .forEach(poll -> resultQuestions.addAll(calculatePollResult(poll)));
    }

    private List<ResultQuestion> calculatePollResult(Poll poll) {
        List<AnswerVariant> answerVariants = answerVariantRepository.findAllByQuestion(poll.getPollQuestions().get(0));
        List<Vote> votes = voteRepository.findAllByPoll(poll);
        List<ResultQuestion> results = new ArrayList<>();
        Map<User, BigDecimal> ownedAreaByUser = getAllAreaOwnedByUser(poll);
        Map<AnswerVariant, List<Vote>> answerVariantsToVotes = new HashMap<>();
        int votesCount = votes.size();
        final int[] totalOwnershipsQuantity = {0};
        double amountOfNeededPeople = 0.0;

        initializeMapOfAnswerVariantIdToVotes(answerVariants, votes, answerVariantsToVotes);

        poll.getPolledHouses().get(0).getApartments().forEach(apartment ->
            apartment.getOwnerships().forEach(ownership -> totalOwnershipsQuantity[0]++)
        );

        if (poll.getType().equals(PollType.MAJOR)) {
            amountOfNeededPeople = totalOwnershipsQuantity[0] / 3.0 * 2;
        } else if (poll.getType().equals(PollType.SIMPLE)) {
            amountOfNeededPeople = totalOwnershipsQuantity[0] / 2.0 + 1;
        }

        poll.setStatus(PollStatus.COMPLETED);

        if (votesCount >= amountOfNeededPeople) {
            if (isOverHalfAreaOwner(ownedAreaByUser, poll)) {
                results = saveResultByVotesQuantity(answerVariantsToVotes, poll);
            } else {
                results = saveResultByOwnershipArea(answerVariantsToVotes, ownedAreaByUser, poll);
            }
        }

        return results;
    }

    private void initializeMapOfAnswerVariantIdToVotes(List<AnswerVariant> answerVariants, List<Vote> votes,
                                                       Map<AnswerVariant, List<Vote>> answerVariantsIdToVotes) {
        answerVariants.forEach(answerVariant ->
            votes.forEach(vote ->
                vote.getVoteAnswerVariants().forEach(voteAnswerVariant -> {
                    if (answerVariant.equals(voteAnswerVariant.getAnswerVariant())) {

                        if (!answerVariantsIdToVotes.containsKey(answerVariant)) {
                            List<Vote> votes1 = new ArrayList<>();
                            votes1.add(vote);
                            answerVariantsIdToVotes.put(answerVariant, votes1);
                        } else {
                            answerVariantsIdToVotes.get(answerVariant).add(vote);
                        }

                    }
                })
            )
        );
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

    private List<ResultQuestion> saveResultByOwnershipArea(Map<AnswerVariant, List<Vote>> answerVariantsToVotes,
                                                           Map<User, BigDecimal> ownedAreaByUser, Poll poll) {
        List<ResultQuestion> results = new ArrayList<>();
        BigDecimal votedHouseArea = BigDecimal.valueOf(poll.getPolledHouses().get(0).getHouseArea());

        for (Map.Entry<AnswerVariant, List<Vote>> entry : answerVariantsToVotes.entrySet()) {
            ResultQuestion resultQuestion = new ResultQuestion();
            AtomicReference<BigDecimal> votedArea = new AtomicReference<>(new BigDecimal(0));

            entry.getValue().forEach(vote -> {
                BigDecimal area = votedArea.get();
                area = area.add(ownedAreaByUser.get(vote.getUser()));
                votedArea.set(area);
            });

            resultQuestion.setAnswerVariant(entry.getKey());
            resultQuestion.setPoll(poll);
            resultQuestion.setVoteCount(entry.getValue().size());
            resultQuestion.setPercentVotes(String.valueOf(
                votedArea.get()
                    .multiply(new BigDecimal(100))
                    .divide(votedHouseArea, 10, RoundingMode.CEILING)
            ));
            results.add(resultQuestion);
        }

        resultQuestionRepository.saveAll(results);

        return results;
    }

    private List<ResultQuestion> saveResultByVotesQuantity(Map<AnswerVariant, List<Vote>> answerVariantsToVotes,
                                                           Poll poll) {
        int voteQuantity = 0;
        List<ResultQuestion> results = new ArrayList<>();

        for (Map.Entry<AnswerVariant, List<Vote>> entry : answerVariantsToVotes.entrySet()) {
            voteQuantity += entry.getValue().size();
        }
        for (Map.Entry<AnswerVariant, List<Vote>> entry : answerVariantsToVotes.entrySet()) {
            ResultQuestion resultQuestion = new ResultQuestion();
            resultQuestion.setAnswerVariant(entry.getKey());
            resultQuestion.setPoll(poll);
            resultQuestion.setVoteCount(entry.getValue().size());
            resultQuestion.setPercentVotes(String.valueOf(
                new BigDecimal(resultQuestion.getVoteCount())
                    .multiply(new BigDecimal(100))
                    .divide(new BigDecimal(voteQuantity), 10, RoundingMode.CEILING)
            ));

            results.add(resultQuestion);
        }

        resultQuestionRepository.saveAll(results);

        return results;
    }
}
