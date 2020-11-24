package com.softserveinc.ita.homeproject.service.constants;

public interface Permissions {
    String createUserPermission = "hasAuthority('CREATE_USER_PERMISSION')";
    String updateUserPermission = "hasAuthority('UPDATE_USER_PERMISSION')";
    String getAllUsersPermission = "hasAuthority('GET_USERS_PERMISSION')";
    String getUserByIdPermission = "hasAuthority('GET_USER_PERMISSION')";
    String deactivateUserPermission = "hasAuthority('DELETE_USER_PERMISSION')";
}
