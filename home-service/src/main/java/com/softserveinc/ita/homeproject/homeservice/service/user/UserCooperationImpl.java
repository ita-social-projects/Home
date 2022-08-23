package com.softserveinc.ita.homeproject.homeservice.service.user;

import static com.softserveinc.ita.homeproject.homeservice.exception.ExceptionMessages.NOT_FOUND_ROLE_MESSAGE;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.querydsl.jpa.impl.JPAQuery;
import com.softserveinc.ita.homeproject.homedata.cooperation.Cooperation;
import com.softserveinc.ita.homeproject.homedata.cooperation.CooperationRepository;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.cooperation.CooperationInvitation;
import com.softserveinc.ita.homeproject.homedata.user.QUserCooperation;
import com.softserveinc.ita.homeproject.homedata.user.User;
import com.softserveinc.ita.homeproject.homedata.user.UserCooperation;
import com.softserveinc.ita.homeproject.homedata.user.UserCooperationRepository;
import com.softserveinc.ita.homeproject.homedata.user.UserRepository;
import com.softserveinc.ita.homeproject.homedata.user.ownership.Ownership;
import com.softserveinc.ita.homeproject.homedata.user.role.Role;
import com.softserveinc.ita.homeproject.homedata.user.role.RoleEnum;
import com.softserveinc.ita.homeproject.homedata.user.role.RoleRepository;
import com.softserveinc.ita.homeproject.homeservice.exception.ExceptionMessages;
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
        UserCooperation userCooperation = new UserCooperation();

        userRepository.findByEmail(invitation.getEmail())
                .ifPresent(userCooperation::setUser);

        cooperationRepository.findCooperationById(invitation.getCooperationId())
                .ifPresent(userCooperation::setCooperation);

        userCooperation.setRole(roleRepository.findByName(invitation.getRole().getName().toUpperCase())
                .orElseThrow(() -> new NotFoundHomeException(NOT_FOUND_ROLE_MESSAGE)));

        userCooperationRepository.save(userCooperation);
    }

    public void createUserCooperationForOwnership(Ownership ownership) {
        Role role = roleRepository.findByName(RoleEnum.OWNER.getName().toUpperCase())
                .orElseThrow(() -> new NotFoundHomeException(NOT_FOUND_ROLE_MESSAGE));
        UserCooperation userCooperation = new UserCooperation();
        if (isUserCooperationNonExists(ownership.getUser(), role, ownership.getCooperation())) {
            userCooperation.setUser(ownership.getUser());
            userCooperation.setCooperation(ownership.getCooperation());
            userCooperation.setRole(role);
            userCooperationRepository.save(userCooperation);
        }
    }

    @Override
    public UserCooperation getUserCooperationByUserId(Long id) {
        return userCooperationRepository.findByUserId(id)
                .orElseThrow(() -> new NotFoundHomeException(ExceptionMessages.NOT_FOUND_COOPERATION_MESSAGE));
    }

    private boolean isUserCooperationNonExists(User user, Role role, Cooperation cooperation) {
        QUserCooperation qUserCooperation = QUserCooperation.userCooperation;

        JPAQuery<?> query = new JPAQuery<>(entityManager);

        return query.select(qUserCooperation).from(qUserCooperation)
                .where(qUserCooperation.role.eq(role),
                        qUserCooperation.user.eq(user),
                        qUserCooperation.cooperation.eq(cooperation)).fetchCount() <= 0;
    }
}
