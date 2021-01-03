package com.softserveinc.ita.homeproject.homedata.repository;

import com.softserveinc.ita.homeproject.homedata.entity.Osbb;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface OsbbRepository extends PagingAndSortingRepository<Osbb, Long> {

    Optional<Osbb> findByFullName(String fullName);
    Page<Osbb> findAllByEnabledTrue(Pageable var1);
}
