package me.rootdeibis.orewards.api.commands;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;

public class MethodUtils {

    public static <T> T invokeMethod(Object instance, Method method, Object... values) {
        LinkedList<Object> parameters = new LinkedList<>();

        for (Class<?> parameterType : method.getParameterTypes()) {
          Object parameter = Arrays.stream(values).filter(parameterType::isInstance).findFirst().orElse(null);

          parameters.add(parameter);

        }

        try {
            return (T) method.invoke(instance, parameters.toArray());
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }


    }
}
