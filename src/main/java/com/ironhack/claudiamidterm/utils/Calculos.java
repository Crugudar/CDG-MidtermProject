package com.ironhack.claudiamidterm.utils;

import java.time.*;

public class Calculos {

    public static Integer calculateYears(LocalDate date){
        Period age = Period.between(date, LocalDate.now());
        return age.getYears();
    }

    public static Integer calculateDays(LocalDate lastUpdate){
        Period age = Period.between(lastUpdate, LocalDate.now());
        return age.getDays();
    }

    public static Integer calculateMonths(LocalDate lastUpdate){
        Period age = Period.between(lastUpdate, LocalDate.now());
        return age.getMonths();
    }
}
