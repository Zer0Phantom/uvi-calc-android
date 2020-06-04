package de.baumanngeorg.uvilsfrechner.service.uvi.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DwdContainer {

    private DwdUviModel model = getBaseModel();

    private DwdUviModel getBaseModel() {
        return DwdUviModel.builder()
                .forecast_day("XTX")
                .last_update("2000-01-01T10:10:10")
                .next_update("2000-01-01T10:10:10")
                .sender("XTX")
                .name("XTX")
                .content(Collections.singletonList(new DwdUviContent("Berlin", new DwdUviForecast(8, 8, 8))))
                .build();
    }

    public String getUpdateString(String city) {
        DwdUviForecast forecast = getContentByCity(city).getForecast();
        return "Vorhersage für " +
                city +
                ":\n" +
                getForecastDay().format(DateTimeFormatter.ofPattern("dd.MM.' UV '")) +
                forecast.getToday() +
                getForecastDay().plusDays(1).format(DateTimeFormatter.ofPattern("'  |  'dd.MM.' UV '")) +
                forecast.getTomorrow() +
                getForecastDay().plusDays(2).format(DateTimeFormatter.ofPattern("'  |  'dd.MM.' UV '")) +
                forecast.getDayafter_to() +
                " \nDaten vom " +
                getLastUpdate().format(DateTimeFormatter.ofPattern("dd.MM.yy', 'hh:mm' Uhr'")) +
                "\nQuelle: " +
                model.getSender();
    }

    public DwdUviContent getContentByCity(String stadt) {
        return model.getContent().stream()
                .filter(city -> stadt.equals(city.getCity()))
                .findAny()
                .orElse(getBaseModel().getContent().get(0));
    }

    public LocalDateTime getNextUpdate() {
        return getDateFromDwdString(model.getNext_update());
    }

    private LocalDateTime getLastUpdate() {
        return getDateFromDwdString(model.getLast_update());
    }

    public LocalDate getForecastDay() {
        int[] forecastDay = stringArrayToInt(model.getForecast_day().split("-"));
        return LocalDate.of(forecastDay[0], forecastDay[1], forecastDay[2]);
    }

    private LocalDateTime getDateFromDwdString(String datesting) {
        String[] dateTime = datesting.split("T");
        int[] date = stringArrayToInt(dateTime[0].split("-"));
        int[] time = stringArrayToInt(dateTime[1].split(":"));
        return LocalDateTime.of(date[0], date[1], date[2], time[0], time[1], time[2]);
    }

    private int[] stringArrayToInt(String[] sarray) {
        return Arrays.stream(sarray).mapToInt(Integer::parseInt).toArray();
    }

}
