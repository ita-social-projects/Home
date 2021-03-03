package com.softserveinc.ita.homeproject.application.constants;

/**
 * Permissions interface is used to store
 * permissions that need to be used in Authorization.
 *
 * @author Mykyta Morar
 */
public final class Permissions {
    public static final String CREATE_USER_PERMISSION = "hasAuthority('CREATE_USER_PERMISSION')";

    public static final String UPDATE_USER_PERMISSION = "hasAuthority('UPDATE_USER_PERMISSION')";

    public static final String GET_ALL_USERS_PERMISSION = "hasAuthority('GET_USERS_PERMISSION')";

    public static final String GET_USER_BY_ID_PERMISSION = "hasAuthority('GET_USER_PERMISSION')";

    public static final String DEACTIVATE_USER_PERMISSION = "hasAuthority('DELETE_USER_PERMISSION')";

    public static final String CREATE_NEWS_PERMISSION = "hasAuthority('CREATE_NEWS_PERMISSION')";

    public static final String UPDATE_NEWS_PERMISSION = "hasAuthority('UPDATE_NEWS_PERMISSION')";

    public static final String GET_NEWS_PERMISSION = "hasAuthority('GET_NEWS_PERMISSION')";

    public static final String DELETE_NEWS_PERMISSION = "hasAuthority('DELETE_NEWS_PERMISSION')";
}
