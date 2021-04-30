package com.softserveinc.ita.homeproject.homedata.repository;

import java.util.List;
import java.util.Optional;

import com.softserveinc.ita.homeproject.homedata.entity.Contact;
import com.softserveinc.ita.homeproject.homedata.entity.ContactType;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends PagingAndSortingRepository<Contact, Long>,
                                           JpaSpecificationExecutor<Contact> {

    List<Contact> findAllByUserIdAndType(Long userId, ContactType type);

    List<Contact> findAllByCooperationIdAndType(Long cooperationId, ContactType type);

    List<Contact> findAllByCooperationId(Long cooperationId);

    Optional<Contact> findByIdAndCooperationId(Long id, Long cooperationId);

    Optional<Contact> findByIdAndUserId(Long id, Long userId);
}
