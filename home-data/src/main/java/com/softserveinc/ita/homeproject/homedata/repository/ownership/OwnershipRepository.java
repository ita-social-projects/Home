package com.softserveinc.ita.homeproject.homedata.repository.ownership;

import java.util.List;

import com.softserveinc.ita.homeproject.homedata.entity.ownership.Ownership;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnershipRepository  extends PagingAndSortingRepository<Ownership, Long>,
        JpaSpecificationExecutor<Ownership> {

    List<Ownership> findAllByApartmentId(Long apartmentId);
}
