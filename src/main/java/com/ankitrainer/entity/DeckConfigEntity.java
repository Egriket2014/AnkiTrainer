package com.ankitrainer.entity;

import com.ankitrainer.language.enums.Language;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "deck_config")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeckConfigEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "deck_name", nullable = false, unique = true)
    private String deckName;

    @Column(name = "model_name", nullable = false)
    private String modelName;

    @Column(name = "language", nullable = false)
    private Language language;

    @Column(name = "word_field", nullable = false)
    private String wordField;

    @Column(name = "translation_field", nullable = false)
    private String translationField;

    @Column(name = "extra_field")
    private String extraField;

    @Column(name = "review_limit", nullable = false)
    private Integer reviewLimit;

    @Column(name = "new_limit", nullable = false)
    private Integer newLimit;

    @CreationTimestamp
    @Column(name = "last_synced_at")
    private LocalDateTime lastSyncedAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}