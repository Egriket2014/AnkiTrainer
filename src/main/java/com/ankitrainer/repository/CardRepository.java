package com.ankitrainer.repository;

import com.ankitrainer.entity.CardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<CardEntity, Long> {

    Optional<CardEntity> findByNoteId(Long noteId);

    boolean existsByNoteId(Long noteId);
}
