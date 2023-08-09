package me.rootdeibis.orewards.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DurationParser {
    public static long parseDuration(String duration) {
        long result = 0;
        Pattern pattern = Pattern.compile("(\\d+)([smhdwmo])");
        Matcher matcher = pattern.matcher(duration);
        while (matcher.find()) {
            int value = Integer.parseInt(matcher.group(1));
            String unit = matcher.group(2);
            switch (unit) {
                case "s":
                    result += value * 1000L;
                    break;
                case "m":
                    result += value * 60 * 1000L;
                    break;
                case "h":
                    result += value * 60 * 60 * 1000L;
                    break;
                case "d":
                    result += value * 24 * 60 * 60 * 1000L;
                    break;
                case "w":
                    result += value * 7 * 24 * 60 * 60 * 1000L;
                    break;
                case "mo":
                    result += value * 30 * 24 * 60 * 60 * 1000L;
                    break;
            }
        }
        return result;
    }

    public static String formatDuration(long duration) {
        StringBuilder result = new StringBuilder();
        long months = duration / (30 * 24 * 60 * 60 * 1000L);
        if (months > 0) {
            result.append(months).append("mo ");
            duration -= months * 30 * 24 * 60 * 60 * 1000L;
        }
        long weeks = duration / (7 * 24 * 60 * 60 * 1000);
        if (weeks > 0) {
            result.append(weeks).append("w ");
            duration -= weeks * 7 * 24 * 60 * 60 * 1000;
        }
        long days = duration / (24 * 60 * 60 * 1000);
        if (days > 0) {
            result.append(days).append("d ");
            duration -= days * 24 * 60 * 60 * 1000;
        }
        long hours = duration / (60 * 60 * 1000);
        if (hours > 0) {
            result.append(hours).append("h ");
            duration -= hours * 60 * 60 * 1000;
        }
        long minutes = duration / (60 * 1000);
        if (minutes > 0) {
            result.append(minutes).append("m ");
            duration -= minutes * 60 * 1000;
        }
        long seconds = duration / (1000);
        if (seconds > 0) {
            result.append(seconds).append("s ");
            duration -= seconds * 1000;
        }
        return result.toString().trim();
    }

}