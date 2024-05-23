package pl.mrjozvvicki.nexusguilds.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Chat {

    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static void sendMessage(Player p, String message) {
        p.sendMessage(colorize(message));
    }
}