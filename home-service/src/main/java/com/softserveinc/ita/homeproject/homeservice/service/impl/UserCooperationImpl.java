package com.softserveinc.ita.homeproject.homeservice.service.impl;

import com.softserveinc.ita.homeproject.homedata.entity.CooperationInvitation;
import com.softserveinc.ita.homeproject.homedata.entity.Ownership;
import com.softserveinc.ita.homeproject.homedata.entity.UserCooperation;
import com.softserveinc.ita.homeproject.homedata.repository.CooperationRepository;
import com.softserveinc.ita.homeproject.homedata.repository.RoleRepository;
import com.softserveinc.ita.homeproject.homedata.repository.UserCooperationRepository;
import com.softserveinc.ita.homeproject.homedata.repository.UserRepository;
import com.softserveinc.ita.homeproject.homeservice.constants.Roles;
import com.softserveinc.ita.homeproject.homeservice.dto.UserCooperationDto;
import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundHomeException;
import com.softserveinc.ita.homeproject.homeservice.service.UserCooperationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCooperationImpl implements UserCooperationService {

    private final RoleRepository roleRepository;

    private final UserCooperationRepository userCooperationRepository;

    private final CooperationRepository cooperationRepository;

    private final UserRepository userRepository;

    @Override
    public void createUserCooperationViaInvitation(CooperationInvitation invitation) {
        var userCooperation = new UserCooperation();

        userRepository.findByEmail(invitation.getEmail())
                .ifPresent(userCooperation::setUser);

        cooperationRepository.findCooperationByName(invitation.getCooperationName())
                .ifPresent(userCooperation::setCooperation);

        userCooperation.setRole(roleRepository.findByName(invitation.getRole().getName().toUpperCase())
                .orElseThrow(() -> new NotFoundHomeException("RoleDto not found.")));

        userCooperationRepository.save(userCooperation);
    }

    public void createUserCooperationForOwnership(Ownership ownership){

        var userCooperation = new UserCooperation();

        userCooperation.setUser(ownership.getUser());
        userCooperation.setCooperation(ownership.getCooperation());
        userCooperation.setRole(roleRepository.findByName(Roles.OWNER_ROLE)
                .orElseThrow(() -> new NotFoundHomeException("Role not found.")));

        userCooperationRepository.save(userCooperation);
    }

    @Override
    public Page<UserCooperationDto> findAll(Integer pageNumber,
                                            Integer pageSize,
                                            Specification<UserCooperation> specification) {
        return null;
    }
}
