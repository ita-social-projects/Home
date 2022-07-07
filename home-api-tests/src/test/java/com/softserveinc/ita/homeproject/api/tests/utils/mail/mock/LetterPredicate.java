package com.softserveinc.ita.homeproject.api.tests.utils.mail.mock;

import com.softserveinc.ita.homeproject.api.tests.utils.mail.mock.dto.EmailHeader;
import com.softserveinc.ita.homeproject.api.tests.utils.mail.mock.dto.ResponseEmailItem;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Letter predicate with fluent builder. Example of usage:
 * <code>predicate.email(email).subject(subj)</code>.
 */
public class LetterPredicate implements Predicate<ResponseEmailItem> {

    private final Map<String, Predicate<ResponseEmailItem>> predicates = new HashMap<>();

    @Override
    public boolean test(ResponseEmailItem item) {
        return predicates.values().stream().allMatch(predicate -> predicate.test(item));
    }

    public LetterPredicate email(String email) {
        predicates.put("email", emailPredicate(email));
        return this;
    }

    public LetterPredicate subject(String subject) {
        predicates.put("subject", subjectPredicate(subject));
        return this;
    }

    private Predicate<ResponseEmailItem> emailPredicate(String email) {
        return item -> item.getContent().getHeaders().getTo().contains(email);
    }

    private Predicate<ResponseEmailItem> subjectPredicate(String subject) {
        return item -> {
            EmailHeader headers = item.getContent().getHeaders();
            return String.join("", headers.getSubject()).contains(subject);
        };
    }
}