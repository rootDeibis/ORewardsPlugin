package me.rootdeibis.orewards.api.commands.executors;

import me.rootdeibis.orewards.api.commands.MethodUtils;
import me.rootdeibis.orewards.api.commands.context.CommandContext;
import me.rootdeibis.orewards.api.commands.context.SubCommandContext;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CoreCommandExecutor extends BukkitCommand {

    private final CommandContext commandContext;
    public CoreCommandExecutor(CommandContext commandContext) {
        super(commandContext.getCommand().name());

        this.setAliases(Arrays.stream(commandContext.getCommand().aliases()).collect(Collectors.toList()));
        this.setPermission(commandContext.getCommand().permission());

        this.commandContext = commandContext;


    }

    @FunctionalInterface
    private  interface Function<In1, Out> {
        Out apply(In1 in1);
    }


    @Override
    public boolean execute(CommandSender commandSender, String s, String[] args) {

        Function<String, Boolean> needPermission = (p) -> p != null && p.length() > 0 && !commandSender.hasPermission(p);

        if(needPermission.apply(this.commandContext.getCommand().permission())) {
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getPermissionMessage()));
        } else {
            if(this.commandContext.getSubCommands().size() == 0 || args.length == 0) {

                return MethodUtils.invokeMethod(this.commandContext.getLoader().getInitializedClass(),
                        this.commandContext.getCommandMethod(), commandSender, s, args);
            } else {


                SubCommandContext subCommandContext = this.commandContext.getSubCommands().stream().filter(d -> d.getSubCommand().name().equalsIgnoreCase(args[0]) || Arrays.stream(d.getSubCommand().aliases()).anyMatch(ds -> ds.equalsIgnoreCase(args[0]))).findFirst().orElse(null);


               if(subCommandContext != null) {
                   if(needPermission.apply(subCommandContext.getSubCommand().permission())) {
                       commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getPermissionMessage()));
                   } else {


                       return MethodUtils.invokeMethod(subCommandContext.getInializedClass(),
                               subCommandContext.getSubCommandMethod(), commandSender, s, Arrays.stream(args, 1, args.length).toArray(String[]::new));

                   }
               }
            }
        }
        return false;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {

        List<String> completions = this.commandContext.getTabCompletionContext().getMainCompletions();
        if(args.length >= 2) {
            completions = this.commandContext.getTabCompletionContext().getSubCommandCompletions(args[0],sender,alias, args);

            if(args[1] != null && args[1].length() > 0) {
                completions = completions.stream().filter(s -> s.toLowerCase().startsWith(args[1].toLowerCase())).collect(Collectors.toList());
            }
        } else if(args[0] != null && args[0].length() > 0) {
           completions = completions.stream().filter(s -> s.toLowerCase().startsWith(args[0].toLowerCase())).collect(Collectors.toList());
        }

        return completions;

    }
}
