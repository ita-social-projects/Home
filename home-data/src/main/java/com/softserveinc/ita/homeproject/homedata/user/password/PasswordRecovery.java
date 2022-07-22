package com.softserveinc.ita.homeproject.homedata.user.password;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.softserveinc.ita.homeproject.homedata.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "password_recovery_tokens")
@SequenceGenerator(name = "sequence", sequenceName = "password_recovery_tokens_sequence")
public class PasswordRecoveryToken extends BaseEntity {

    @Column(name = "email")
    private String email;

    @Column(name = "recovery_token")
    private String recoveryToken;

    @Column(name = "sent_datetime")
    private LocalDateTime sentDateTime;

    @Column(name = "enabled")
    private Boolean enabled;
}
