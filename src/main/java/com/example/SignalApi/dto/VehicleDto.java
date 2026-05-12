package com.example.SignalApi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDto {

    private String type;
    private String brand;
    private String model;
    private String color;
    private String license;
    private String direction;
    private String movement;
    private String extra;
}
