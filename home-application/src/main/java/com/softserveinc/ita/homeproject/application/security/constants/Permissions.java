package com.softserveinc.ita.homeproject.application.security.constants;

/**
 * Permissions final class is used to store
 * permissions that need to be used in Authorization.
 *
 * @author Mykyta Morar
 */
public final class Permissions {

    private Permissions() {

    }

    public static final String READ_NEWS = "hasAuthority('READ_NEWS')";

    public static final String MANAGE_NEWS = "hasAuthority('MANAGE_NEWS')";

    public static final String MANAGE_USER = "hasAuthority('MANAGE_USER')";

    public static final String READ_COOPERATION = "hasAuthority('READ_COOPERATION')";

    public static final String CREATE_DELETE_COOPERATION = "hasAuthority('CREATE_DELETE_COOPERATION')";

    public static final String UPDATE_COOPERATION = "hasAuthority('UPDATE_COOPERATION')";

    public static final String READ_COOPERATION_DATA = "hasAuthority('READ_COOPERATION_DATA')";

    public static final String MANAGE_COOPERATION_DATA = "hasAuthority('MANAGE_COOPERATION_DATA')";

    public static final String MANAGE_IN_COOPERATION = "hasAuthority('MANAGE_IN_COOPERATION')";

    public static final String READ_APARTMENT_INFO = "hasAuthority('READ_APARTMENT_INFO')";

    public static final String READ_POLL = "hasAuthority('READ_POLL')";

    public static final String MANAGE_POLLS = "hasAuthority('MANAGE_POLLS')";

}
