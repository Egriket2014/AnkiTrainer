package com.ankitrainer.entity;

import com.ankitrainer.language.enums.PartOfSpeech;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "card")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "note_id", unique = true, nullable = false)
    private Long noteId;

    @Column(name = "word" ,nullable = false)
    private String word;

    @Column(name = "translation", nullable = false)
    private String translation;

    @Column(name = "extra", nullable = true)
    private String extra;

    @Column(name = "deck_name")
    private String deckName;

    @Column(name = "part_of_speech")
    private PartOfSpeech partOfSpeech;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}