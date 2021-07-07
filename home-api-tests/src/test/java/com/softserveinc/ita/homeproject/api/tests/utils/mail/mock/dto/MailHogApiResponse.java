package com.softserveinc.ita.homeproject.api.tests.utils.mail.mock.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MailHogApiResponse {

    Integer total;

    Integer count;

    Integer start;

    List<ResponseEmailItem> items;
}
