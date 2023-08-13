package me.rootdeibis.orewards.api.commands.context;

import me.rootdeibis.orewards.api.commands.CommandLoader;
import me.rootdeibis.orewards.api.commands.annotations.CoreCommand;
import me.rootdeibis.orewards.api.commands.annotations.CoreSubCommands;
import me.rootdeibis.orewards.api.commands.annotations.TabCompletion;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class CommandContext {

    private final CommandLoader loader;
    private final Method commandMethod;

    private CoreCommand command;

    private final List<SubCommandContext> subCommandContexts = new ArrayList<>();

    private TabCompletionContext tabCompletionContext;
    public CommandContext(CommandLoader loader, Method commandMethod) {
        this.loader = loader;
        this.commandMethod = commandMethod;

        this.load();
    }

    private void load() {
        this.command = this.commandMethod.getAnnotation(CoreCommand.class);
        CoreSubCommands subCommands = this.commandMethod.getAnnotation(CoreSubCommands.class);

        this.tabCompletionContext = new TabCompletionContext(Arrays.stream(this.loader.getTabCompletionMethods()).filter(t -> {
            TabCompletion tabCompletion = t.getAnnotation(TabCompletion.class);
            return tabCompletion.command().equalsIgnoreCase(this.command.name());
        }).toArray(Method[]::new), this);

        if(subCommands != null) {
            for (Class<?> aClass : subCommands.list()) {
                for (Method method : Arrays.stream(aClass.getMethods()).filter(m -> m.isAnnotationPresent(CoreCommand.class)).collect(Collectors.toList())) {
                    this.subCommandContexts.add(new SubCommandContext(this, method, aClass));
                }
            }
        }


    }

    public CoreCommand getCommand() {
        return command;
    }

    public Method getCommandMethod() {
        return commandMethod;
    }

    public CommandLoader getLoader() {
        return loader;
    }


    public List<SubCommandContext> getSubCommands() {
        return subCommandContexts;
    }

    public TabCompletionContext getTabCompletionContext() {
        return tabCompletionContext;
    }
}
