package com.softserveinc.ita.homeproject.application.constants;

/**
 * Permissions interface is used to store
 * permissions that need to be used in Authorization.
 *
 * @author Mykyta Morar
 */
public interface Permissions {
    String CREATE_USER_PERMISSION = "hasAuthority('CREATE_USER_PERMISSION')";
    String UPDATE_USER_PERMISSION = "hasAuthority('UPDATE_USER_PERMISSION')";
    String GET_ALL_USERS_PERMISSION = "hasAuthority('GET_USERS_PERMISSION')";
    String GET_USER_BY_ID_PERMISSION = "hasAuthority('GET_USER_PERMISSION')";
    String DEACTIVATE_USER_PERMISSION = "hasAuthority('DELETE_USER_PERMISSION')";
    String CREATE_NEWS_PERMISSION = "hasAuthority('CREATE_NEWS_PERMISSION')";
    String UPDATE_NEWS_PERMISSION = "hasAuthority('UPDATE_NEWS_PERMISSION')";
    String GET_NEWS_PERMISSION = "hasAuthority('GET_NEWS_PERMISSION')";
    String DELETE_NEWS_PERMISSION = "hasAuthority('DELETE_NEWS_PERMISSION')";
    String CREATE_COOPERATION_PERMISSION = "hasAuthority('CREATE_USER_PERMISSION')";
    String UPDATE_COOPERATION_PERMISSION = "hasAuthority('UPDATE_COOPERATION_PERMISSION')";
    String GET_COOPERATION_PERMISSION = "hasAuthority('GET_COOPERATION_PERMISSION')";
    String DEACTIVATE_COOPERATION_PERMISSION = "hasAuthority('DELETE_COOPERATION_PERMISSION')";

}
