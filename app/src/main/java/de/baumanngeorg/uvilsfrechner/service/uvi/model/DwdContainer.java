package de.baumanngeorg.uvilsfrechner.service.uvi.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;

public class DwdContainer {

    private DwdUviModel model = getBaseModel();

    public DwdContainer(DwdUviModel model) {
        this.model = model;
    }

    public DwdContainer() {
    }

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

    public DwdUviModel getModel() {
        return this.model;
    }

    public void setModel(DwdUviModel model) {
        this.model = model;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof DwdContainer))
            return false;
        final DwdContainer other = (DwdContainer) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$model = this.getModel();
        final Object other$model = other.getModel();
        return this$model == null ? other$model == null : this$model.equals(other$model);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof DwdContainer;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $model = this.getModel();
        result = result * PRIME + ($model == null ? 43 : $model.hashCode());
        return result;
    }

    public String toString() {
        return "DwdContainer(model=" + this.getModel() + ")";
    }
}
