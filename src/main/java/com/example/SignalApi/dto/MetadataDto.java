package com.example.SignalApi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MetadataDto {

    private String reportPath;
    private String reportAction;
    private String reportPathLabel;
}
