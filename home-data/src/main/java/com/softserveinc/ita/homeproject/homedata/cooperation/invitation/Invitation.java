package com.softserveinc.ita.homeproject.homedata.cooperation.invitation;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.softserveinc.ita.homeproject.homedata.BaseEntity;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.converters.InvitationStatusAttributeConverter;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.converters.InvitationTypeAttributeConverter;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.enums.InvitationStatus;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.enums.InvitationType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "invitations")
@SequenceGenerator(name = "sequence", sequenceName = "invitations_sequence")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
public abstract class Invitation extends BaseEntity {

    @Column(name = "email")
    private String email;

    @Convert(converter = InvitationTypeAttributeConverter.class)
    @Column(name = "type", insertable = false, updatable = false)
    private InvitationType type;

    @Convert(converter = InvitationStatusAttributeConverter.class)
    @Column(name = "status")
    private InvitationStatus status;

    @Column(name = "enabled")
    private Boolean enabled;

    @Column(name = "sent_datetime")
    private LocalDateTime sentDatetime;

    @Column(name = "registration_token")
    private String registrationToken;

    @Column(name = "end_time")
    private LocalDateTime requestEndTime;
}
