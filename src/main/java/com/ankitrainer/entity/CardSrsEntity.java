package com.ankitrainer.entity;

import com.ankitrainer.language.enums.ConjugationType;
import io.github.openspacedrepetition.Card;
import io.github.openspacedrepetition.State;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Comparator;

@Entity
@Table(
        name = "card_srs",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"card_id", "conjugation_type"})
        }
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardSrsEntity implements Comparable<CardSrsEntity> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "card_id", nullable = false)
    private CardEntity card;

    @Column(name = "conjugation_type", nullable = false)
    private ConjugationType conjugationType;

    @Column(name = "answer", nullable = false)
    private String answer;

    @Column(name = "srs_json", columnDefinition = "JSONB", nullable = false)
    @ColumnTransformer(write = "?::jsonb")
    private String srsJson;

    @Transient
    private Card srsCard;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PostLoad
    private void onLoad() {
        if (srsJson != null && !srsJson.isEmpty()) {
            this.srsCard = Card.fromJson(srsJson);
        }
    }

    @PrePersist
    @PreUpdate
    private void onSave() {
        if (srsCard != null) {
            this.srsJson = srsCard.toJson();
        }
    }

    public State getState() {
        return srsCard != null ? srsCard.getState() : null;
    }

    public static final Comparator<CardSrsEntity> BY_DUE =
            Comparator.comparing(c -> c.getSrsCard().getDue());

    @Override
    public int compareTo(CardSrsEntity o) {
        return BY_DUE.compare(this, o);
    }
}