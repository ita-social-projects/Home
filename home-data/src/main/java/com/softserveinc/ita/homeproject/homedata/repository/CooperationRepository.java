package com.softserveinc.ita.homeproject.homedata.repository;

import com.softserveinc.ita.homeproject.homedata.entity.Cooperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CooperationRepository extends PagingAndSortingRepository<Cooperation, Long> {

    Page<Cooperation> findAllByEnabledTrue(Pageable var1);
}
