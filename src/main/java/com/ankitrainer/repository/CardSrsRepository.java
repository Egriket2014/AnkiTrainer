package com.ankitrainer.repository;

import com.ankitrainer.entity.CardSrsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CardSrsRepository extends JpaRepository<CardSrsEntity, Long> {

    @Query(value = """
            SELECT 
                c.id AS id,
                c.card_id AS card_id,
                c.conjugation_type AS conjugation_type,
                c.answer AS answer,
                c.srs_json AS srs_json,
                c.created_at AS created_at,
                c.updated_at AS updated_at
            FROM card_srs c
            JOIN card card ON c.card_id = card.id
            WHERE card.deck_name = :deckName
              AND c.conjugation_type = :conjugationType
              AND c.srs_json->>'state' = 'LEARNING'
              AND c.srs_json->>'lastReview' IS NULL
            ORDER BY (c.srs_json->>'due')::timestamp ASC
            LIMIT :limit
            """, nativeQuery = true)
    List<CardSrsEntity> findNewCardsForToday(@Param("deckName") String deckName,
                                             @Param("conjugationType") String conjugationType,
                                             @Param("limit") int limit);

    @Query(value = """
            SELECT 
                c.id AS id,
                c.card_id AS card_id,
                c.conjugation_type AS conjugation_type,
                c.answer AS answer,
                c.srs_json AS srs_json,
                c.created_at AS created_at,
                c.updated_at AS updated_at
            FROM card_srs c
            JOIN card card ON c.card_id = card.id
            WHERE card.deck_name = :deckName
              AND c.conjugation_type = :conjugationType
              AND c.srs_json->>'state' = 'LEARNING'
              AND (c.srs_json->>'lastReview')::date = CAST(:today AS date)
              AND (c.srs_json->>'due')::timestamp <= CURRENT_TIMESTAMP
            ORDER BY (c.srs_json->>'due')::timestamp ASC
            """, nativeQuery = true)
    List<CardSrsEntity> findSeenTodayNewCards(@Param("deckName") String deckName,
                                              @Param("conjugationType") String conjugationType,
                                              @Param("today") String today);

    @Query(value = """
            SELECT 
                c.id AS id,
                c.card_id AS card_id,
                c.conjugation_type AS conjugation_type,
                c.answer AS answer,
                c.srs_json AS srs_json,
                c.created_at AS created_at,
                c.updated_at AS updated_at
            FROM card_srs c
            JOIN card card ON c.card_id = card.id
            WHERE card.deck_name = :deckName
              AND c.conjugation_type = :conjugationType
              AND c.srs_json->>'state' = 'LEARNING'
              AND (c.srs_json->>'lastReview')::date < CAST(:today AS date)
              AND (c.srs_json->>'due')::timestamp <= CURRENT_TIMESTAMP
            ORDER BY (c.srs_json->>'due')::timestamp ASC
            """, nativeQuery = true)
    List<CardSrsEntity> findSeenNotTodayNewCards(@Param("deckName") String deckName,
                                                 @Param("conjugationType") String conjugationType,
                                                 @Param("today") String today);

    @Query(value = """
            SELECT 
                c.id AS id,
                c.card_id AS card_id,
                c.conjugation_type AS conjugation_type,
                c.answer AS answer,
                c.srs_json AS srs_json,
                c.created_at AS created_at,
                c.updated_at AS updated_at
            FROM card_srs c
            JOIN card card ON c.card_id = card.id
            WHERE card.deck_name = :deckName
              AND c.conjugation_type = :conjugationType
              AND c.srs_json->>'state' = 'RELEARNING'
              AND (c.srs_json->>'due')::timestamp <= CURRENT_TIMESTAMP
            ORDER BY (c.srs_json->>'due')::timestamp ASC
            """, nativeQuery = true)
    List<CardSrsEntity> findRelearningCards(@Param("deckName") String deckName,
                                            @Param("conjugationType") String conjugationType);

    @Query(value = """
            SELECT 
                c.id AS id,
                c.card_id AS card_id,
                c.conjugation_type AS conjugation_type,
                c.answer AS answer,
                c.srs_json AS srs_json,
                c.created_at AS created_at,
                c.updated_at AS updated_at
            FROM card_srs c
            JOIN card card ON c.card_id = card.id
            WHERE card.deck_name = :deckName
              AND c.conjugation_type = :conjugationType
              AND c.srs_json->>'state' = 'REVIEW'
              AND (c.srs_json->>'due')::timestamp <= CURRENT_TIMESTAMP
            ORDER BY (c.srs_json->>'due')::timestamp ASC
            LIMIT :limit
            """, nativeQuery = true)
    List<CardSrsEntity> findReviewCards(@Param("deckName") String deckName,
                                        @Param("conjugationType") String conjugationType,
                                        @Param("limit") int limit);

    @Query(value = """
            SELECT COUNT(*)
            FROM card_srs c
            JOIN card card ON c.card_id = card.id
            WHERE card.deck_name = :deckName
              AND c.conjugation_type = :conjugationType
              AND c.srs_json->>'state' = 'LEARNING'
              AND c.srs_json->>'lastReview' IS NULL
            """, nativeQuery = true)
    int countNewCardsForToday(@Param("deckName") String deckName,
                               @Param("conjugationType") String conjugationType);

    @Query(value = """
            SELECT COUNT(*)
            FROM card_srs c
            JOIN card card ON c.card_id = card.id
            WHERE card.deck_name = :deckName
              AND c.conjugation_type = :conjugationType
              AND c.srs_json->>'state' = 'LEARNING'
              AND (c.srs_json->>'lastReview')::date = CAST(:today AS date)
              AND (c.srs_json->>'due')::timestamp <= CURRENT_TIMESTAMP
            """, nativeQuery = true)
    int countSeenTodayNewCards(@Param("deckName") String deckName,
                                @Param("conjugationType") String conjugationType,
                                @Param("today") String today);

    @Query(value = """
            SELECT COUNT(*)
            FROM card_srs c
            JOIN card card ON c.card_id = card.id
            WHERE card.deck_name = :deckName
              AND c.conjugation_type = :conjugationType
              AND c.srs_json->>'state' = 'LEARNING'
              AND (c.srs_json->>'lastReview')::date < CAST(:today AS date)
              AND (c.srs_json->>'due')::timestamp <= CURRENT_TIMESTAMP
            """, nativeQuery = true)
    int countSeenNotTodayNewCards(@Param("deckName") String deckName,
                                   @Param("conjugationType") String conjugationType,
                                   @Param("today") String today);

    @Query(value = """
            SELECT COUNT(*)
            FROM card_srs c
            JOIN card card ON c.card_id = card.id
            WHERE card.deck_name = :deckName
              AND c.conjugation_type = :conjugationType
              AND c.srs_json->>'state' = 'RELEARNING'
              AND (c.srs_json->>'due')::timestamp <= CURRENT_TIMESTAMP
            """, nativeQuery = true)
    int countRelearningCards(@Param("deckName") String deckName,
                              @Param("conjugationType") String conjugationType);

    @Query(value = """
            SELECT COUNT(*)
            FROM card_srs c
            JOIN card card ON c.card_id = card.id
            WHERE card.deck_name = :deckName
              AND c.conjugation_type = :conjugationType
              AND c.srs_json->>'state' = 'REVIEW'
              AND (c.srs_json->>'due')::timestamp <= CURRENT_TIMESTAMP
            """, nativeQuery = true)
    int countReviewCards(@Param("deckName") String deckName,
                          @Param("conjugationType") String conjugationType);
}