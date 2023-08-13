package me.rootdeibis.orewards.api.commands;


import me.rootdeibis.orewards.api.commands.annotations.CoreCommand;
import me.rootdeibis.orewards.api.commands.annotations.TabCompletion;
import me.rootdeibis.orewards.api.commands.context.CommandContext;
import me.rootdeibis.orewards.api.commands.executors.CoreCommandExecutor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public class CommandLoader {
    private final Class<?> commandloaderClass;
    private final Object initializedClass;



    public CommandLoader(Class<?> commandLoaderClass) {
        this.commandloaderClass = commandLoaderClass;
        try {
            this.initializedClass = commandLoaderClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public Method[] getCommandMethods() {
        return Arrays.stream(this.commandloaderClass.getMethods()).filter(m -> m.isAnnotationPresent(CoreCommand.class)).toArray(Method[]::new);
    }

    public Method[] getTabCompletionMethods() {
        return Arrays.stream(this.commandloaderClass.getMethods()).filter(m -> m.isAnnotationPresent(TabCompletion.class)).toArray(Method[]::new);
    }

    public Class<?> getCommandLoaderClass() {
        return commandloaderClass;
    }

    public void register() {
        for (Method commandMethod : this.getCommandMethods()) {
            CommandContext commandContext = new CommandContext(this, commandMethod);

            this.getCommandMap().register(commandContext.getCommand().name(), new CoreCommandExecutor(commandContext));

        }
    }

    public static void register(Class<?>... commandClass) {
        for (Class<?> aClass : commandClass) {
            new CommandLoader(aClass).register();
        }
    }

    public Object getInitializedClass() {
        return initializedClass;
    }

    private CommandMap getCommandMap() {
        CommandMap commandMap = null;

        try {
            Field f = Bukkit.getPluginManager().getClass().getDeclaredField("commandMap");
            f.setAccessible(true);

            commandMap = (CommandMap) f.get(Bukkit.getPluginManager());
        } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException | SecurityException e) {
            e.printStackTrace();
        }

        return commandMap;
    }

}
