package com.example.SignalApi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SuspectDto {

    private String name;
    private String description;
    private String phoneNumber;
    private String imageUrl;
}
