package com.softserveinc.ita.homeproject.homedata.repository;

import java.util.Optional;

import com.softserveinc.ita.homeproject.homedata.entity.Cooperation;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CooperationRepository
    extends PagingAndSortingRepository<Cooperation, Long>, JpaSpecificationExecutor<Cooperation> {

    Optional<Cooperation> findCooperationByName(String name);

}
