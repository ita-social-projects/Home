package com.softserveinc.ita.homeproject.homeservice.mapper.config.impl;

import java.util.ArrayList;
import java.util.List;

import com.softserveinc.ita.homeproject.homedata.entity.AdviceChoiceQuestion;
import com.softserveinc.ita.homeproject.homedata.entity.AnswerVariant;
import com.softserveinc.ita.homeproject.homedata.entity.QuestionVote;
import com.softserveinc.ita.homeproject.homedata.entity.Vote;
import com.softserveinc.ita.homeproject.homedata.repository.AnswerVariantRepository;
import com.softserveinc.ita.homeproject.homedata.repository.PollQuestionRepository;
import com.softserveinc.ita.homeproject.homedata.repository.VoteRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.AdviceQuestionVoteDto;
import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundVoteException;
import com.softserveinc.ita.homeproject.homeservice.mapper.config.ServiceMappingConfig;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Conditions;
import org.modelmapper.Converter;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdviceQuestionVoteDtoServiceMappingConfig
    implements ServiceMappingConfig<AdviceQuestionVoteDto, QuestionVote> {

    private final VoteRepository voteRepository;

    private final PollQuestionRepository questionRepository;

    private final AnswerVariantRepository answerVariantRepository;

    @Override
    public void addMappings(TypeMap<AdviceQuestionVoteDto, QuestionVote> typeMap) {
        typeMap
            .addMappings(
                mapper -> mapper.when(Conditions.isNotNull()).map(AdviceQuestionVoteDto::getId, QuestionVote::setId))
            .setPostConverter(fieldsConverter());
    }

    private Converter<AdviceQuestionVoteDto, QuestionVote> fieldsConverter() {
        return context -> {
            AdviceQuestionVoteDto source = context.getSource();
            QuestionVote destination = context.getDestination();
            Long voteId = source.getVoteId();
            if (voteId != null) {
                Vote vote = voteRepository.findById(voteId).orElse(null);
                destination.setVote(vote);
            }
            Long questionId = source.getQuestionId();
            if (questionId != null) {
                AdviceChoiceQuestion question =
                    (AdviceChoiceQuestion) questionRepository.findById(questionId).orElse(null);
                destination.setQuestion(question);
            }
            mapAnswerVariants(source, destination);
            return context.getDestination();
        };
    }

    private void mapAnswerVariants(AdviceQuestionVoteDto source, QuestionVote destination) {
        Long questionId = source.getQuestionId();
        AdviceChoiceQuestion question = (AdviceChoiceQuestion) questionRepository.findById(questionId).orElseThrow(
            () -> new NotFoundVoteException(String.format("Vote with 'id: %d' is not found", questionId)));
        String answer = source.getAnswer();
        List<AnswerVariant> answers = new ArrayList<>();
        AnswerVariant repeatedAnswer = answerVariantRepository.findByQuestionAndAnswer(question, answer);
        if (repeatedAnswer != null) {
            answers.add(repeatedAnswer);
        } else {
            AnswerVariant newAnswerVariant = new AnswerVariant();
            newAnswerVariant.setAnswer(answer);
            newAnswerVariant.setQuestion(question);
            answers.add(newAnswerVariant);
        }
        destination.setAnswerVariants(answers);
    }
}
