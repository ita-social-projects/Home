package com.softserveinc.ita.homeproject.homedata.user.password;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.softserveinc.ita.homeproject.homedata.user.password.enums.PasswordRecoveryTokenStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface PasswordRecoveryRepository extends JpaRepository<PasswordRecovery, Long> {

    List<PasswordRecovery> findAllByEmail(String email);

    List<PasswordRecovery> findAllByStatus(PasswordRecoveryTokenStatus status);

    Optional<PasswordRecovery> findByRecoveryTokenAndEmail(String token, String email);

    @Modifying
    @Query(value = "update PasswordRecovery token set token.status = :status, token.sentDateTime = :sent_datetime "
        + "where token.id = :id")
    void updateSentDateTimeAndStatus(@Param("id") Long id,
                                     @Param("sent_datetime") LocalDateTime dateTime,
                                     @Param("status") PasswordRecoveryTokenStatus status);
}
