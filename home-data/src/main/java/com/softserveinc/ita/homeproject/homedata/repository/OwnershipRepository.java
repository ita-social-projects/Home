package com.softserveinc.ita.homeproject.homedata.repository;

import java.util.List;

import com.softserveinc.ita.homeproject.homedata.entity.Ownership;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnershipRepository  extends PagingAndSortingRepository<Ownership, Long>,
        JpaSpecificationExecutor<Ownership> {

    List<Ownership> findAllByApartmentId(Long apartmentId);
}
