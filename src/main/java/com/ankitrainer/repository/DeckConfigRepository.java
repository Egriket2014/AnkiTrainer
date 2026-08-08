package com.ankitrainer.repository;

import com.ankitrainer.entity.DeckConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeckConfigRepository extends JpaRepository<DeckConfigEntity, Long> {

    Optional<DeckConfigEntity> findByDeckName(String deckName);

    boolean existsByDeckName(String deckName);

//    @Query("SELECT d.id, d.deckName FROM DeckConfigEntity d")
//    List<DeckConfigEntity> findAllDeckNames();


    void deleteByDeckName(String deckName);
}