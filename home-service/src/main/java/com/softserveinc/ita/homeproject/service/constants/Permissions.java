package com.softserveinc.ita.homeproject.service.constants;

public interface Permissions {
    String CREATE_USER_PERMISSION = "hasAuthority('CREATE_USER_PERMISSION')";
    String UPDATE_USER_PERMISSION = "hasAuthority('UPDATE_USER_PERMISSION')";
    String GET_ALL_USERS_PERMISSION = "hasAuthority('GET_USERS_PERMISSION')";
    String GET_USER_BY_ID_PERMISSION = "hasAuthority('GET_USER_PERMISSION')";
    String DEACTIVATE_USER_PERMISSION = "hasAuthority('DELETE_USER_PERMISSION')";
}
