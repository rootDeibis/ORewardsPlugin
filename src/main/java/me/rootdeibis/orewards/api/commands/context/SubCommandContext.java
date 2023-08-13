package me.rootdeibis.orewards.api.commands.context;

import me.rootdeibis.orewards.api.commands.annotations.CoreCommand;

import java.lang.reflect.Method;

public class SubCommandContext {


    private final CommandContext commandContext;
    private final CoreCommand subcommand;

    private final Method subCommandMethod;

    private final Class<?> subCommandClass;

    private final Object inializedClass;

    public SubCommandContext(CommandContext commandContext, Method subCommandMethod, Class<?> subCommandClass) {
        this.commandContext = commandContext;
        this.subcommand = subCommandMethod.getAnnotation(CoreCommand.class);
        this.subCommandMethod = subCommandMethod;
        this.subCommandClass = subCommandClass;
        try {
            this.inializedClass = subCommandClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public CoreCommand getSubCommand() {
        return subcommand;
    }

    public Method getSubCommandMethod() {
        return subCommandMethod;
    }

    public Object getInializedClass() {
        return inializedClass;
    }

    public Class<?> getSubCommandClass() {
        return subCommandClass;
    }
}
