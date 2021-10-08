package com.softserveinc.ita.homeproject.application.mapper;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;

import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;
import com.softserveinc.ita.homeproject.homeservice.dto.poll.question.AdviceChoiceQuestionDto;
import com.softserveinc.ita.homeproject.homeservice.dto.poll.question.MultipleChoiceQuestionDto;
import com.softserveinc.ita.homeproject.homeservice.mapper.config.AbstractTypeConverter;
import com.softserveinc.ita.homeproject.model.CreateAdviceQuestion;
import com.softserveinc.ita.homeproject.model.QuestionLookup;
import com.softserveinc.ita.homeproject.model.ReadAdviceQuestion;
import com.softserveinc.ita.homeproject.model.UpdateAdviceQuestion;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.ConditionalConverter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HomeMapper {

    public static final String MODEL_PACKAGE = "com.softserveinc.ita.homeproject.application.model";

    public static final String DTO_PACKAGE = "com.softserveinc.ita.homeproject.homeservice.dto";

    private ModelMapper modelMapper;

    private final Map<Class, Class> customTypeMappingMap = new HashMap<>();

    {
        customTypeMappingMap.put(AdviceChoiceQuestionDto.class, ReadAdviceQuestion.class);
        customTypeMappingMap.put(CreateAdviceQuestion.class, AdviceChoiceQuestionDto.class);
        customTypeMappingMap.put(QuestionLookup.class, MultipleChoiceQuestionDto.class);
        customTypeMappingMap.put(UpdateAdviceQuestion.class, AdviceChoiceQuestionDto.class);
    }

    @PostConstruct
    public void init() {
        modelMapper = new ModelMapper();

        ConditionalConverter<Object, BaseDto> conditionalConverter =
            new AbstractTypeConverter<>(MODEL_PACKAGE, DTO_PACKAGE, (s, d) -> {
                if (customTypeMappingMap.containsKey(s)) {
                    return d.equals(customTypeMappingMap.get(s));
                }

                String sourceSimpleDomainName = getSimpleDomainName(s);
                String destinationSimpleName = d.getSimpleName();

                return destinationSimpleName.contains(sourceSimpleDomainName)
                    || sourceSimpleDomainName.contains(destinationSimpleName);
            });
        ConditionalConverter<BaseDto, Object> conditionalConverter2 =
            new AbstractTypeConverter<>(DTO_PACKAGE, MODEL_PACKAGE, (s, d) -> {
                if (customTypeMappingMap.containsKey(s)) {
                    return d.equals(customTypeMappingMap.get(s));
                }

                String sourceSimpleDomainName = s.getSimpleName();
                String destinationSimpleName = getSimpleDomainName(d);

                return destinationSimpleName.contains(sourceSimpleDomainName)
                    || sourceSimpleDomainName.contains(destinationSimpleName);
            }, (r, d) -> !r.getSubTypesOf(d).isEmpty());

        modelMapper.getConfiguration().getConverters().add(0, conditionalConverter);
        modelMapper.getConfiguration().getConverters().add(0, conditionalConverter2);
    }

    private static String getSimpleDomainName(Class<?> s) {
        return s.getSimpleName().replace("Read", "").replace("Create", "").replace("Update", "");
    }

    public <D> D convert(Object source, Class<D> destination) {
        return modelMapper.map(source, destination);
    }
}
