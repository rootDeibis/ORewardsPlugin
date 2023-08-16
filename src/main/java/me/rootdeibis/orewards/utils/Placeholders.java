package me.rootdeibis.orewards.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class Placeholders {

    private final HashMap<String, Callable<Object>> placeholders = new HashMap<>();
    private final HashMap<String, String> cache = new HashMap<>();

    public Placeholders() {

    }


    public void add(String name, Object value) {
        this.placeholders.put(name, () -> value);
    }

    public void add(String name, Callable<Object> value) {
        this.placeholders.put(name, value);
    }

    public void remove(String name) {
        this.placeholders.remove(name);
    }

    public String apply(String target) {
        String targetResult = target;

        for (String key : this.placeholders.keySet()) {
            try {
                targetResult = targetResult.replaceAll("<" + key + ">", String.valueOf(placeholders.get(key).call()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return targetResult;

    }

    public List<String> apply(List<String> target) {
        return Arrays.stream(this.apply(String.join("__.", target)
        ).split("__.")).collect(Collectors.toList());
    }




}