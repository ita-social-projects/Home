package com.softserveinc.ita.homeproject.homeservice.mapper.config.impl;

import java.util.List;

import com.softserveinc.ita.homeproject.homedata.entity.AnswerVariant;
import com.softserveinc.ita.homeproject.homedata.entity.EmailContact;
import com.softserveinc.ita.homeproject.homedata.entity.QuestionVote;
import com.softserveinc.ita.homeproject.homeservice.dto.AdviceQuestionVoteDto;
import com.softserveinc.ita.homeproject.homeservice.dto.MultipleChoiceQuestionVoteDto;
import com.softserveinc.ita.homeproject.homeservice.dto.QuestionVoteDto;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.mapper.config.ServiceMappingConfig;
import com.softserveinc.ita.homeproject.homeservice.service.impl.VoteServiceImpl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Condition;
import org.modelmapper.TypeMap;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionVoteDtoServiceMappingConfig
    implements ServiceMappingConfig<QuestionVoteDto, QuestionVote> {
    //    @Lazy
    //    private final VoteServiceImpl voteService;
    @Lazy
    private final ServiceMapper serviceMapper;

    Condition<QuestionVoteDto, QuestionVote> isAdviceQuestion = ctx -> ctx.getSource()
        .getClass() == AdviceQuestionVoteDto.class;

    Condition<QuestionVoteDto, QuestionVote> isMultipleChoiceQuestion = ctx -> ctx.getSource()
        .getClass() == MultipleChoiceQuestionVoteDto.class;

    @Override
    public void addMappings(TypeMap<QuestionVoteDto, QuestionVote> typeMap) {
        typeMap.setProvider(request -> serviceMapper.convert(request.getSource(), QuestionVote.class));
        //        typeMap.addMappings(mapper -> mapper.map(QuestionVoteDto::getVoteId, QuestionVote::setVote));
        //        typeMap.addMappings(
        //            mapper -> mapper.map(QuestionVoteDto::getQuestionId, QuestionVote::setQuestion));
        //        if (typeMap.getSourceType().equals(AdviceQuestionVoteDto.class)) {
        //            typeMap.addMappings(
        //                mapper -> mapper.<AnswerVariant>map(dto -> ((AdviceQuestionVoteDto) dto).getAnswerVariant(),
        //                    (dest, v) -> dest.getAnswerVariants().add(v)));
        //        } else {
        //            typeMap.addMappings(
        //                mapper -> mapper.map(dto -> ((MultipleChoiceQuestionVoteDto) dto).getAnswerVariants(),
        //                    (dest, v) -> dest.getAnswerVariants().addAll((List<AnswerVariant>) v)));
        //        }
        //        typeMap.addMappings(
        //            mapper -> mapper.when(isMultipleChoiceQuestion)
        //                .map(dto -> ((MultipleChoiceQuestionVoteDto) dto).getAnswerVariants(),
        //                    QuestionVote::setAnswerVariants));
        //        typeMap.addMappings(
        //            mapper -> mapper.when(isAdviceQuestion)
        //                .<AnswerVariant>map(dto -> ((AdviceQuestionVoteDto) dto).getAnswerVariant(),
        //                    (dest, v) -> dest.getAnswerVariants().add(v)));
    }
}
