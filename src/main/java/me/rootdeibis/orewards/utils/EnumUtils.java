package me.rootdeibis.orewards.utils;

public class EnumUtils {

    public static <T extends Enum<?>> T searchEnum(Class<T> enumeration,
                                                   String search) {
        for (T each : enumeration.getEnumConstants()) {
            if (each.name().compareToIgnoreCase(search) == 0) {
                return each;
            }
        }
        return null;
    }

    public static <T extends Enum<?>> T mathEnum(Class<T> enumeration, String query) {
        for (T each : enumeration.getEnumConstants()) {
            if (each.name().startsWith(query)) {
                return each;
            }
        }

        return null;
    }
}
