package com.softserveinc.ita.homeproject.homeservice.service.user;

import com.softserveinc.ita.homeproject.homedata.user.User;
import com.softserveinc.ita.homeproject.homeservice.dto.user.UserDto;
import com.softserveinc.ita.homeproject.homeservice.dto.user.password.PasswordRestorationApprovalDto;
import com.softserveinc.ita.homeproject.homeservice.dto.user.password.PasswordRestorationRequestDto;
import com.softserveinc.ita.homeproject.homeservice.service.QueryableService;

public interface UserService extends QueryableService<User, UserDto> {

    UserDto createUser(UserDto createUserDto);

    UserDto updateUser(Long id, UserDto updateUserDto);

    UserDto getCurrentUser();

    void deactivateUser(Long id);

    void requestPasswordRestoration(PasswordRestorationRequestDto requestDto);

    void changePassword(PasswordRestorationApprovalDto approvalDto);
}
