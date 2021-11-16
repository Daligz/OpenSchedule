package me.upp.dali.openschedule.model.database.utils;

import lombok.experimental.UtilityClass;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@UtilityClass
public class DataTime {

    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public Timestamp getTimeNow() {
        return new Timestamp(System.currentTimeMillis());
    }

    public int timeToMilliseconds(final int hours, final int minutes) {
        System.out.println((hours * 3600000) + (minutes * 60000));
        return (hours * 3600000) + (minutes * 60000);
    }
}
