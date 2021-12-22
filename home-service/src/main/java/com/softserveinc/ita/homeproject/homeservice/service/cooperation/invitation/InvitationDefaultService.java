package com.softserveinc.ita.homeproject.homeservice.service.cooperation.invitation;

import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.Invitation;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.InvitationRepository;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.apartment.ApartmentInvitation;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.cooperation.CooperationInvitation;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation.InvitationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation.apartment.ApartmentInvitationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation.cooperation.CooperationInvitationDto;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.service.QueryableService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InvitationDefaultService extends InvitationServiceImpl
        implements QueryableService<Invitation, InvitationDto> {


    public InvitationDefaultService(InvitationRepository invitationRepository, ServiceMapper mapper) {
        super(invitationRepository, mapper);
    }

    @Transactional
    public Page<InvitationDto> findAll(Integer pageNumber,
                                       Integer pageSize,
                                       Specification<Invitation> specification) {
        Specification<Invitation> invitationSpecification = specification
                .and((root, criteriaQuery, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get("enabled"), true));
        Page<Invitation> pageCooperationInvitation = invitationRepository
                .findAll(invitationSpecification, PageRequest.of(pageNumber - 1, pageSize));

        return pageCooperationInvitation.map(invitation -> mapper.convert(invitation, InvitationDto.class));
    }

    @Override
    public void acceptUserInvitation(Invitation invitation) {

    }

    @Override
    protected InvitationDto saveInvitation(InvitationDto invitationDto) {
        return null;
    }

    @Override
    public void markInvitationsAsOverdue() {

    }
}
