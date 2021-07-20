package com.softserveinc.ita.homeproject.application.api;

import static com.softserveinc.ita.homeproject.application.constants.Permissions.CREATE_COOPERATION_PERMISSION;
import static com.softserveinc.ita.homeproject.application.constants.Permissions.CREATE_COOP_CONTACT_PERMISSION;
import static com.softserveinc.ita.homeproject.application.constants.Permissions.CREATE_HOUSE_PERMISSION;
import static com.softserveinc.ita.homeproject.application.constants.Permissions.CREATE_POLL_PERMISSION;
import static com.softserveinc.ita.homeproject.application.constants.Permissions.DELETE_COOPERATION_PERMISSION;
import static com.softserveinc.ita.homeproject.application.constants.Permissions.DELETE_COOP_CONTACT_PERMISSION;
import static com.softserveinc.ita.homeproject.application.constants.Permissions.DELETE_HOUSE_PERMISSION;
import static com.softserveinc.ita.homeproject.application.constants.Permissions.DELETE_POLL_PERMISSION;
import static com.softserveinc.ita.homeproject.application.constants.Permissions.GET_ALL_COOPERATION_PERMISSION;
import static com.softserveinc.ita.homeproject.application.constants.Permissions.GET_ALL_COOP_CONTACT_PERMISSION;
import static com.softserveinc.ita.homeproject.application.constants.Permissions.GET_COOPERATION_PERMISSION;
import static com.softserveinc.ita.homeproject.application.constants.Permissions.GET_COOP_CONTACT_PERMISSION;
import static com.softserveinc.ita.homeproject.application.constants.Permissions.GET_HOUSES_PERMISSION;
import static com.softserveinc.ita.homeproject.application.constants.Permissions.GET_HOUSE_PERMISSION;
import static com.softserveinc.ita.homeproject.application.constants.Permissions.GET_POLL_PERMISSION;
import static com.softserveinc.ita.homeproject.application.constants.Permissions.UPDATE_COOPERATION_PERMISSION;
import static com.softserveinc.ita.homeproject.application.constants.Permissions.UPDATE_COOP_CONTACT_PERMISSION;
import static com.softserveinc.ita.homeproject.application.constants.Permissions.UPDATE_HOUSE_PERMISSION;
import static com.softserveinc.ita.homeproject.application.constants.Permissions.UPDATE_POLL_PERMISSION;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.softserveinc.ita.homeproject.api.CooperationApi;
import com.softserveinc.ita.homeproject.homeservice.dto.ContactDto;
import com.softserveinc.ita.homeproject.homeservice.dto.CooperationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.HouseDto;
import com.softserveinc.ita.homeproject.homeservice.dto.PollDto;
import com.softserveinc.ita.homeproject.homeservice.service.CooperationContactService;
import com.softserveinc.ita.homeproject.homeservice.service.CooperationService;
import com.softserveinc.ita.homeproject.homeservice.service.HouseService;
import com.softserveinc.ita.homeproject.homeservice.service.PollService;
import com.softserveinc.ita.homeproject.model.ContactType;
import com.softserveinc.ita.homeproject.model.CreateContact;
import com.softserveinc.ita.homeproject.model.CreateCooperation;
import com.softserveinc.ita.homeproject.model.CreateHouse;
import com.softserveinc.ita.homeproject.model.CreatePoll;
import com.softserveinc.ita.homeproject.model.HouseLookup;
import com.softserveinc.ita.homeproject.model.PollStatus;
import com.softserveinc.ita.homeproject.model.PollType;
import com.softserveinc.ita.homeproject.model.ReadContact;
import com.softserveinc.ita.homeproject.model.ReadCooperation;
import com.softserveinc.ita.homeproject.model.ReadHouse;
import com.softserveinc.ita.homeproject.model.ReadPoll;
import com.softserveinc.ita.homeproject.model.UpdateContact;
import com.softserveinc.ita.homeproject.model.UpdateCooperation;
import com.softserveinc.ita.homeproject.model.UpdateHouse;
import com.softserveinc.ita.homeproject.model.UpdatePoll;
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

    @Autowired
    private PollService pollService;

    private HouseDto getHouseDto(Long cooperationId, Long houseId) {
        return (HouseDto)getChildDto(cooperationId, houseId, cooperationService, houseService);
    }

    private ContactDto getContactDto(Long cooperationId, Long contactId) {
        return (ContactDto)getChildDto(cooperationId, contactId, cooperationService, contactService);
    }

    private PollDto getPollDto(Long cooperationId, Long contactId) {
        return (PollDto)getChildDto(cooperationId, contactId, cooperationService, pollService);
    }

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
    public Response createHouse(Long cooperationId, CreateHouse createHouse) {
        HouseDto createHouseDto = mapper.convert(createHouse, HouseDto.class);
        HouseDto readHouseDto = houseService.createHouse(cooperationId, createHouseDto);
        ReadHouse readHouse = mapper.convert(readHouseDto, ReadHouse.class);
        return Response.status(Response.Status.CREATED).entity(readHouse).build();
    }

    @PreAuthorize(CREATE_COOP_CONTACT_PERMISSION)
    @Override
    public Response createContactOnCooperation(Long cooperationId,
                                               CreateContact createCooperationContact) {
        var createContactDto = mapper.convert(createCooperationContact, ContactDto.class);
        var readContactDto = contactService.createContact(cooperationId, createContactDto);
        var readContact = mapper.convert(readContactDto, ReadContact.class);
        return Response.status(Response.Status.CREATED).entity(readContact).build();
    }

    @PreAuthorize(CREATE_POLL_PERMISSION)
    @Override
    public Response createCooperationPoll(Long cooperationId, CreatePoll createPoll) {
        PollDto createPollDto = mapper.convert(createPoll, PollDto.class);
        createPollDto.setPolledHouses(createPoll.getHouses().stream()
            .map(HouseLookup::getId)
            .map(id -> houseService.getOne(id))
            .collect(Collectors.toList()));
        PollDto readPollDto = pollService.create(cooperationId, createPollDto);
        ReadPoll readPoll = mapper.convert(readPollDto, ReadPoll.class);
        return Response.status(Response.Status.CREATED).entity(readPoll).build();
    }

    @PreAuthorize(GET_COOPERATION_PERMISSION)
    @Override
    public Response getCooperation(Long id) {
        CooperationDto readCoopDto = cooperationService.getOne(id);
        ReadCooperation readCoop = mapper.convert(readCoopDto, ReadCooperation.class);
        return Response.status(Response.Status.OK).entity(readCoop).build();
    }

    @PreAuthorize(GET_HOUSE_PERMISSION)
    @Override
    public Response getHouse(Long cooperationId, Long id) {
        ReadHouse readHouse = mapper.convert(getHouseDto(cooperationId,id), ReadHouse.class);
        return Response.status(Response.Status.OK).entity(readHouse).build();
    }

    @PreAuthorize(GET_COOP_CONTACT_PERMISSION)
    @Override
    public Response getContactOnCooperation(Long cooperationId, Long id) {
        var readContact = mapper.convert(getContactDto(cooperationId,id), ReadContact.class);
        return Response.status(Response.Status.OK).entity(readContact).build();
    }

    @PreAuthorize(GET_POLL_PERMISSION)
    @Override
    public Response getCooperationPoll(Long cooperationId, Long id) {
        ReadPoll readPoll = mapper.convert(getPollDto(cooperationId,id), ReadPoll.class);
        return Response.status(Response.Status.OK).entity(readPoll).build();
    }

    @PreAuthorize(GET_ALL_COOPERATION_PERMISSION)
    @Override
    public Response queryCooperation(Integer pageNumber,
                                     Integer pageSize,
                                     String sort,
                                     String filter,
                                     Long id,
                                     String name,
                                     String iban,
                                     String usreo) {
        Page<CooperationDto> readCooperation = cooperationService.findAll(pageNumber, pageSize, getSpecification());
        return buildQueryResponse(readCooperation, ReadCooperation.class);
    }

    @PreAuthorize(GET_HOUSES_PERMISSION)
    @Override
    public Response queryHouse(Long cooperationId,
                               Integer pageNumber,
                               Integer pageSize,
                               String sort,
                               String filter,
                               Long id,
                               Integer quantityFlat,
                               Integer adjoiningArea,
                               BigDecimal houseArea) {
        verifyExistence(cooperationId, cooperationService);
        Page<HouseDto> readHouse = houseService.findAll(pageNumber, pageSize, getSpecification());
        return buildQueryResponse(readHouse, ReadHouse.class);
    }

    @PreAuthorize(GET_ALL_COOP_CONTACT_PERMISSION)
    @Override
    public Response queryContactsOnCooperation(Long cooperationId,
                                               Integer pageNumber,
                                               Integer pageSize,
                                               String sort,
                                               String filter,
                                               Long id, String phone,
                                               String email,
                                               String main,
                                               ContactType type) {
        verifyExistence(cooperationId, cooperationService);
        Page<ContactDto> readContact = contactService.findAll(pageNumber, pageSize, getSpecification());
        return buildQueryResponse(readContact, ReadContact.class);
    }

    @PreAuthorize(GET_POLL_PERMISSION)
    @Override
    public Response queryCooperationPoll(Long cooperationId,
                                         Integer pageNumber,
                                         Integer pageSize,
                                         String sort,
                                         String filter,
                                         Long id,
                                         LocalDateTime creationDate,
                                         LocalDateTime completionDate,
                                         PollType type,
                                         PollStatus status) {
        //        verifyExistence(cooperationId, cooperationService);
        Page<PollDto> readPoll = (Page<PollDto>) getAllChildDtos(cooperationId,
                pageNumber,
                pageSize,
                cooperationService,
                pollService);
        //        pollService.findAll(pageNumber, pageSize, getSpecification());
        return buildQueryResponse(readPoll, ReadPoll.class);
    }

    @PreAuthorize(DELETE_COOPERATION_PERMISSION)
    @Override
    public Response deleteCooperation(Long id) {
        //todo dto
        cooperationService.deactivateCooperation(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @PreAuthorize(DELETE_HOUSE_PERMISSION)
    @Override
    public Response deleteHouse(Long cooperationId, Long id) {
        houseService.deactivate(getHouseDto(cooperationId,id));
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @PreAuthorize(DELETE_COOP_CONTACT_PERMISSION)
    @Override
    public Response deleteContactOnCooperation(Long cooperationId, Long id) {
        //todo dto
        contactService.deactivateContact(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @PreAuthorize(DELETE_POLL_PERMISSION)
    @Override
    public Response deleteCooperationPoll(Long cooperationId, Long id) {
        //todo dto
        pollService.deactivate(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @PreAuthorize(UPDATE_COOPERATION_PERMISSION)
    @Override
    public Response updateCooperation(Long id, UpdateCooperation updateCooperation) {
        CooperationDto updateCoopDto = mapper.convert(updateCooperation, CooperationDto.class);
        //todo dto
        CooperationDto toUpdate = cooperationService.updateCooperation(id, updateCoopDto);
        ReadCooperation readCooperation = mapper.convert(toUpdate, ReadCooperation.class);
        return Response.status(Response.Status.OK).entity(readCooperation).build();
    }

    @PreAuthorize(UPDATE_HOUSE_PERMISSION)
    @Override
    public Response updateHouse(Long cooperationId, Long id, UpdateHouse updateHouse) {
        HouseDto oldHouseDto = getHouseDto(cooperationId, id);
        HouseDto updateHouseDto = mapper.convert(updateHouse, HouseDto.class);
        HouseDto toUpdate = houseService.updateHouse(oldHouseDto, updateHouseDto);
        ReadHouse readHouse = mapper.convert(toUpdate, ReadHouse.class);
        return Response.status(Response.Status.OK).entity(readHouse).build();
    }

    @PreAuthorize(UPDATE_COOP_CONTACT_PERMISSION)
    @Override
    public Response updateContactOnCooperation(Long cooperationId, Long id, UpdateContact updateContact) {
        var updateContactDto = mapper.convert(updateContact, ContactDto.class);
        //todo dto
        var updatedContactDto = contactService.updateContact(cooperationId, id, updateContactDto);
        var readContact = mapper.convert(updatedContactDto, ReadContact.class);

        return Response.status(Response.Status.OK).entity(readContact).build();
    }

    @PreAuthorize(UPDATE_POLL_PERMISSION)
    @Override
    public Response updateCooperationPoll(Long cooperationId, Long id, UpdatePoll updatePoll) {
        PollDto updatePollDto = mapper.convert(updatePoll, PollDto.class);
        //todo dto
        PollDto updatedPollDto = pollService.update(cooperationId, id, updatePollDto);
        ReadPoll readPoll = mapper.convert(updatedPollDto, ReadPoll.class);

        return Response.status(Response.Status.OK).entity(readPoll).build();
    }
}
