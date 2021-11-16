package com.softserveinc.ita.homeproject.api.tests.utils.mail.mock;

import com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil;
import com.softserveinc.ita.homeproject.api.tests.utils.mail.mock.dto.MailHogApiResponse;
import com.softserveinc.ita.homeproject.client.ApiException;
import com.softserveinc.ita.homeproject.client.api.InvitationsApi;
import com.softserveinc.ita.homeproject.client.model.InvitationToken;
import com.softserveinc.ita.homeproject.client.model.ReadInvitation;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InvitationApprovalUtil {
    private static InvitationsApi invitationApi = new InvitationsApi(ApiClientUtil.getCooperationAdminClient());

    public static String getToken(String str) {
        Pattern pattern = Pattern.compile("(?<=:) .* (?=<)");
        Matcher matcher = pattern.matcher(str);

        String result = "";
        if (matcher.find()) {
            result = matcher.group();
        }
        return result.trim();
    }

    public static ReadInvitation approveInvitation(String userEmail) throws IOException, InterruptedException, ApiException {
        ApiUsageFacade api = new ApiUsageFacade();
        MailHogApiResponse mailResponse = api.getMessages(new ApiMailHogUtil(), MailHogApiResponse.class);
        String cooperationInvitationToken = getToken(getDecodedMessageByEmail(mailResponse, userEmail));
        return invitationApi.approveInvitation(buildInvitationPayload(cooperationInvitationToken));
    }

    public static String getDecodedMessageByEmail(MailHogApiResponse response, String email) {
        String message="";
        for (int i = 0; i < response.getItems().size(); i++){
            if(response.getItems().get(i).getContent().getHeaders().getTo().contains(email)){
                message = response.getItems().get(i).getMime().getParts().get(0).getMime().getParts().get(0).getBody();
                break;
            }
        }
        return new String(Base64.getMimeDecoder().decode(message), StandardCharsets.UTF_8);
    }

    public static InvitationToken buildInvitationPayload(String invitationToken){
        InvitationToken token = new InvitationToken();
        token.setInvitationToken(invitationToken);
        return token;
    }
}
