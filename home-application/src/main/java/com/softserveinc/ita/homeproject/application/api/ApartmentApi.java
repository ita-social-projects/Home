package com.softserveinc.ita.homeproject.application.api;

import com.softserveinc.ita.homeproject.api.ApartmentsApi;
import com.softserveinc.ita.homeproject.model.CreateApartmentInvitation;
import com.softserveinc.ita.homeproject.model.UpdateApartmentInvitation;
import com.softserveinc.ita.homeproject.model.UpdateOwnership;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;

public class ApartmentApi extends CommonApi implements ApartmentsApi {
    @Override
    public Response createInvitation(Long apartmentId, @Valid CreateApartmentInvitation createApartmentInvitation) {
        return null;
    }

    @Override
    public Response deleteInvitation(Long apartmentId, Long id) {
        return null;
    }

    @Override
    public Response deleteOwnership(Long apartmentId, Long id) {
        return null;
    }

    @Override
    public Response getInvitation(Long apartmentId, Long id) {
        return null;
    }

    @Override
    public Response getOwnership(Long apartmentId, Long id) {
        return null;
    }

    @Override
    public Response queryInvitation(Long apartmentId, @Min(1) Integer pageNumber, @Min(1) @Max(10) Integer pageSize, String sort, String filter, Long id, String email, @DecimalMin("0.00010") @DecimalMax("1.0") BigDecimal ownershipPart, String status) {
        return null;
    }

    @Override
    public Response queryOwnership(Long apartmentId, @Min(1) Integer pageNumber, @Min(1) @Max(10) Integer pageSize, String sort, String filter, Long id, Long userId, @DecimalMin("0.00010") @DecimalMax("1.0") BigDecimal ownershipPart) {
        return null;
    }

    @Override
    public Response updateInvitation(Long apartmentId, Long id, @Valid UpdateApartmentInvitation updateApartmentInvitation) {
        return null;
    }

    @Override
    public Response updateOwnership(Long apartmentId, Long id, @Valid UpdateOwnership updateOwnership) {
        return null;
    }
}
