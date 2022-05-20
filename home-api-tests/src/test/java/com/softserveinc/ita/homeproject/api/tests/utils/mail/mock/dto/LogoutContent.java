package com.softserveinc.ita.homeproject.api.tests.utils.mail.mock.dto;

import java.util.List;

import com.softserveinc.ita.homeproject.client.ApiClient;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LogoutContent {

    private ApiClient apiClient;
    private List<String> access_tokens;
}
