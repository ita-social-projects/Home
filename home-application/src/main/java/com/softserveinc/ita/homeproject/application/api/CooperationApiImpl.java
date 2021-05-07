package com.softserveinc.ita.homeproject.application.api;

import static com.softserveinc.ita.homeproject.application.constants.Permissions.CREATE_COOPERATION_PERMISSION;
import static com.softserveinc.ita.homeproject.application.constants.Permissions.CREATE_COOP_CONTACT_PERMISSION;
import static com.softserveinc.ita.homeproject.application.constants.Permissions.CREATE_HOUSE_PERMISSION;
import static com.softserveinc.ita.homeproject.application.constants.Permissions.DELETE_COOPERATION_PERMISSION;
import static com.softserveinc.ita.homeproject.application.constants.Permissions.DELETE_COOP_CONTACT_PERMISSION;
import static com.softserveinc.ita.homeproject.application.constants.Permissions.DELETE_HOUSE_PERMISSION;
import static com.softserveinc.ita.homeproject.application.constants.Permissions.GET_ALL_COOPERATION_PERMISSION;
import static com.softserveinc.ita.homeproject.application.constants.Permissions.GET_ALL_COOP_CONTACT_PERMISSION;
import static com.softserveinc.ita.homeproject.application.constants.Permissions.GET_COOPERATION_PERMISSION;
import static com.softserveinc.ita.homeproject.application.constants.Permissions.GET_COOP_CONTACT_PERMISSION;
import static com.softserveinc.ita.homeproject.application.constants.Permissions.GET_HOUSES_PERMISSION;
import static com.softserveinc.ita.homeproject.application.constants.Permissions.GET_HOUSE_PERMISSION;
import static com.softserveinc.ita.homeproject.application.constants.Permissions.UPDATE_COOPERATION_PERMISSION;
import static com.softserveinc.ita.homeproject.application.constants.Permissions.UPDATE_COOP_CONTACT_PERMISSION;
import static com.softserveinc.ita.homeproject.application.constants.Permissions.UPDATE_HOUSE_PERMISSION;

import java.math.BigDecimal;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.softserveinc.ita.homeproject.api.CooperationApi;
import com.softserveinc.ita.homeproject.homeservice.dto.ContactDto;
import com.softserveinc.ita.homeproject.homeservice.dto.CooperationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.HouseDto;
import com.softserveinc.ita.homeproject.homeservice.service.CooperationContactService;
import com.softserveinc.ita.homeproject.homeservice.service.CooperationService;
import com.softserveinc.ita.homeproject.homeservice.service.HouseService;
import com.softserveinc.ita.homeproject.model.CreateContact;
import com.softserveinc.ita.homeproject.model.CreateCooperation;
import com.softserveinc.ita.homeproject.model.CreateHouse;
import com.softserveinc.ita.homeproject.model.ReadContact;
import com.softserveinc.ita.homeproject.model.ReadCooperation;
import com.softserveinc.ita.homeproject.model.ReadHouse;
import com.softserveinc.ita.homeproject.model.UpdateContact;
import com.softserveinc.ita.homeproject.model.UpdateCooperation;
import com.softserveinc.ita.homeproject.model.UpdateHouse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;


@Provider
@Component
public class CooperationApiImpl extends CommonApi implements CooperationApi {

    @Autowired
    private CooperationService cooperationService;

    @Autowired
    private HouseService houseService;

    @Autowired
    private CooperationContactService contactService;

    @PreAuthorize(CREATE_COOPERATION_PERMISSION)
    @Override
    public Response createCooperation(CreateCooperation createCooperation) {
        CooperationDto createCoopDto = mapper.convert(createCooperation, CooperationDto.class);
        CooperationDto readCoopDto = cooperationService.createCooperation(createCoopDto);
        ReadCooperation readCoop = mapper.convert(readCoopDto, ReadCooperation.class);

        return Response.status(Response.Status.CREATED).entity(readCoop).build();
    }

    @PreAuthorize(CREATE_HOUSE_PERMISSION)
    @Override
    public Response createHouse(Long cooperationId, @Valid CreateHouse createHouse) {
        HouseDto createHouseDto = mapper.convert(createHouse, HouseDto.class);
        HouseDto readHouseDto = houseService.createHouse(cooperationId, createHouseDto);
        ReadHouse readHouse = mapper.convert(readHouseDto, ReadHouse.class);

        return Response.status(Response.Status.CREATED).entity(readHouse).build();
    }

    @PreAuthorize(CREATE_COOP_CONTACT_PERMISSION)
    @Override
    public Response createContactOnCooperation(Long cooperationId,
                                                @Valid CreateContact createCooperationContact) {
        var createContactDto = mapper.convert(createCooperationContact, ContactDto.class);
        var readContactDto = contactService.createContact(cooperationId, createContactDto);
        var readContact = mapper.convert(readContactDto, ReadContact.class);

        return Response.status(Response.Status.CREATED).entity(readContact).build();
    }

