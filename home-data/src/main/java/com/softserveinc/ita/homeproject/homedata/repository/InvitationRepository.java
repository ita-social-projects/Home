package com.softserveinc.ita.homeproject.homedata.repository;

import java.util.List;

import com.softserveinc.ita.homeproject.homedata.entity.Invitation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvitationRepository extends CrudRepository<Invitation, Long> {

    List<Invitation> findAllBySentDateTimeIsNull();
}
