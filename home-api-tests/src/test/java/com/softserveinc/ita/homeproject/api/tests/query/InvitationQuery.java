package com.softserveinc.ita.homeproject.api.tests.query;

import com.softserveinc.ita.homeproject.client.ApiException;
import com.softserveinc.ita.homeproject.client.api.InvitationsApi;
import com.softserveinc.ita.homeproject.client.model.InvitationType;
import com.softserveinc.ita.homeproject.client.model.ReadInvitation;
import com.softserveinc.ita.homeproject.client.model.ReadInvitationWithApartment;

import java.util.List;

public class InvitationQuery extends BaseQuery {

    private InvitationsApi invitationsApi;

    private InvitationType invitationType;

    private Long cooperationId;

    private Long apartmentId;

    private String email;

    private String status;

    public void setInvitationsApi(InvitationsApi invitationsApi) {
        this.invitationsApi = invitationsApi;
    }

    public void setInvitationType(InvitationType invitationType) {
        this.invitationType = invitationType;
    }

    public void setCooperationId(Long cooperationId) {
        this.cooperationId = cooperationId;
    }

    public void setApartmentId(Long apartmentId) {
        this.apartmentId = apartmentId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ReadInvitationWithApartment> perfom() throws ApiException {
        return invitationsApi
                .queryAllInvitations(invitationType,
                        cooperationId,
                        apartmentId,
                        this.getPageNumber(),
                        this.getPageSize(),
                        this.getSort(),
                        this.getFilter(),
                        this.getId(),
                        email,
                        status
                        );
    }

    public static class Builder extends BaseBuilder<InvitationQuery, Builder> {

        public Builder(InvitationsApi invitationsApi){
            queryClass.setInvitationsApi(invitationsApi);}

        public Builder invitationType(InvitationType invitationType) {
            queryClass.setInvitationType(invitationType);
            return this;
        }

        public Builder cooperationId(Long cooperationId) {
            queryClass.setCooperationId(cooperationId);
            return this;
        }

        public Builder apartmentId(Long apartmentId) {
            queryClass.setApartmentId(apartmentId);
            return this;
        }

        public Builder email(String email) {
            queryClass.setEmail(email);
            return this;
        }

        public Builder status(String status) {
            queryClass.setStatus(status);
            return this;
        }


    @Override
        protected InvitationQuery getActual() {
            return new InvitationQuery();
        }

        @Override
        protected Builder getActualBuilder() {
            return this;
        }
    }



}
