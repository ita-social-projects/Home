package com.softserveinc.ita.homeproject.application.mapper;

import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;
import com.softserveinc.ita.homeproject.model.BaseView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Component
@RequiredArgsConstructor
public class IHomeMapperImpl implements IHomeMapper {

    private final HomeMapper mapper;

    @Override
    public <View extends BaseView> View convertToView(BaseDto dto, Class<View> view) {
        return mapper.map(dto, (Type) view);
    }

    @Override
    public <Dto extends BaseDto> Dto convertToDto(BaseView view, Class<Dto> dto) {
        return mapper.map(view, (Type) dto);
    }
}
