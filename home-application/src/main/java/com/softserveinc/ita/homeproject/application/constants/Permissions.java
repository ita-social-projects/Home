package com.softserveinc.ita.homeproject.application.constants;

/**
 * Permissions final class is used to store
 * permissions that need to be used in Authorization.
 *
 * @author Mykyta Morar
 */
public final class Permissions {

    private Permissions() {

    }

    public static final String CREATE_USER_PERMISSION = "hasAuthority('CREATE_USER_PERMISSION')";

    public static final String CREATE_CONTACT_PERMISSION = "hasAuthority('CREATE_CONTACT_PERMISSION')";

    public static final String CREATE_COOP_CONTACT_PERMISSION = "hasAuthority('CREATE_COOP_CONTACT_PERMISSION')";

    public static final String CREATE_NEWS_PERMISSION = "hasAuthority('CREATE_NEWS_PERMISSION')";

    public static final String CREATE_COOPERATION_PERMISSION = "hasAuthority('CREATE_COOPERATION_PERMISSION')";

    public static final String CREATE_HOUSE_PERMISSION = "hasAuthority('CREATE_HOUSE_PERMISSION')";

    public static final String GET_NEWS_PERMISSION = "hasAuthority('GET_NEWS_PERMISSION')";

    public static final String GET_ALL_USERS_PERMISSION = "hasAuthority('GET_USERS_PERMISSION')";

    public static final String GET_USER_BY_ID_PERMISSION = "hasAuthority('GET_USER_PERMISSION')";

    public static final String GET_COOPERATION_PERMISSION = "hasAuthority('GET_COOPERATION_PERMISSION')";

    public static final String GET_ALL_COOPERATION_PERMISSION = "hasAuthority('GET_COOPERATIONS_PERMISSION')";

    public static final String GET_ALL_COOP_CONTACT_PERMISSION = "hasAuthority('GET_ALL_COOP_CONTACT_PERMISSION')";

    public static final String GET_ALL_USER_CONTACT_PERMISSION = "hasAuthority('GET_ALL_USER_CONTACT_PERMISSION')";

    public static final String GET_COOP_CONTACT_PERMISSION = "hasAuthority('GET_COOP_CONTACT_PERMISSION')";

    public static final String GET_HOUSE_PERMISSION = "hasAuthority('GET_HOUSE_PERMISSION')";

    public static final String GET_HOUSES_PERMISSION = "hasAuthority('GET_HOUSES_PERMISSION')";

    public static final String GET_USER_CONTACT_PERMISSION = "hasAuthority('GET_USER_CONTACT_PERMISSION')";

    public static final String UPDATE_NEWS_PERMISSION = "hasAuthority('UPDATE_NEWS_PERMISSION')";

    public static final String UPDATE_USER_PERMISSION = "hasAuthority('UPDATE_USER_PERMISSION')";

    public static final String UPDATE_COOPERATION_PERMISSION = "hasAuthority('UPDATE_COOPERATION_PERMISSION')";

    public static final String UPDATE_HOUSE_PERMISSION = "hasAuthority('UPDATE_HOUSE_PERMISSION')";

    public static final String UPDATE_COOP_CONTACT_PERMISSION = "hasAuthority('UPDATE_COOP_CONTACT_PERMISSION')";

    public static final String UPDATE_USER_CONTACT_PERMISSION = "hasAuthority('UPDATE_USER_CONTACT_PERMISSION')";

    public static final String DELETE_NEWS_PERMISSION = "hasAuthority('DELETE_NEWS_PERMISSION')";

    public static final String DELETE_USER_PERMISSION = "hasAuthority('DELETE_USER_PERMISSION')";

    public static final String DELETE_USER_CONTACT_PERMISSION = "hasAuthority('DELETE_USER_CONTACT_PERMISSION')";

    public static final String DELETE_COOPERATION_PERMISSION = "hasAuthority('DELETE_COOPERATION_PERMISSION')";

    public static final String DELETE_COOP_CONTACT_PERMISSION ="hasAuthority('DELETE_COOP_CONTACT_PERMISSION')";

    public static final String DELETE_HOUSE_PERMISSION = "hasAuthority('DELETE_HOUSE_PERMISSION')";

    public static final String CREATE_APARTMENT_PERMISSION = "hasAuthority('CREATE_APARTMENT_PERMISSION')";

    public static final String GET_APARTMENT_PERMISSION = "hasAuthority('GET_APARTMENT_PERMISSION')";

    public static final String GET_APARTMENTS_PERMISSION = "hasAuthority('GET_APARTMENTS_PERMISSION')";

    public static final String UPDATE_APARTMENT_PERMISSION = "hasAuthority('UPDATE_APARTMENT_PERMISSION')";

    public static final String DELETE_APARTMENT_PERMISSION = "hasAuthority('DELETE_APARTMENT_PERMISSION')";

    public static final String GET_OWNERSHIP_PERMISSION = "hasAuthority('GET_OWNERSHIP_PERMISSION')";

    public static final String UPDATE_OWNERSHIP_PERMISSION = "hasAuthority('UPDATE_OWNERSHIP_PERMISSION')";

    public static final String DELETE_OWNERSHIP_PERMISSION = "hasAuthority('DELETE_OWNERSHIP_PERMISSION')";

    public static final String CREATE_POLL_PERMISSION = "hasAuthority('CREATE_POLL_PERMISSION')";

    public static final String UPDATE_POLL_PERMISSION = "hasAuthority('UPDATE_POLL_PERMISSION')";

    public static final String GET_POLL_PERMISSION = "hasAuthority('GET_POLL_PERMISSION')";

    public static final String DELETE_POLL_PERMISSION = "hasAuthority('DELETE_POLL_PERMISSION')";

}
