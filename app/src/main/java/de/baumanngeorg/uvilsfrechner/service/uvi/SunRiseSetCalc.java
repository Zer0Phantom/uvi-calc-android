package de.baumanngeorg.uvilsfrechner.service.uvi;

import java.util.Calendar;

public class SunRiseSetCalc {

    private SunRiseSetCalc() {
        // Empty to hide
        // public constructor
    }


    public static long getSunshineDuration() {
        Calendar[] dates = ca.rmen.sunrisesunset.SunriseSunset.getSunriseSunset(Calendar.getInstance(), 52.511869, 13.405181);
        return Math.round((((dates[1].getTimeInMillis() - dates[0].getTimeInMillis()) / 1000D) / 60D) - 60D);
    }
}
