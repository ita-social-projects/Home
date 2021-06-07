package com.softserveinc.ita.homeproject.api.tests.utils.MailHogUtil.Dto;

import com.softserveinc.ita.homeproject.api.tests.utils.MailHogUtil.ApiMailHogUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResponseDto {
    Integer total;
    Integer count;
    Integer start;
    List<ItemDto> items;
}
