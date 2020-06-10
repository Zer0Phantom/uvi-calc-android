package de.baumanngeorg.uvilsfrechner.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UviCalculationService {

    int uvi;
    int zeit;
    int med;
    int lsf;
    int minAlreadySpend;

    public String getWhatLsf() {
        // Calculation Zeit/((MED/(2*UVI) - minAlreadySpend)
        if (uvi == 0) {
            return "Kein LSF notwendig.";
        }

        double lsfCalculated = (zeit / (med / (2D * uvi) - minAlreadySpend)) * 2;
        if (lsfCalculated < 0 || lsfCalculated > 55) {
            return "Es gibt leider keinen groß genügenden LSF.";
        }

        return "LSF " + Math.round(lsfCalculated);
    }

    public String getHowLongOutside() {
        // Calculation: (MED/(2*UVI) - minALreadySpend)*LSF
        if (lsf == 0) {
            lsf = 2;
        }
        if (uvi == 0) {
            return "Keine Beschränkung vorhanden.";
        }

        double restOutside = ((med / (2D * uvi)) - minAlreadySpend) * (lsf / 2D);
        int restHoursOutside = (int) restOutside / 60;
        int restMinutesOutside = (int) restOutside % 60;

        return (restHoursOutside > 0 ? (restHoursOutside + "h") : "") + (restMinutesOutside > 0 ? (" " + restMinutesOutside + "min") : " 0min");
    }
}
