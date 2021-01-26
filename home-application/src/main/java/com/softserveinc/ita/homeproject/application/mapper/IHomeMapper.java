package com.softserveinc.ita.homeproject.application.mapper;

import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;
import com.softserveinc.ita.homeproject.model.BaseView;

public interface IHomeMapper {
    <View extends BaseView> View convertToView(BaseDto dto, Class<View> view);

    <Dto extends BaseDto> Dto convertToDto(BaseView view, Class<Dto> dto);
}
