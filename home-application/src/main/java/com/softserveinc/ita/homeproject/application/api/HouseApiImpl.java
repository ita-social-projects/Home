package com.softserveinc.ita.homeproject.application.api;


import static com.softserveinc.ita.homeproject.application.security.constants.Permissions.MANAGE_IN_COOPERATION;
import static com.softserveinc.ita.homeproject.application.security.constants.Permissions.READ_APARTMENT_INFO;

import java.math.BigDecimal;
import java.util.List;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.softserveinc.ita.homeproject.application.model.CreateApartment;
import com.softserveinc.ita.homeproject.application.model.ReadApartment;
import com.softserveinc.ita.homeproject.application.model.ReadOwner;
import com.softserveinc.ita.homeproject.application.model.ReadOwnership;
import com.softserveinc.ita.homeproject.application.model.UpdateApartment;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.apartment.ApartmentDto;
import com.softserveinc.ita.homeproject.homeservice.service.cooperation.apartment.ApartmentService;
import com.softserveinc.ita.homeproject.homeservice.service.cooperation.house.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Provider
@Component
public class HouseApiImpl extends CommonApi implements HousesApi {

    @Autowired
    private ApartmentService apartmentService;

    @Autowired
    private HouseService houseService;

    @PreAuthorize(MANAGE_IN_COOPERATION)
    @Override
    public Response createApartment(Long houseId, CreateApartment createApartment) {
        ReadOwner owner1 = new ReadOwner()
            .firstName("John")
            .middleName("Charles")
            .lastName("Doe")
            .contacts(null)
            .id(1L);
        ReadOwner owner2 = new ReadOwner()
            .firstName("Caren")
            .middleName("Marry")
            .lastName("Doe")
            .contacts(null)
            .id(2L);
        ReadOwnership ownership1 = new ReadOwnership()
            .owner(owner1)
            .ownershipPart("0.7")
            .id(1L);
        ReadOwnership ownership2 = new ReadOwnership()
            .owner(owner2)
            .ownershipPart("0.3")
            .id(2L);
        ReadApartment readApartment = new ReadApartment()
            .apartmentArea(new BigDecimal(75.25))
            .apartmentNumber("23")
            .ownerships(List.of(ownership1, ownership2));

        return Response.status(Response.Status.CREATED).entity(readApartment).build();
    }

    @PreAuthorize(MANAGE_IN_COOPERATION)
    @Override
    public Response deleteApartment(Long houseId, Long id) {

        apartmentService.deactivateApartment(houseId, id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @PreAuthorize(READ_APARTMENT_INFO)
    @Override
    public Response getApartment(Long houseId, Long id) {
        ApartmentDto toGet = apartmentService.getOne(id, getSpecification());
        ReadApartment readApartment = mapper.convert(toGet, ReadApartment.class);

        return Response.status(Response.Status.OK).entity(readApartment).build();
    }

    @PreAuthorize(READ_APARTMENT_INFO)
    @Override
    public Response queryApartment(Long houseId,
                                   Integer pageNumber,
                                   Integer pageSize,
                                   String sort,
                                   String filter,
                                   Long id,
                                   String apartmentNumber,
                                   BigDecimal apartmentArea) {
        verifyExistence(houseId, houseService);
        Page<ApartmentDto> readApartment = apartmentService.findAll(pageNumber, pageSize, getSpecification());
        return buildQueryResponse(readApartment, ReadApartment.class);
    }

    @PreAuthorize(MANAGE_IN_COOPERATION)
    @Override
    public Response updateApartment(Long houseId, Long id, UpdateApartment updateApartment) {
        ApartmentDto updateApartmentDto = mapper.convert(updateApartment, ApartmentDto.class);
        ApartmentDto toUpdate = apartmentService.updateApartment(houseId, id, updateApartmentDto);
        ReadApartment readApartment = mapper.convert(toUpdate, ReadApartment.class);

        return Response.status(Response.Status.OK).entity(readApartment).build();
    }

}
