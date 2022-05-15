package com.softserveinc.ita.homeproject.homedata.user;

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
@Table(name = "user_tokens")
@SequenceGenerator(name = "sequence", sequenceName="user_tokens_sequence")
public class UserSession extends BaseEntity {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "expire_date")
    private LocalDateTime expireDate;
}
