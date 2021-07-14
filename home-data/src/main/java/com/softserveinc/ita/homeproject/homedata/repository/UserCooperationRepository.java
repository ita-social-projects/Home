package com.softserveinc.ita.homeproject.homedata.repository;

import java.util.List;

import com.softserveinc.ita.homeproject.homedata.entity.User;
import com.softserveinc.ita.homeproject.homedata.entity.UserCooperation;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCooperationRepository  extends PagingAndSortingRepository<UserCooperation, Long>,
        JpaSpecificationExecutor<UserCooperation>, QuerydslPredicateExecutor<UserCooperation> {

    List<UserCooperation> findUserCooperationByUser(User user);

}
