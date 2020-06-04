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
    private int today, tomorrow, dayafter_to;
}
