package com.softserveinc.ita.homeproject.api.tests.users;

import static com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil.NOT_ACCEPTABLE;
import static com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil.NOT_FOUND;
import static com.softserveinc.ita.homeproject.homeservice.service.general.mail.PasswordRestorationMailServiceImpl.PASSWORD_RESET_EMAIL_SUBJECT;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Response;

import com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil;
import com.softserveinc.ita.homeproject.api.tests.utils.mail.mock.LetterPredicate;
import com.softserveinc.ita.homeproject.api.tests.utils.mail.mock.MailUtil;
import com.softserveinc.ita.homeproject.api.tests.utils.mail.mock.dto.ResponseEmailItem;
import com.softserveinc.ita.homeproject.client.ApiException;
import com.softserveinc.ita.homeproject.client.api.CooperationApi;
import com.softserveinc.ita.homeproject.client.api.ResetPasswordApi;
import com.softserveinc.ita.homeproject.client.api.UserApi;
import com.softserveinc.ita.homeproject.client.model.Address;
import com.softserveinc.ita.homeproject.client.model.ContactType;
import com.softserveinc.ita.homeproject.client.model.CreateContact;
import com.softserveinc.ita.homeproject.client.model.CreateCooperation;
import com.softserveinc.ita.homeproject.client.model.CreateEmailContact;
import com.softserveinc.ita.homeproject.client.model.CreatePhoneContact;
import com.softserveinc.ita.homeproject.client.model.CreateUser;
import com.softserveinc.ita.homeproject.client.model.PasswordRestorationApproval;
import com.softserveinc.ita.homeproject.client.model.PasswordRestorationRequest;
import com.softserveinc.ita.homeproject.client.model.ReadUser;
import com.softserveinc.ita.homeproject.homeoauthserver.ApiResponse;
import com.softserveinc.ita.homeproject.homeoauthserver.api.AuthenticationApi;
import com.softserveinc.ita.homeproject.homeoauthserver.model.CreateToken;
import com.softserveinc.ita.homeproject.homeoauthserver.model.UserCredentials;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

class ResetPasswordApiIT {

    private static final UserApi userApi = new UserApi(ApiClientUtil.getCooperationAdminClient());

    private static final CooperationApi cooperationApi = new CooperationApi(ApiClientUtil.getCooperationAdminClient());

    private static final ResetPasswordApi resetPasswordApi = new ResetPasswordApi(ApiClientUtil.getUserGuestClient());

    private static final AuthenticationApi authenticationApi = new AuthenticationApi(ApiClientUtil
        .getAuthenticationApi());


