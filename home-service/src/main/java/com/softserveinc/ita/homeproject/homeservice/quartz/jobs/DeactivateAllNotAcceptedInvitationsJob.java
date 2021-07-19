package com.softserveinc.ita.homeproject.homeservice.quartz.jobs;

import java.time.LocalDateTime;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.softserveinc.ita.homeproject.homedata.entity.ApartmentInvitation;
import com.softserveinc.ita.homeproject.homedata.entity.CooperationInvitation;
import com.softserveinc.ita.homeproject.homedata.entity.Invitation;
import com.softserveinc.ita.homeproject.homedata.entity.InvitationStatus;
import com.softserveinc.ita.homeproject.homedata.repository.ApartmentInvitationRepository;
import com.softserveinc.ita.homeproject.homedata.repository.CooperationInvitationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeactivateAllNotAcceptedInvitationsJob {

    private final ApartmentInvitationRepository apartmentInvitationRepository;

    private final CooperationInvitationRepository cooperationInvitationRepository;

    public void deactivateApartmentInvitations() {
        for (ApartmentInvitation apartmentInvitation : apartmentInvitationRepository
                .findAll((Specification<ApartmentInvitation>) (root, criteriaQuery, criteriaBuilder) ->
                        getInvitationForDeactivating(root, criteriaBuilder))) {
            apartmentInvitation.setStatus(InvitationStatus.DEACTIVATED);
            apartmentInvitationRepository.save(apartmentInvitation);
        }
    }

    public void deactivateCooperationInvitations() {
        for (CooperationInvitation cooperationInvitation : cooperationInvitationRepository
                .findAll((Specification<CooperationInvitation>) (root, criteriaQuery, criteriaBuilder) ->
                        getInvitationForDeactivating(root, criteriaBuilder))) {
            cooperationInvitation.setStatus(InvitationStatus.DEACTIVATED);
            cooperationInvitationRepository.save(cooperationInvitation);
        }
    }

    private Predicate getInvitationForDeactivating(Root<? extends Invitation> root, CriteriaBuilder criteriaBuilder) {
        LocalDateTime currentTime = LocalDateTime.now();
        return criteriaBuilder
                .and((criteriaBuilder.notEqual(root.get("status"), InvitationStatus.DEACTIVATED)),
                        (criteriaBuilder.notEqual(root.get("status"), InvitationStatus.ACCEPTED)),
                        (criteriaBuilder.lessThanOrEqualTo(root.get("requestEndTime"), currentTime.minusDays(7))));
    }
}
