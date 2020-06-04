package de.baumanngeorg.uvilsfrechner.service.uvi.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DwdUviModel {
    private String last_update;
    private String next_update;
    private String name;
    private String sender;

    private List<DwdUviContent> content;

    private String forecast_day;
}

