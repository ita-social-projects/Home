package com.softserveinc.ita.homeproject.application.config;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import com.softserveinc.ita.homeproject.homedata.entity.Permission;
import com.softserveinc.ita.homeproject.homedata.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * HomeUserWrapperDetails provides core user information for security.
 *
 * @author Ihor Svyrydenko
 * @see org.springframework.security.core.userdetails.UserDetails
 */
public class HomeUserWrapperDetails implements UserDetails {

    private final User user;

    private final Collection<Permission> userPermissions;

    public HomeUserWrapperDetails(User user, Collection<Permission> userPermissions) {
        this.user = user;
        this.userPermissions = userPermissions;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities;
        authorities = userPermissions.stream()
            .map(permission -> new SimpleGrantedAuthority(permission.getName()))
            .collect(Collectors.toSet());

        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return !user.getExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !user.getLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !user.getCredentialsExpired();
    }

    @Override
    public boolean isEnabled() {
        return user.getEnabled();
    }
}
