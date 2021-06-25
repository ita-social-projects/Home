package com.softserveinc.ita.homeproject.homedata.repository;

import java.util.List;

import com.softserveinc.ita.homeproject.homedata.entity.AnswerVariant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerVariantRepository extends JpaRepository<AnswerVariant, Long> {

    List<AnswerVariant> findAllByIdIn(List<Long> listId);
}
