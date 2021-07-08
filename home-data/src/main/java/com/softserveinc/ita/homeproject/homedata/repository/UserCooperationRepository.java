package com.softserveinc.ita.homeproject.homedata.repository;

import java.util.Optional;

import com.softserveinc.ita.homeproject.homedata.entity.Cooperation;
import com.softserveinc.ita.homeproject.homedata.entity.Role;
import com.softserveinc.ita.homeproject.homedata.entity.User;
import com.softserveinc.ita.homeproject.homedata.entity.UserCooperation;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCooperationRepository  extends PagingAndSortingRepository<UserCooperation, Long>,
        JpaSpecificationExecutor<UserCooperation> {

    UserCooperation findUserCooperationByUser(User user);

    Optional<UserCooperation> findUserCooperationByCooperationAndUserAndRole(Cooperation coop, User user, Role role);
}
