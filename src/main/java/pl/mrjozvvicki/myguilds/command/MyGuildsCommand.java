package pl.mrjozvvicki.myguilds.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.mrjozvvicki.myguilds.manager.CommandManager;
import pl.mrjozvvicki.myguilds.manager.GuildsManager;

import java.util.List;

public class MyGuildsCommand implements CommandExecutor, TabCompleter {
    GuildsManager guildsManager;
    Player p;
    CommandManager commandManager;

    public MyGuildsCommand(JavaPlugin plugin) {
        commandManager = CommandManager.getInstance(plugin);
        guildsManager = GuildsManager.getInstance();
    }

    private boolean isSenderInstanceOfPlayer(CommandSender sender) {
        if (sender instanceof Player) {
            return true;
        }

        System.out.println("This command is only for players!");
        return false;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (isSenderInstanceOfPlayer(sender)) {
            p = ((Player) sender).getPlayer();

            return commandManager.handleCommand(p, args);
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length == 2) {
            if (strings[0].equals("nexus")) {
                return commandManager.getNexusCommands(strings);
            }

            if (strings[0].equals("member")) {
                return commandManager.getMemberCommands(strings);
            }
        }

        if (strings.length <= 1) {
            return commandManager.getCommands(strings);
        }

        return null;
    }
}
