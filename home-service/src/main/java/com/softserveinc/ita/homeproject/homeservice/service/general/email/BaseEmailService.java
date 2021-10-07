package com.softserveinc.ita.homeproject.homeservice.service.general.email;

import com.softserveinc.ita.homeproject.homedata.repository.UserRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.InvitationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.MailDto;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.service.general.mail.MailService;
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
        } else {
            mailDto.setLink("https://home-project-academy.herokuapp.com/api/0/apidocs/index.html#post-/invitations/invitation-approval");
            mailDto.setIsRegistered(true);
        }
    }
}
