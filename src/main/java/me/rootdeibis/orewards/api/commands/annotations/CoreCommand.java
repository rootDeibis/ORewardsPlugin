package me.rootdeibis.orewards.api.commands.annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CoreCommand {

    String name();

    String permission() default "";

    String[] aliases() default {};

}
