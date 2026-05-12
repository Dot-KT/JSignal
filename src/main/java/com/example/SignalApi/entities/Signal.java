package com.example.SignalApi.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "signals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Signal {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String status;

    @Column(columnDefinition = "TEXT")
    private String text;

    @Column(columnDefinition = "TEXT")
    private String rawText;

    private Double latitude;

    private Double longitude;

    private String reporterPhone;

    private String reference;

    private String address;

    private Instant occurredAt;

    private Boolean hasCctv;

    private String policeCaseId;

    @Column(nullable = false)
    private String communityId;

    @ElementCollection
    @CollectionTable(name = "signal_images", joinColumns = @JoinColumn(name = "signal_id"))
    @Column(name = "image_url")
    private List<String> images = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "signal_id")
    private List<Suspect> suspects = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "signal_id")
    private List<Vehicle> vehicles = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "signal_tags", joinColumns = @JoinColumn(name = "signal_id"))
    @Column(name = "tag")
    private List<String> tags = new ArrayList<>();

    @Embedded
    private Metadata metadata;

    private String parentSignalId;

    @Column(nullable = false)
    private Boolean isPrimary = false;

    @Column(nullable = false)
    private Boolean flowCompleted = false;

    @Column(nullable = false)
    private Boolean isUnderReview = false;

    @Column(nullable = false)
    private Boolean isLinkedToIncident = false;

    @Column(nullable = false)
    private Boolean isActive = true;

    private Integer priorityScore;

    private String decisionOutcome;

    private Instant decidedAt;

    private String decidedBy;

    private String claimedBy;

    private Instant claimedAt;

    private String primaryBoloId;

    private String reporterId;

    private String externalMessageId;

    private Instant submissionTime;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    private Instant deactivatedAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }
}
