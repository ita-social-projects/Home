package com.softserveinc.ita.homeproject.homeservice.exception;

public class ExceptionMessages {

    private ExceptionMessages(){}

    public static final String INVALID_TOKEN_MESSAGE = "Invalid token";

    public static final String BAD_CREDENTIAL_ERROR_MESSAGE = "Bad credentials";

    public static final String NOT_FOUND_MESSAGE = "%s with 'id: %s' is not found";

    public static final String NOT_ACCEPTABLE_TOKEN_MESSAGE = "Token is not correct";

    public static final String UNKNOWN_ERROR_APPEARED_MESSAGE =
            "The unknown error appeared. Check your payload or contact support.";

    public static final String NOT_FOUND_USER_NAME_MESSAGE = "There is no user with given email!";

    public static final String BAD_REQUEST_HOME_MESSAGE = "Request contains same path and query params";

    public static final String BAD_REQUEST_HOME_WITH_QUERY_PARAM_MESSAGE = "Query param %s has more than one value";

    public static final String BAD_REQUEST_HOME_WITH_PARAMETER_TYPE_MESSAGE = "The parameter type must be one of the "
        + "values: %s";

    public static final String ILLEGAL_STATE_RESOLVING_TYPE_MESSAGE = "Something went wrong with resolving types. "
        + "Try to provide it explicitly.";

    public static final String UNKNOWN_ERROR_MESSAGE = "Unknown error. Please contact support.";

    public static final String SERVER_ERROR_MESSAGE = "Unknown error. Please contact support.";

    public static final String NOT_FOUND_USER_FORMAT_MESSAGE = "User with id: %d is not found";

    public static final String NOT_FOUND_CURRENT_USER_MESSAGE = "Current user is not found";

    public static final String NOT_MATCH_EMAILS_MESSAGE = "The e-mail to which the token was sent "
            + "does not match provided";

    public static final String BAD_REQUEST_USER_EXISTS_MESSAGE = "User with email %s is already exists";

    public static final String NOT_FOUND_INVITATION_MESSAGE = "Invitation with provided token not found";

    public static final String NOT_SUPPORTED_PROVIDED_TYPE_MESSAGE = "Provided type not supported";

    public static final String USER_CANT_BE_DELETED_MESSAGE = "User cannot be deleted";

    public static final String NOT_FOUND_ROLE_MESSAGE = "Role not found.";

    public static final String NOT_FOUND_OWNERSHIP_WITH_ID_MESSAGE = "Ownership with 'id: %d' is not found";

    public static final String ALERT_MORE_THAN_ONE_ELEMENT_MESSAGE =
            "Result of the request that require to return one element, contains more than one element";

    public static final String ALERT_COMPLETION_DATE_VALIDATION_MESSAGE =
            "Completion date of the poll has not to be less than %s days after creation";

    public static final String CANT_UPDATE_POLL_STATUS_VALIDATION_MESSAGE = "Can't update or delete poll"
        + " with status: '%s'";

    public static final String CANT_COMPLETED_POLL_STATUS_MESSAGE = "Poll status can't be changed to 'completed'";

    public static final String CANT_CREATE_VOTE_POLL_STATUS_MESSAGE = "Can't create vote on poll with status: '%s'";

    public static final String CANT_CREATE_VOTE_OUTDATED_POLL_MESSAGE = "Can't create vote on outdated poll: '%s'";

    public static final String NOT_FOUND_USER_LOGIN_MESSAGE = "User with 'login: %s' is not found";

    public static final String ALERT_TRYING_TO_REVOTE_MESSAGE =
            "You are trying to re-vote on a poll with 'id: %d' that you have already voted";

    public static final String ALERT_WRONG_QUESTIONS_COUNT_FOR_POLL_MESSAGE =
            "The number of voted questions does not equal the number of questions in the poll with 'id: %d'";

    public static final String ALERT_WRONG_QUESTIONS_FOR_POLL_MESSAGE =
            "Some of the questions you are trying to vote do not match the poll with 'id: %d'";

    public static final String ALERT_WRONG_ANSWER_COUNT_VALIDATION_MESSAGE =
            "Wrong count of selected answers to poll question with 'id: %d' (there is should be min 1, max %d)";

    public static final String NOT_MATCH_ANSWER_VOTING_QUESTION_VALIDATION_MESSAGE =
            "The answer variant with 'id: %d' cannot be chosen when voting on the question with 'id: %d'";

    public static final String ALERT_WRONG_INVITATION_TYPE_MESSAGE = "Wrong invitation type.";

    public static final String NOT_MATCH_POLL_QUESTION_TYPE_MESSAGE = "Type of the PollQuestion doesn't match";

    public static final String NOT_FOUND_QUESTION_TYPE_MESSAGE = "Type of the PollQuestion is not found";

    public static final String INVALID_HOUSE_MESSAGE = "Can't add or remove house, invalid poll_id or house_id";

    public static final String NOT_FOUND_NEWS_MESSAGE = "Can't find news with given ID:";

    public static final String NOT_FOUND_COOPERATION_FORMAT_MESSAGE = "Can't find cooperation with given ID: %d";

    public static final String NOT_FOUND_CONTACT_FORMAT_MESSAGE = "Can't find contact with given ID:%d";

    public static final String NOT_FOUND_TYPE_CONTACT_FORMAT_MESSAGE = "Type of the contact is not found";

    public static final String NOT_MATCH_TYPE_CONTACT_FORMAT_MESSAGE = "Type of the contact doesn't match";

    public static final String NOT_FOUND_CONTACT_ID_MESSAGE = "Contact with id: %d is not found";

    public static final String ALERT_USER_HAS_MAIN_CONTACT_MESSAGE = "User with id %d already has main %s contact";

    public static final String NOT_FOUND_COOPERATION_MESSAGE = "Cooperation with 'id: %s' is not found";

    public static final String NOT_FOUND_INVITATION_FORMAT = "Invitation with id: %d not found.";

    public static final String NOT_FOUND_REGISTRATION_TOKEN_MESSAGE = "Registration token not found";

    public static final String ALERT_COOPERATION_HAS_MAIN_CONTACT_MESSAGE = "Cooperation with id %d already has "
        + "main %s contact";

    public static final String ALERT_INVITATION_OVERDUE_MESSAGE = "Invitation was overdue";

    public static final String ALERT_INVITATION_NOT_ACTIVE_MESSAGE = "Invitation is not active";

    public static final String ALERT_INVITATION_DELETED_BY_ADMIN_MESSAGE = "Invitation was deleted "
        + "By Cooperation Admin";

    public static final String ALERT_INVITATION_ALREADY_EXIST_MESSAGE = "Invitation already exist for cooperation";

    public static final String ALERT_INVITATION_ALREADY_EXIST_APARTMENT_MESSAGE = "Invitation already exist "
        + "for apartment";

    public static final String ALERT_APARTMENT_WITH_NUMBER_EXIST_MESSAGE = "Apartment with number %s already exist "
        + "in this house";

    public static final String ALERT_HOUSE_EXIST_IN_POLL_MESSAGE = "House with id:%s already exists in poll with id:%s";

    public static final String NOT_FOUND_APARTMENT_ID_MESSAGE = "Apartment with id: %d not found";

    public static final String NOT_FOUND_HOUSE_WITH_ID_IN_COOPERATION_MESSAGE = "House with id: %d not found "
        + "in this cooperation";

    public static final String NOT_FOUND_COOPERATION_WITH_ID_MESSAGE = "Can't find cooperation with given ID: %d";

    public static final String NOT_FOUND_HOUSE_WITH_ID_MESSAGE = "House with 'id: %d' is not found";

}
