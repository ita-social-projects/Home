package com.softserveinc.ita.homeproject.homedata.repository;

import java.util.List;

import com.softserveinc.ita.homeproject.homedata.entity.CooperationInvitation;
import com.softserveinc.ita.homeproject.homedata.entity.InvitationStatus;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CooperationInvitationRepository extends PagingAndSortingRepository<CooperationInvitation, Long>,
        JpaSpecificationExecutor<CooperationInvitation> {

    List<CooperationInvitation> findAllBySentDatetimeIsNullAndStatusEquals(
            InvitationStatus status);
}

