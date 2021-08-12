package com.softserveinc.ita.homeproject.homeservice.service.user;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.querydsl.jpa.impl.JPAQuery;
import com.softserveinc.ita.homeproject.homedata.entity.cooperation.Cooperation;
import com.softserveinc.ita.homeproject.homedata.entity.invitation.CooperationInvitation;
import com.softserveinc.ita.homeproject.homedata.entity.ownership.Ownership;
import com.softserveinc.ita.homeproject.homedata.entity.role.Role;
import com.softserveinc.ita.homeproject.homedata.entity.user.QUserCooperation;
import com.softserveinc.ita.homeproject.homedata.entity.user.User;
import com.softserveinc.ita.homeproject.homedata.entity.user.UserCooperation;
import com.softserveinc.ita.homeproject.homedata.repository.cooperation.CooperationRepository;
import com.softserveinc.ita.homeproject.homedata.repository.role.RoleRepository;
import com.softserveinc.ita.homeproject.homedata.repository.user.UserCooperationRepository;
import com.softserveinc.ita.homeproject.homedata.repository.user.UserRepository;
import com.softserveinc.ita.homeproject.homeservice.constants.Roles;
import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundHomeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCooperationImpl implements UserCooperationService {

    private final RoleRepository roleRepository;

    private final UserCooperationRepository userCooperationRepository;

    @PersistenceContext
    private final EntityManager entityManager;

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
                .orElseThrow(() -> new NotFoundHomeException("Role not found.")));

        userCooperationRepository.save(userCooperation);
    }

    public void createUserCooperationForOwnership(Ownership ownership) {
        var role = roleRepository.findByName(Roles.OWNER_ROLE)
                .orElseThrow(() -> new NotFoundHomeException("Role not found."));
        var userCooperation = new UserCooperation();
        if (isUserCooperationNonExists(ownership.getUser(), role, ownership.getCooperation())) {
            userCooperation.setUser(ownership.getUser());
            userCooperation.setCooperation(ownership.getCooperation());
            userCooperation.setRole(role);
            userCooperationRepository.save(userCooperation);
        }
    }

    private boolean isUserCooperationNonExists(User user, Role role, Cooperation cooperation) {
        var qUserCooperation = QUserCooperation.userCooperation;

        JPAQuery<?> query = new JPAQuery<>(entityManager);

        return query.select(qUserCooperation).from(qUserCooperation)
                .where(qUserCooperation.role.eq(role),
                        qUserCooperation.user.eq(user),
                        qUserCooperation.cooperation.eq(cooperation)).fetchCount() <= 0;
    }
}
