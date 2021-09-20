package com.softserveinc.ita.homeproject.homedata.user;

import java.util.List;

import com.softserveinc.ita.homeproject.homedata.user.User;
import com.softserveinc.ita.homeproject.homedata.user.UserCooperation;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCooperationRepository  extends PagingAndSortingRepository<UserCooperation, Long>,
        JpaSpecificationExecutor<UserCooperation>, QuerydslPredicateExecutor<UserCooperation> {

    List<UserCooperation> findUserCooperationByUser(User user);

}
