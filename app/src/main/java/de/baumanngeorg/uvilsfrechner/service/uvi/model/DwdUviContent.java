package de.baumanngeorg.uvilsfrechner.service.uvi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DwdUviContent {
    private String city;

    private DwdUviForecast forecast;
}
