package com.softserveinc.ita.homeproject.homedata.repository;

import java.util.List;
import java.util.Set;

import com.softserveinc.ita.homeproject.homedata.entity.Invitation;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface InvitationRepository extends CrudRepository<Invitation, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE Invitation i set i.status = true WHERE i.id in (:ids)")
    List<Invitation> updateStatus(@Param("ids") Set<Long> ids);

    @Query("SELECT i FROM Invitation i WHERE i.status = false ")
    List<Invitation> getAllNotSentInvitations();

}
