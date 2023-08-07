package me.rootdeibis.orewards.api.commands.context;




import me.rootdeibis.orewards.api.commands.MethodUtils;
import me.rootdeibis.orewards.api.commands.annotations.TabCompletion;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class TabCompletionContext {

    private final Method mainCompletion;
    private final Method[] subCommandCompletions;

    private final CommandContext commandContext;


    public TabCompletionContext(Method[] completionMethods, CommandContext commandContext) {
        this.mainCompletion = Arrays.stream(completionMethods).filter(m -> ((TabCompletion) m.getAnnotation(TabCompletion.class)).target() == TabCompletion.TargetType.MAIN ).findFirst().orElse(null);
        this.subCommandCompletions = Arrays.stream(completionMethods).filter(m -> ((TabCompletion) m.getAnnotation(TabCompletion.class)).target() == TabCompletion.TargetType.SUBCOMMAND ).toArray(Method[]::new);
        this.commandContext = commandContext;

    }


    public List<String> getMainCompletions(Object... arguments) {
        return MethodUtils.invokeMethod(this.commandContext.getLoader().getInitializedClass(),  this.mainCompletion,arguments);
    }

    public List<String> getSubCommandCompletions(String subCommand, Object... arguments) {
        List<String> list = new ArrayList<>();
        Predicate<Method> predicateMethod = (m -> {
            TabCompletion command = m.getAnnotation(TabCompletion.class);
            return command.command().equalsIgnoreCase(this.commandContext.getCommand().name()) &&
            this.commandContext.getSubCommands().stream().anyMatch(s -> Arrays.stream(s.getSubCommand().aliases()).anyMatch(d -> d.equalsIgnoreCase(subCommand))) ||
                    command.subcommand().equalsIgnoreCase(subCommand);
        });

        if(Arrays.stream(this.subCommandCompletions).anyMatch(predicateMethod)) {
            Method completionMethod = Arrays.stream(this.subCommandCompletions).filter(predicateMethod).findAny().get();

                return MethodUtils.invokeMethod(this.commandContext.getLoader().getInitializedClass(), completionMethod,arguments);

        }

        return list;

    }
}
