package com.softserveinc.ita.homeproject.homeservice.service.user.password;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.softserveinc.ita.homeproject.homedata.user.password.PasswordRecovery;
import com.softserveinc.ita.homeproject.homedata.user.password.PasswordRecoveryRepository;
import com.softserveinc.ita.homeproject.homedata.user.password.enums.PasswordRecoveryTokenStatus;
import com.softserveinc.ita.homeproject.homeservice.dto.user.password.PasswordRecoveryTokenDto;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.service.general.email.Mailable;
import com.softserveinc.ita.homeproject.homeservice.service.general.email.MailableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class PasswordRestorationServiceImpl implements MailableService {

    @Autowired
    private ServiceMapper mapper;

    @Autowired
    private PasswordRecoveryRepository passwordRecoveryRepository;

    @Override
    public List<? extends Mailable> getAllUnsentLetters() {
        return passwordRecoveryRepository.findAllByStatus(PasswordRecoveryTokenStatus.PENDING).stream()
                .filter(PasswordRecovery::getEnabled)
                .map(token -> mapper.convert(token, PasswordRecoveryTokenDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateSentDateTimeAndStatus(Long id) {
        passwordRecoveryRepository.updateSentDateTimeAndStatus(
            id,
            LocalDateTime.now(),
            PasswordRecoveryTokenStatus.ACTIVE
        );
    }
}
