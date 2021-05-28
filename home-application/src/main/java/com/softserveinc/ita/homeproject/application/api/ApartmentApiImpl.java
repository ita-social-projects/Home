package com.softserveinc.ita.homeproject.application.api;

import static com.softserveinc.ita.homeproject.application.constants.Permissions.DELETE_OWNERSHIP_PERMISSION;
import static com.softserveinc.ita.homeproject.application.constants.Permissions.GET_OWNERSHIP_PERMISSION;
import static com.softserveinc.ita.homeproject.application.constants.Permissions.UPDATE_OWNERSHIP_PERMISSION;

import java.math.BigDecimal;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.softserveinc.ita.homeproject.api.ApartmentsApi;
import com.softserveinc.ita.homeproject.homeservice.dto.OwnershipDto;
import com.softserveinc.ita.homeproject.homeservice.service.OwnershipService;
import com.softserveinc.ita.homeproject.model.CreateApartmentInvitation;
import com.softserveinc.ita.homeproject.model.ReadOwnership;
import com.softserveinc.ita.homeproject.model.UpdateApartmentInvitation;
import com.softserveinc.ita.homeproject.model.UpdateOwnership;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Provider
@Component
public class ApartmentApiImpl extends CommonApi implements ApartmentsApi {

    @Autowired
    private OwnershipService ownershipService;

    @Override
    public Response createInvitation(Long apartmentId, @Valid CreateApartmentInvitation createApartmentInvitation) {
        throw new UnsupportedOperationException("Unsupported endpoint createInvitation");
    }

    @Override
    public Response deleteInvitation(Long apartmentId, Long id) {
        throw new UnsupportedOperationException("Unsupported endpoint deleteInvitation");
    }

    @PreAuthorize(DELETE_OWNERSHIP_PERMISSION)
    @Override
    public Response deleteOwnership(Long apartmentId, Long id) {
        ownershipService.deactivateOwnershipById(apartmentId, id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @Override
    public Response getInvitation(Long apartmentId, Long id) {
        throw new UnsupportedOperationException("Unsupported endpoint getInvitation");
    }

    @PreAuthorize(GET_OWNERSHIP_PERMISSION)
    @Override
    public Response getOwnership(Long apartmentId, Long id) {
        OwnershipDto toGet = ownershipService.getOne(id, getSpecification());
        var readOwnership = mapper.convert(toGet, ReadOwnership.class);

        return Response.status(Response.Status.OK).entity(readOwnership).build();
    }

    @Override
    public Response queryInvitation(Long apartmentId,
                                    @Min(1) Integer pageNumber,
                                    @Min(1) @Max(10) Integer pageSize,
                                    String sort,
                                    String filter,
                                    Long id,
                                    String email,
                                    @DecimalMin("0.00010") @DecimalMax("1.0") BigDecimal ownershipPart,
                                    String status) {
        throw new UnsupportedOperationException("Unsupported endpoint queryInvitation");
    }

    @PreAuthorize(GET_OWNERSHIP_PERMISSION)
    @Override
    public Response queryOwnership(Long apartmentId,
                                   @Min(1) Integer pageNumber,
                                   @Min(1) @Max(10) Integer pageSize,
                                   String sort,
                                   String filter,
                                   Long id,
                                   Long userId,
                                   @DecimalMin("0.00010") @DecimalMax("1.0") BigDecimal ownershipPart) {

        Page<OwnershipDto> readOwnership = ownershipService.findAll(pageNumber, pageSize, getSpecification());
        return buildQueryResponse(readOwnership, ReadOwnership.class);
    }

    @Override
    public Response updateInvitation(Long apartmentId,
                                     Long id,
                                     @Valid UpdateApartmentInvitation updateApartmentInvitation) {
        throw new UnsupportedOperationException("Unsupported endpoint updateInvitation");
    }

    @PreAuthorize(UPDATE_OWNERSHIP_PERMISSION)
    @Override
    public Response updateOwnership(Long apartmentId, Long id, @Valid UpdateOwnership updateOwnership) {
        var updateOwnershipDto = mapper.convert(updateOwnership, OwnershipDto.class);
        var toUpdate = ownershipService.updateOwnership(apartmentId, id, updateOwnershipDto);
        var readOwnership = mapper.convert(toUpdate, ReadOwnership.class);

        return Response.status(Response.Status.OK).entity(readOwnership).build();
    }
}
