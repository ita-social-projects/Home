package com.softserveinc.ita.homeproject.application.mapper.config.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.softserveinc.ita.homeproject.application.mapper.HomeMapper;
import com.softserveinc.ita.homeproject.application.mapper.config.HomeMappingConfig;
import com.softserveinc.ita.homeproject.homeservice.dto.MultipleChoiceQuestionVoteDto;
import com.softserveinc.ita.homeproject.homeservice.dto.VoteQuestionVariantDto;
import com.softserveinc.ita.homeproject.model.ReadAnswerVariant;
import com.softserveinc.ita.homeproject.model.ReadMultipleChoiceQuestion;
import com.softserveinc.ita.homeproject.model.ReadMultipleChoiceQuestionVote;
import com.softserveinc.ita.homeproject.model.ReadQuestionVote;
import lombok.RequiredArgsConstructor;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReadMultipleChoiceQuestionVoteHomeMappingConfig
    implements HomeMappingConfig<MultipleChoiceQuestionVoteDto, ReadQuestionVote> {
    @Lazy
    private final HomeMapper homeMapper;

    @Override
    public void addMappings(TypeMap<MultipleChoiceQuestionVoteDto, ReadQuestionVote> typeMap) {
        typeMap.setProvider(request -> homeMapper.convert(request.getSource(), ReadMultipleChoiceQuestionVote.class))
            //    Если не добавить постконвертер,
            //    то MultipleChoiceQuestionDto смапится в ReadQuestion, а не в ReadMultipleChoiceQuestion
            //    (в таком случае не будет выведено списка возможных ответов и максимального количества ответов).
            //    Кроме того, в объектах ReadAnswerVariant проставится не id выбранных AnswerVariant-ов,
            //    а id соответствующих им VoteQuestionVariant-ов.
            .setPostConverter(c -> {
                var source = c.getSource();
                ReadMultipleChoiceQuestionVote dest = (ReadMultipleChoiceQuestionVote) c.getDestination();
                dest.setQuestion(homeMapper.convert(source.getQuestion(), ReadMultipleChoiceQuestion.class));
                List<ReadAnswerVariant> readAnswerVariants =
                    source.getAnswers().stream().map(VoteQuestionVariantDto::getAnswerVariant)
                        .map(answer -> homeMapper.convert(answer, ReadAnswerVariant.class))
                        .collect(Collectors.toList());
                dest.setAnswers(readAnswerVariants);
                return dest;
            });
    }
}
