package me.rootdeibis.orewards.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;

public class DurationParser {
    public static int[] formatDateValues(String format) {
        String[] types = format.split(" ");

        int[] values = {0,0,0,0,0};

        for (String type : types) {

            if (type.endsWith("mo")) {
                values[0] = Integer.parseInt(type.replaceAll("mo", ""));
            }

            if (type.endsWith("d")) {
                values[1] = Integer.parseInt(type.replaceAll("d", ""));
            }

            if (type.endsWith("h")) {
                values[2] = Integer.parseInt(type.replaceAll("h", ""));
            }

            if (type.endsWith("m")) {
                values[3] = Integer.parseInt(type.replaceAll("m", ""));
            }

            if (type.endsWith("s")) {
                values[4] = Integer.parseInt(type.replaceAll("s", ""));
            }

        }
        return values;
    }

    public static Date addToDate(String format) {
        return addToDate(formatDateValues(format));
    }

    public static Date addDateToDate(Date date, String format) {
        int[] values = formatDateValues(format);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        if (values[0] != 0) calendar.add(Calendar.MONTH, values[0]);
        if (values[1] != 0) calendar.add(Calendar.DAY_OF_MONTH, values[1]);
        if (values[2] != 0) calendar.add(Calendar.HOUR, values[2]);
        if (values[3] != 0) calendar.add(Calendar.MINUTE, values[3]);
        if (values[4] != 0) calendar.add(Calendar.SECOND, values[4]);

        return calendar.getTime();
    }


    public static Date addToDate(int... values) {
        Calendar calendar = Calendar.getInstance();

        if (values[0] != 0) calendar.add(Calendar.MONTH, values[0]);
        if (values[1] != 0) calendar.add(Calendar.DAY_OF_MONTH, values[1]);
        if (values[2] != 0) calendar.add(Calendar.HOUR, values[2]);
        if (values[3] != 0) calendar.add(Calendar.MINUTE, values[3]);
        if (values[4] != 0) calendar.add(Calendar.SECOND, values[4]);

        return calendar.getTime();
    }

    public static String formatDate(int... values) {

        StringBuilder str = new StringBuilder();

        if(values.length >= 1 && values[0] != 0) str.append(values[0] + "mo ");
        if(values.length >= 2 && values[1] != 0) str.append(values[1] + "d ");
        if(values.length >= 3 && values[2] != 0) str.append(values[2] + "h ");
        if(values.length >= 4 && values[3] != 0) str.append(values[3] + "m ");
        if(values.length >= 5 && values[4] != 0) str.append(values[4] + "s ");

        return str.toString();

    }

    public static String format(long d) {
        String str = "%d %h %m %s";
        Date date = new Date(d);
        long diff = (new Date()).getTime() - date.getTime();
        long diffSeconds = Math.abs(diff / 1000L % 60L);
        long diffMinutes = Math.abs(diff / 60000L % 60L);
        long diffHours = Math.abs(diff / 3600000L % 24L);
        long diffDays = Math.abs(diff / 86400000L);
        str = str.replaceAll("%d", diffDays != 0L ? String.valueOf(diffDays) + "d" : "").replaceAll("%h", diffHours != 0L ? String.valueOf(diffHours) + "h" : "").replaceAll("%m", diffMinutes != 0L ? String.valueOf(diffMinutes) + "m" : "").replaceAll("%s", diffSeconds != 0L ? String.valueOf(diffSeconds) + "s" : "");
        str = str.replaceAll("  ", "");
        return str.startsWith(" ") ? str.substring(1) : str;
    }

    public static long nextDayOfweek(String day) {
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDate localDate = LocalDate.of(localDateTime.getYear(), localDateTime.getMonthValue(), localDateTime.getDayOfMonth());
        localDate = localDate.with(TemporalAdjusters.next(DayOfWeek.valueOf(day)));

        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        return date.getTime();
    }

}