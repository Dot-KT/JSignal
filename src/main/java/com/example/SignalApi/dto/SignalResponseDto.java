package com.example.SignalApi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignalResponseDto {

    private String id;
    private String type;
    private String status;
    private String text;
    private String rawText;
    private Double latitude;
    private Double longitude;
    private String reporterPhone;
    private String reference;
    private String address;
    private Instant occurredAt;
    private Boolean hasCctv;
    private String policeCaseId;
    private String communityId;
    private List<String> images;
    private List<SuspectDto> suspects;
    private List<VehicleDto> vehicles;
    private List<String> tags;
    private MetadataDto metadata;
    private String parentSignalId;
    private Boolean isPrimary;
    private Boolean flowCompleted;
    private Boolean isUnderReview;
    private Boolean isLinkedToIncident;
    private Boolean isActive;
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
    private Instant createdAt;
    private Instant deactivatedAt;
}
