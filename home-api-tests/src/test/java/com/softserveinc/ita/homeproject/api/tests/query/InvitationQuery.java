package com.softserveinc.ita.homeproject.api.tests.query;

import com.softserveinc.ita.homeproject.client.ApiException;
import com.softserveinc.ita.homeproject.client.api.InvitationsApi;
import com.softserveinc.ita.homeproject.client.model.InvitationType;
import com.softserveinc.ita.homeproject.client.model.ReadInvitation;

import java.util.List;

public class InvitationQuery extends BaseQuery {

    private InvitationsApi invitationsApi;

    private InvitationType type;

    private Long cooperationId;

    private Long apartmentId;

    private String email;

    private String status;

    public void setInvitationsApi(InvitationsApi invitationsApi) {
        this.invitationsApi = invitationsApi;
    }

    public void setInvitationType(InvitationType invitationType) {
        this.type = invitationType;
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

    public List<ReadInvitation> perform() throws ApiException {
        return invitationsApi
            .queryAllInvitations(type,
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

    public static class Builder extends BaseBuilder<InvitationQuery, InvitationQuery.Builder> {

        public Builder(InvitationsApi invitationsApi) {
            queryClass.setInvitationsApi(invitationsApi);
        }

        public InvitationQuery.Builder invitationType(InvitationType invitationType) {
            queryClass.setInvitationType(invitationType);
            return this;
        }

        public InvitationQuery.Builder cooperationId(Long cooperationId) {
            queryClass.setCooperationId(cooperationId);
            return this;
        }

        public InvitationQuery.Builder apartmentId(Long apartmentId) {
            queryClass.setApartmentId(apartmentId);
            return this;
        }

        public InvitationQuery.Builder email(String email) {
            queryClass.setEmail(email);
            return this;
        }

        public InvitationQuery.Builder status(String status) {
            queryClass.setStatus(status);
            return this;
        }


        @Override
        protected InvitationQuery getActual() {
            return new InvitationQuery();
        }

        @Override
        protected InvitationQuery.Builder getActualBuilder() {
            return this;
        }
    }



}
