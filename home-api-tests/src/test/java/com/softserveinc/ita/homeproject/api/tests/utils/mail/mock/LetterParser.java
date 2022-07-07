package com.softserveinc.ita.homeproject.api.tests.utils.mail.mock;

import com.softserveinc.ita.homeproject.api.tests.utils.mail.mock.dto.ResponseEmailItem;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Letter parser. Can parse invitation tokens.
 */
class LetterParser {

    public String getToken(ResponseEmailItem letter) {
        return getToken(getDecodedMessage(letter));
    }

    private String getDecodedMessage(ResponseEmailItem letter) {
        String message = letter.getMime().getParts().get(0).getMime().getParts().get(0).getBody();
        return new String(Base64.getMimeDecoder().decode(message), StandardCharsets.UTF_8);
    }

    private String getToken(String message) {
        Pattern pattern = Pattern.compile("(?<=:) .* (?=<)");
        Matcher matcher = pattern.matcher(message);

        String result = "";
        if (matcher.find()) {
            result = matcher.group();
        }

        return result.trim();
    }
}