    @PreAuthorize(GET_COOPERATION_PERMISSION)
    @Override
    public Response getCooperation(Long id) {
        CooperationDto readCoopDto = (CooperationDto) queryApiService.getOne(uriInfo, cooperationService);
        ReadCooperation readCoop = mapper.convert(readCoopDto, ReadCooperation.class);

        return Response.status(Response.Status.OK).entity(readCoop).build();
    }


    @PreAuthorize(GET_HOUSE_PERMISSION)
    @Override
    public Response getHouse(Long cooperationId, Long id) {
        HouseDto toGet = (HouseDto) queryApiService.getOne(uriInfo, houseService);
        ReadHouse readHouse = mapper.convert(toGet, ReadHouse.class);

        return Response.status(Response.Status.OK).entity(readHouse).build();
    }

    @PreAuthorize(GET_COOP_CONTACT_PERMISSION)
    @Override
    public Response getContactOnCooperation(Long cooperationId, Long contactId) {
        var readContactDto = contactService.getContactById(contactId);
        var readContact = mapper.convert(readContactDto, ReadContact.class);

        return Response.status(Response.Status.OK).entity(readContact).build();
    }

    @PreAuthorize(GET_ALL_COOPERATION_PERMISSION)
    @Override
    public Response queryCooperation(@Min(1) Integer pageNumber,
                                     @Min(1) @Max(10) Integer pageSize,
                                     String sort,
                                     String filter,
                                     Long id,
                                     String name,
                                     String iban,
                                     String usreo) {

        Page<CooperationDto> readCooperation = queryApiService.getPageFromQuery(uriInfo, cooperationService);
        return buildQueryResponse(readCooperation, ReadCooperation.class);
    }

    @PreAuthorize(GET_HOUSES_PERMISSION)
    @Override
    public Response queryHouse( Long cooperationId,
                               @Min(1) Integer pageNumber,
                               @Min(1) @Max(10) Integer pageSize,
                               String sort,
                               String filter,
                               Long id,
                               Integer quantityFlat,
                               Integer adjoiningArea,
                               BigDecimal houseArea) {

        Page<HouseDto> readHouse = queryApiService.getPageFromQuery(uriInfo, houseService);
        return buildQueryResponse(readHouse, ReadHouse.class);
    }

    @PreAuthorize(GET_ALL_COOP_CONTACT_PERMISSION)
    @Override
    public Response queryContactsOnCooperation(Long cooperationId,
                                               @Min(1) Integer pageNumber,
                                               @Min(1) @Max(10) Integer pageSize,
                                               String sort,
                                               String filter,
                                               Long id, String phone,
                                               String email,
                                               String main,
                                               String type) {
        Page<ContactDto> readContact = queryApiService.getPageFromQuery(uriInfo, contactService);
        return buildQueryResponse(readContact, ReadContact.class);
    }

    @PreAuthorize(DELETE_COOPERATION_PERMISSION)
    @Override
    public Response deleteCooperation(Long id) {
        cooperationService.deactivateCooperation(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @PreAuthorize(DELETE_HOUSE_PERMISSION)
    @Override
    public Response deleteHouse(Long cooperationId, Long id) {
        houseService.deactivateById(cooperationId, id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @PreAuthorize(DELETE_COOP_CONTACT_PERMISSION)
    @Override
    public Response deleteContactOnCooperation(Long cooperationId, Long contactId) {
        contactService.deactivateContact(contactId);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @PreAuthorize(UPDATE_COOPERATION_PERMISSION)
    @Override
    public Response updateCooperation(Long id, @Valid UpdateCooperation updateCooperation) {
        CooperationDto updateCoopDto = mapper.convert(updateCooperation, CooperationDto.class);
        CooperationDto toUpdate = cooperationService.updateCooperation(id, updateCoopDto);
        ReadCooperation readCooperation = mapper.convert(toUpdate, ReadCooperation.class);

        return Response.status(Response.Status.OK).entity(readCooperation).build();
    }

    @PreAuthorize(UPDATE_HOUSE_PERMISSION)
    @Override
    public Response updateHouse(Long cooperationId, Long id, @Valid UpdateHouse updateHouse) {
        HouseDto updateHouseDto = mapper.convert(updateHouse, HouseDto.class);
        HouseDto toUpdate = houseService.updateHouse(id, updateHouseDto);
        ReadHouse readHouse = mapper.convert(toUpdate, ReadHouse.class);

        return Response.status(Response.Status.OK).entity(readHouse).build();
    }

    @PreAuthorize(UPDATE_COOP_CONTACT_PERMISSION)
    @Override
    public Response updateContactOnCooperation(Long cooperationId, Long contactId, @Valid UpdateContact updateContact) {
        var updateContactDto = mapper.convert(updateContact, ContactDto.class);
        var updatedContactDto = contactService.updateContact(cooperationId, contactId, updateContactDto);
        var readContact = mapper.convert(updatedContactDto, ReadContact.class);

        return Response.status(Response.Status.OK).entity(readContact).build();
    }
}