    @Test
    @SneakyThrows
    void successAuthorizationAfterChangingPassword() {
        ReadUser savedUser = createBaseUserForTests();
        String userEmail = savedUser.getEmail();
        String newPassword = "newStr0ngPassword";

        resetPasswordApi.passwordRestorationRequest(new PasswordRestorationRequest()
            .email(userEmail));
        ResponseEmailItem letter = MailUtil.waitLetter(new LetterPredicate().email(userEmail)
            .subject(PASSWORD_RESET_EMAIL_SUBJECT));
        PasswordRestorationApproval approvalForm = new PasswordRestorationApproval()
            .email(userEmail)
            .token(MailUtil.getToken(letter))
            .newPassword(newPassword)
            .passwordConfirmation(newPassword);
        resetPasswordApi.passwordRestorationApproval(approvalForm);

        ApiResponse<CreateToken> response = authenticationApi.authenticateUserWithHttpInfo(new UserCredentials()
            .email(userEmail)
            .password(newPassword));

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatusCode());
    }

    @Test
    @SneakyThrows
    void tryingChangePasswordWithNonExistingToken() {
        ReadUser savedUser = createBaseUserForTests();
        String userEmail = savedUser.getEmail();
        String newPassword = "newStr0ngPassword";
        PasswordRestorationApproval approvalForm = new PasswordRestorationApproval()
            .email(userEmail)
            .token(" ")
            .newPassword(newPassword)
            .passwordConfirmation(newPassword);

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> resetPasswordApi.passwordRestorationApproval(approvalForm))
            .matches(exception -> exception.getCode() == NOT_ACCEPTABLE)
            .withMessageContaining("Invalid token");
    }

    @Test
    @SneakyThrows
    void tryingChangePasswordWithFirstTokenAfterRequestedSecondTokenGeneration() {
        ReadUser savedUser = createBaseUserForTests();
        String userEmail = savedUser.getEmail();
        String newPassword = "newStr0ngPassword";

        resetPasswordApi.passwordRestorationRequest(new PasswordRestorationRequest()
            .email(userEmail));
        ResponseEmailItem letter = MailUtil.waitLetter(new LetterPredicate().email(userEmail)
            .subject(PASSWORD_RESET_EMAIL_SUBJECT));
        resetPasswordApi.passwordRestorationRequest(new PasswordRestorationRequest()
            .email(userEmail));
        PasswordRestorationApproval approvalForm = new PasswordRestorationApproval()
            .email(userEmail)
            .token(MailUtil.getToken(letter))
            .newPassword(newPassword)
            .passwordConfirmation(newPassword);

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> resetPasswordApi.passwordRestorationApproval(approvalForm))
            .matches(exception -> exception.getCode() == NOT_ACCEPTABLE)
            .withMessageContaining("Invalid token");
    }

    @Test
    @SneakyThrows
    void tryingChangePasswordWithWrongEmailButCorrectToken() {
        ReadUser savedUser = createBaseUserForTests();
        String userEmail = savedUser.getEmail();
        String newPassword = "newStr0ngPassword";

        resetPasswordApi.passwordRestorationRequest(new PasswordRestorationRequest()
            .email(userEmail));
        ResponseEmailItem letter = MailUtil.waitLetter(new LetterPredicate().email(userEmail)
            .subject(PASSWORD_RESET_EMAIL_SUBJECT));
        PasswordRestorationApproval approvalForm = new PasswordRestorationApproval()
            .email("wrong_email@gmail.com")
            .token(MailUtil.getToken(letter))
            .newPassword(newPassword)
            .passwordConfirmation(newPassword);

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> resetPasswordApi.passwordRestorationApproval(approvalForm))
            .matches(exception -> exception.getCode() == NOT_ACCEPTABLE)
            .withMessageContaining("Invalid token");
    }

    @Test
    @SneakyThrows
    void tryingChangePasswordWithCorrectTokenButNotMatchesPasswords() {
        ReadUser savedUser = createBaseUserForTests();
        String userEmail = savedUser.getEmail();
        String newPassword = "newStr0ngPassword";

        resetPasswordApi.passwordRestorationRequest(new PasswordRestorationRequest()
            .email(userEmail));
        ResponseEmailItem letter = MailUtil.waitLetter(new LetterPredicate().email(userEmail)
            .subject(PASSWORD_RESET_EMAIL_SUBJECT));
        PasswordRestorationApproval approvalForm = new PasswordRestorationApproval()
            .email(userEmail)
            .token(MailUtil.getToken(letter))
            .newPassword(newPassword)
            .passwordConfirmation(newPassword + "q");

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> resetPasswordApi.passwordRestorationApproval(approvalForm))
            .matches(exception -> exception.getCode() == NOT_ACCEPTABLE)
            .withMessageContaining("Passwords not equals");
    }

    @Test
    @SneakyThrows
    void tryingChangePasswordWithCorrectTokenButWeakPassword() {
        ReadUser savedUser = createBaseUserForTests();
        String userEmail = savedUser.getEmail();
        String newWeakPassword = "weakpassword";

        resetPasswordApi.passwordRestorationRequest(new PasswordRestorationRequest()
            .email(userEmail));
        ResponseEmailItem letter = MailUtil.waitLetter(new LetterPredicate().email(userEmail)
            .subject(PASSWORD_RESET_EMAIL_SUBJECT));
        PasswordRestorationApproval approvalForm = new PasswordRestorationApproval()
            .email(userEmail)
            .token(MailUtil.getToken(letter))
            .newPassword(newWeakPassword)
            .passwordConfirmation(newWeakPassword);

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> resetPasswordApi.passwordRestorationApproval(approvalForm))
            .matches(exception -> exception.getCode() == NOT_ACCEPTABLE)
            .withMessageContaining("Password too weak");
    }

    @Test
    @SneakyThrows
    void successPasswordRestorationRequest() {
        ReadUser savedUser = createBaseUserForTests();
        resetPasswordApi.passwordRestorationRequest(new PasswordRestorationRequest()
            .email(savedUser.getEmail()));

        com.softserveinc.ita.homeproject.client.ApiResponse<Void> response =
            resetPasswordApi.passwordRestorationRequestWithHttpInfo(new PasswordRestorationRequest()
            .email(savedUser.getEmail()));

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatusCode());
    }

    @Test
    @SneakyThrows
    void tryingToRequestPasswordRestorationWithNonExistingEmail() {
        ReadUser savedUser = createBaseUserForTests();

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> resetPasswordApi.passwordRestorationRequest(new PasswordRestorationRequest()
                .email("nonexistingemail@gmail.com")))
            .matches(exception -> exception.getCode() == NOT_FOUND)
            .withMessageContaining("Password restoration request not found");
    }

    @SneakyThrows
    private static ReadUser createBaseUserForTests() {
        CreateCooperation coop = createBaseCooperation();
        CreateUser user = createBaseUser();
        return ApiClientUtil.createCooperationAdmin(cooperationApi, coop, userApi, user);
    }

    private static CreateCooperation createBaseCooperation() {
        return new CreateCooperation()
            .name(RandomStringUtils.randomAlphabetic(5).concat(" Cooperation"))
            .usreo(RandomStringUtils.randomNumeric(8))
            .iban("UA".concat(RandomStringUtils.randomNumeric(27)))
            .adminEmail(RandomStringUtils.randomAlphabetic(10).concat("@gmail.com"))
            .address(createAddress())
            .contacts(createContactList());
    }

    private static CreateUser createBaseUser() {
        return new CreateUser()
            .password(ApiClientUtil.VALID_USER_PASSWORD)
            .email("test.receive.apartment@gmail.com");
    }

    private static Address createAddress() {
        return new Address().city("Dnepr")
            .district("District")
            .houseBlock("block")
            .houseNumber("number")
            .region("Dnipro")
            .street("street")
            .zipCode("zipCode");
    }

    private static List<CreateContact> createContactList() {
        List<CreateContact> createContactList = new ArrayList<>();
        createContactList.add(new CreateEmailContact()
            .email("primaryemail@example.com")
            .type(ContactType.EMAIL)
            .main(true));

        createContactList.add(new CreateEmailContact()
            .email("secondaryemail@example.com")
            .type(ContactType.EMAIL)
            .main(false));

        createContactList.add(new CreateEmailContact()
            .email("myemail@example.com")
            .type(ContactType.EMAIL)
            .main(false));

        createContactList.add(new CreateEmailContact()
            .email("youremail@example.com")
            .type(ContactType.EMAIL)
            .main(false));

        createContactList.add(new CreatePhoneContact()
            .phone("+380509999999")
            .type(ContactType.PHONE)
            .main(true));

        createContactList.add(new CreatePhoneContact()
            .phone("+380679999999")
            .type(ContactType.PHONE)
            .main(false));

        createContactList.add(new CreatePhoneContact()
            .phone("+380639999999")
            .type(ContactType.PHONE)
            .main(false));

        return createContactList;
    }
}
