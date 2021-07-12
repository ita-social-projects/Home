package com.softserveinc.ita.homeproject.homeservice.service.impl;

import com.softserveinc.ita.homeproject.homedata.repository.UserRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.InvitationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.MailDto;
import com.softserveinc.ita.homeproject.homeservice.exception.InvitationException;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseEmailService {
    @Autowired
    protected ServiceMapper mapper;

    @Autowired
    protected MailService mailService;

    @Autowired
    private UserRepository userRepository;

    protected abstract void executeAllInvitationsByType();

    protected abstract MailDto createMailDto(InvitationDto invitationDto);

    protected void checkRegistration(InvitationDto invitationDto, MailDto mailDto) {
        if(userRepository.findByEmail(invitationDto.getEmail()).isEmpty()) {
            mailDto.setLink("https://home-project-academy.herokuapp.com/api/0/apidocs/index.html#post-/users");
            mailDto.setIsRegistered(false);
            return;
        }
        switch (invitationDto.getType()) {
            case COOPERATION:
                mailDto.setLink("Link for joining into cooperation");
                mailDto.setIsRegistered(true);
                break;
            case APARTMENT:
                mailDto.setLink("Link for joining into apartment");
                mailDto.setIsRegistered(true);
                break;
            default:
                throw new InvitationException("Wrong invitation type.");
        }
    }


}
