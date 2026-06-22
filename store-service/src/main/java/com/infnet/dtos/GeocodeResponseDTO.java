package com.infnet.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeocodeResponseDTO {
    private Double latitude;
    private Double longitude;
}