package com.softserveinc.ita.homeproject.api.tests.utils.mail.mock;

import com.softserveinc.ita.homeproject.api.tests.utils.mail.mock.dto.MailHogApiResponse;
import com.softserveinc.ita.homeproject.api.tests.utils.mail.mock.dto.ResponseEmailItem;
import lombok.SneakyThrows;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Mailhog utilities facade.<br>
 * Allows to wait for certain letters and parse them.
 */
public class MailUtil {

    public static ResponseEmailItem waitLetterForEmail(String email) {
        return waitLetter(predicate().email(email));
    }

    public static ResponseEmailItem waitLetter(Predicate<ResponseEmailItem> predicate) {
        return waitFor(() -> findFirstLetter(predicate).orElse(null));
    }

    public static LetterPredicate predicate() {
        return new LetterPredicate();
    }

    private static Optional<ResponseEmailItem> findFirstLetter(Predicate<ResponseEmailItem> predicate) {
        return getLetters().getItems()
                .stream()
                .filter(predicate)
                .findFirst();
    }

    @SneakyThrows
    private static MailHogApiResponse getLetters() {
        return new MailApi().getMessages();
    }

    public static String getToken(ResponseEmailItem letter) {
        return new LetterParser().getToken(letter);
    }

    @SneakyThrows
    private static <T> T waitFor(Supplier<T> supplier) {
        int timeout = 15000;
        int pauseTime = 200;
        int n = timeout / pauseTime;
        T result = null;
        for (int i = 0; i <= n; i++) {
            try {
                result = supplier.get();
            } catch (Exception e) {
                // ignoring
            }
            if (result != null) {
                return result;
            }
            Thread.sleep(pauseTime);
        }
        throw new RuntimeException("Wait timeout : " + timeout + " ms reached");
    }
}
