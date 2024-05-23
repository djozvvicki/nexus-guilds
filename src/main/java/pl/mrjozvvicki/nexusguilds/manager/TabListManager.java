package pl.mrjozvvicki.nexusguilds.manager;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pl.mrjozvvicki.nexusguilds.entities.Guild;
import pl.mrjozvvicki.nexusguilds.utils.Chat;

import java.util.List;

public class TabListManager {
    public static TabListManager instance;
    private final GuildsManager guildsManager;

    public TabListManager() {
        guildsManager = GuildsManager.getInstance();
    }

    public static TabListManager getInstance() {
        if (instance == null) {
            instance = new TabListManager();
        }

        return instance;
    }

    public void updateTabList() {
        List<Guild> topGuilds = guildsManager.getTopGuilds();
        String header = "&7[&NexusGuilds&7] TOP10 Gildii\n";
        String footer = "&7-------------------------";
        if (!topGuilds.isEmpty()) {
            TextComponent headerComponent = Component.text(Chat.colorize(header));
            TextComponent footerComponent = Component.text(Chat.colorize(footer));

            for (Player player : Bukkit.getOnlinePlayers()) {
                int place = 1;
                for (Guild guild : topGuilds) {
                    headerComponent = headerComponent.append(Component.text(Chat.colorize("&7" + place + ". [&4" + guild.getName() + "&7] Pkt: &4" + guild.getPoints() + "&7\n")));
                    place++;
                }

                player.sendPlayerListHeaderAndFooter(headerComponent, footerComponent);
            }
        }
    }
}
