package me.rootdeibis.orewards.utils;

public class TimeVerifier {
    public static final long SECOND = 20;
    public static final long MINUTE = SECOND * 60;
    public static final long HOUR = MINUTE * 60;
    public static final long DAY = HOUR * 24;
    public static final long MONTH = DAY * 30;

    public static String formatTime(long ticks) {
        StringBuilder sb = new StringBuilder();
        if (ticks >= MONTH) {
            sb.append(ticks / MONTH).append("mo ");
            ticks %= MONTH;
        }
        if (ticks >= DAY) {
            sb.append(ticks / DAY).append("d ");
            ticks %= DAY;
        }
        if (ticks >= HOUR) {
            sb.append(ticks / HOUR).append("h ");
            ticks %= HOUR;
        }
        if (ticks >= MINUTE) {
            sb.append(ticks / MINUTE).append("m ");
            ticks %= MINUTE;
        }
        if (ticks >= SECOND) {
            sb.append(ticks / SECOND).append("s");
        }
        return sb.toString();
    }

    public static boolean verifyTime(long ticks, String requiredTime) {
        String[] parts = requiredTime.split(" ");
        for (String part : parts) {
            char unit = part.charAt(part.length() - 1);
            int value = Integer.parseInt(part.substring(0, part.length() - 1));
            switch (unit) {
                case 's':
                    ticks -= value * SECOND;
                    break;
                case 'm':
                    ticks -= value * MINUTE;
                    break;
                case 'h':
                    ticks -= value * HOUR;
                    break;
                case 'd':
                    ticks -= value * DAY;
                    break;
                case 'o':
                    ticks -= value * MONTH;
                    break;
            }
        }
        return ticks >= 0;
    }
}
