package com.softserveinc.ita.homeproject.homedata.cooperation;

import java.util.Optional;

import com.softserveinc.ita.homeproject.homedata.cooperation.Cooperation;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CooperationRepository
    extends PagingAndSortingRepository<Cooperation, Long>, JpaSpecificationExecutor<Cooperation> {

    Optional<Cooperation> findCooperationByName(String name);

}
