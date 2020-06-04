package de.baumanngeorg.uvilsfrechner.service.uvi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DwdUviForecast {
    private int today;
    private int tomorrow;
    private int dayafter_to;
}
