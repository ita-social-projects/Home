package com.softserveinc.ita.homeproject.homeservice.mapper;

import java.util.ArrayList;
import java.util.List;

import com.softserveinc.ita.homeproject.homedata.poll.Poll;
import com.softserveinc.ita.homeproject.homedata.poll.enums.PollQuestionType;
import com.softserveinc.ita.homeproject.homedata.poll.question.AnswerVariant;
import com.softserveinc.ita.homeproject.homedata.poll.votes.Vote;
import com.softserveinc.ita.homeproject.homedata.poll.votes.VoteAnswerVariant;
import com.softserveinc.ita.homeproject.homedata.user.User;
import com.softserveinc.ita.homeproject.homeservice.dto.poll.enums.PollQuestionTypeDto;
import com.softserveinc.ita.homeproject.homeservice.dto.poll.question.AdviceChoiceQuestionDto;
import com.softserveinc.ita.homeproject.homeservice.dto.poll.question.MultipleChoiceQuestionDto;
import com.softserveinc.ita.homeproject.homeservice.dto.poll.question.PollQuestionDto;
import com.softserveinc.ita.homeproject.homeservice.dto.poll.results.AnswerVariantDto;
import com.softserveinc.ita.homeproject.homeservice.dto.poll.votes.AdviceQuestionVoteDto;
import com.softserveinc.ita.homeproject.homeservice.dto.poll.votes.MultipleChoiceQuestionVoteDto;
import com.softserveinc.ita.homeproject.homeservice.dto.poll.votes.QuestionVoteDto;
import com.softserveinc.ita.homeproject.homeservice.dto.poll.votes.VoteDto;
import com.softserveinc.ita.homeproject.homeservice.dto.poll.votes.VoteQuestionVariantDto;
import org.springframework.beans.factory.annotation.Autowired;

public class VoteMapper {

    @Autowired
    private ServiceMapper mapper;

    public List<Vote> convertToList(VoteDto voteDto, Poll poll, User user) {
        List<Vote> votes = new ArrayList<>();

        for (QuestionVoteDto questionVoteDto : voteDto.getQuestionVotes()) {
            Vote vote = new Vote();
            PollQuestionDto questionDto = questionVoteDto.getQuestion();

            vote.setPoll(poll);
            vote.setUser(user);
            vote.setType(mapper.convert(questionVoteDto.getType(), PollQuestionType.class));

            if (questionDto instanceof AdviceChoiceQuestionDto) {
                vote.setType(PollQuestionType.ADVICE);
                vote.setAdviceAnswer(questionVoteDto.getQuestion().getQuestion());
            } else if (questionDto instanceof MultipleChoiceQuestionDto) {
                List<VoteAnswerVariant> answers = new ArrayList<>();

                vote.setType(PollQuestionType.MULTIPLE_CHOICE);
                ((MultipleChoiceQuestionDto) questionDto).getAnswerVariants()
                    .forEach(answerVariantDto -> {
                        VoteAnswerVariant voteAnswerVariant = new VoteAnswerVariant();
                        voteAnswerVariant.setVote(vote);
                        voteAnswerVariant.setAnswerVariant(mapper.convert(answerVariantDto, AnswerVariant.class));
                        answers.add(voteAnswerVariant);
                    });
                vote.setVoteAnswerVariants(answers);
            }

            votes.add(vote);
        }

        return votes;
    }

    public VoteDto convertToDto(List<Vote> votes) {
        VoteDto voteDto = new VoteDto();
        List<QuestionVoteDto> questionVotes = new ArrayList<>();

        voteDto.setPollId(votes.get(0).getId());
        voteDto.setUserId(votes.get(0).getUser().getId());

        for (Vote vote : votes) {
            if (vote.getType() == PollQuestionType.ADVICE) {
                AdviceQuestionVoteDto adviceQuestionVoteDto = new AdviceQuestionVoteDto();
                adviceQuestionVoteDto.setType(PollQuestionTypeDto.ADVICE);
                adviceQuestionVoteDto.setQuestion(mapper.convert(vote.getQuestion(), PollQuestionDto.class));
                adviceQuestionVoteDto.setAnswer(vote.getAdviceAnswer());
                questionVotes.add(adviceQuestionVoteDto);
            } else if (vote.getType() == PollQuestionType.MULTIPLE_CHOICE) {
                MultipleChoiceQuestionVoteDto multipleChoiceQuestionDto = new MultipleChoiceQuestionVoteDto();
                List<VoteQuestionVariantDto> answers = new ArrayList<>();
                vote.getVoteAnswerVariants().forEach(voteAnswerVariant -> {
                    VoteQuestionVariantDto voteQuestionVariantDto = new VoteQuestionVariantDto();
                    voteQuestionVariantDto.setAnswerVariant(mapper.convert(voteAnswerVariant.getAnswerVariant(),
                        AnswerVariantDto.class));
                    answers.add(voteQuestionVariantDto);
                });
                multipleChoiceQuestionDto.setAnswers(answers);
                questionVotes.add(multipleChoiceQuestionDto);
            }
        }

        voteDto.setQuestionVotes(questionVotes);

        return voteDto;
    }
}
