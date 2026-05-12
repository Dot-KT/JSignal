package com.example.SignalApi.mapper;

import com.example.SignalApi.dto.*;
import com.example.SignalApi.entities.*;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Component
public class SignalMapper {

    public Signal toEntity(SignalRequestDto dto) {
        Signal signal = new Signal();
        signal.setType(dto.getType());
        signal.setStatus(dto.getStatus());
        signal.setText(dto.getText());
        signal.setRawText(dto.getRawText());
        signal.setLatitude(dto.getLatitude());
        signal.setLongitude(dto.getLongitude());
        signal.setReporterPhone(dto.getReporterPhone());
        signal.setReference(dto.getReference());
        signal.setAddress(dto.getAddress());
        signal.setOccurredAt(parseInstant(dto.getOccurredAt()));
        signal.setHasCctv(dto.getHasCctv());
        signal.setPoliceCaseId(dto.getPoliceCaseId());
        signal.setCommunityId(dto.getCommunityId());
        signal.setImages(dto.getImages() != null ? dto.getImages() : new ArrayList<>());
        signal.setTags(dto.getTags() != null ? dto.getTags() : new ArrayList<>());
        signal.setParentSignalId(dto.getParentSignalId());
        signal.setIsPrimary(dto.getIsPrimary());
        signal.setFlowCompleted(dto.getFlowCompleted());
        signal.setIsUnderReview(dto.getIsUnderReview());
        signal.setIsLinkedToIncident(dto.getIsLinkedToIncident());
        signal.setIsActive(dto.getIsActive());
        signal.setPriorityScore(dto.getPriorityScore());
        signal.setDecisionOutcome(dto.getDecisionOutcome());
        signal.setDecidedBy(dto.getDecidedBy());
        signal.setClaimedBy(dto.getClaimedBy());
        signal.setPrimaryBoloId(dto.getPrimaryBoloId());
        signal.setReporterId(dto.getReporterId());
        signal.setExternalMessageId(dto.getExternalMessageId());
        signal.setSubmissionTime(parseInstant(dto.getSubmissionTime()));

        if (dto.getSuspects() != null) {
            signal.setSuspects(dto.getSuspects().stream().map(this::toEntity).toList());
        }
        if (dto.getVehicles() != null) {
            signal.setVehicles(dto.getVehicles().stream().map(this::toEntity).toList());
        }
        if (dto.getMetadata() != null) {
            signal.setMetadata(toEntity(dto.getMetadata()));
        }

        return signal;
    }

    public SignalResponseDto toResponseDto(Signal signal) {
        SignalResponseDto dto = new SignalResponseDto();
        dto.setId(signal.getId());
        dto.setType(signal.getType());
        dto.setStatus(signal.getStatus());
        dto.setText(signal.getText());
        dto.setRawText(signal.getRawText());
        dto.setLatitude(signal.getLatitude());
        dto.setLongitude(signal.getLongitude());
        dto.setReporterPhone(signal.getReporterPhone());
        dto.setReference(signal.getReference());
        dto.setAddress(signal.getAddress());
        dto.setOccurredAt(signal.getOccurredAt());
        dto.setHasCctv(signal.getHasCctv());
        dto.setPoliceCaseId(signal.getPoliceCaseId());
        dto.setCommunityId(signal.getCommunityId());
        dto.setImages(signal.getImages());
        dto.setTags(signal.getTags());
        dto.setParentSignalId(signal.getParentSignalId());
        dto.setIsPrimary(signal.getIsPrimary());
        dto.setFlowCompleted(signal.getFlowCompleted());
        dto.setIsUnderReview(signal.getIsUnderReview());
        dto.setIsLinkedToIncident(signal.getIsLinkedToIncident());
        dto.setIsActive(signal.getIsActive());
        dto.setPriorityScore(signal.getPriorityScore());
        dto.setDecisionOutcome(signal.getDecisionOutcome());
        dto.setDecidedAt(signal.getDecidedAt());
        dto.setDecidedBy(signal.getDecidedBy());
        dto.setClaimedBy(signal.getClaimedBy());
        dto.setClaimedAt(signal.getClaimedAt());
        dto.setPrimaryBoloId(signal.getPrimaryBoloId());
        dto.setReporterId(signal.getReporterId());
        dto.setExternalMessageId(signal.getExternalMessageId());
        dto.setSubmissionTime(signal.getSubmissionTime());
        dto.setCreatedAt(signal.getCreatedAt());
        dto.setDeactivatedAt(signal.getDeactivatedAt());

        if (signal.getSuspects() != null) {
            dto.setSuspects(signal.getSuspects().stream().map(this::toDto).toList());
        }
        if (signal.getVehicles() != null) {
            dto.setVehicles(signal.getVehicles().stream().map(this::toDto).toList());
        }
        if (signal.getMetadata() != null) {
            dto.setMetadata(toDto(signal.getMetadata()));
        }

        return dto;
    }

    private Suspect toEntity(SuspectDto dto) {
        Suspect suspect = new Suspect();
        suspect.setName(dto.getName());
        suspect.setDescription(dto.getDescription());
        suspect.setPhoneNumber(dto.getPhoneNumber());
        suspect.setImageUrl(dto.getImageUrl());
        return suspect;
    }

    private Vehicle toEntity(VehicleDto dto) {
        Vehicle vehicle = new Vehicle();
        vehicle.setType(dto.getType());
        vehicle.setBrand(dto.getBrand());
        vehicle.setModel(dto.getModel());
        vehicle.setColor(dto.getColor());
        vehicle.setLicense(dto.getLicense());
        vehicle.setDirection(dto.getDirection());
        vehicle.setMovement(dto.getMovement());
        vehicle.setExtra(dto.getExtra());
        return vehicle;
    }

    private Metadata toEntity(MetadataDto dto) {
        Metadata metadata = new Metadata();
        metadata.setReportPath(dto.getReportPath());
        metadata.setReportAction(dto.getReportAction());
        metadata.setReportPathLabel(dto.getReportPathLabel());
        return metadata;
    }

    private SuspectDto toDto(Suspect suspect) {
        return new SuspectDto(
                suspect.getName(),
                suspect.getDescription(),
                suspect.getPhoneNumber(),
                suspect.getImageUrl()
        );
    }

    private VehicleDto toDto(Vehicle vehicle) {
        return new VehicleDto(
                vehicle.getType(),
                vehicle.getBrand(),
                vehicle.getModel(),
                vehicle.getColor(),
                vehicle.getLicense(),
                vehicle.getDirection(),
                vehicle.getMovement(),
                vehicle.getExtra()
        );
    }

    private MetadataDto toDto(Metadata metadata) {
        return new MetadataDto(
                metadata.getReportPath(),
                metadata.getReportAction(),
                metadata.getReportPathLabel()
        );
    }

    private Instant parseInstant(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return Instant.parse(value);
    }
}
