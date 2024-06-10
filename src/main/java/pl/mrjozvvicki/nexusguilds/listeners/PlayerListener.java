package pl.mrjozvvicki.nexusguilds.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import pl.mrjozvvicki.nexusguilds.entities.Guild;
import pl.mrjozvvicki.nexusguilds.manager.GuildsManager;
import pl.mrjozvvicki.nexusguilds.manager.PlayerPointManager;
import pl.mrjozvvicki.nexusguilds.manager.PlayerRankManager;
import pl.mrjozvvicki.nexusguilds.manager.TabListManager;
import pl.mrjozvvicki.nexusguilds.utils.Chat;

public class PlayerListener implements Listener {
    private final GuildsManager guildsManager;
    private final PlayerPointManager playerPointManager;
    private final PlayerRankManager playerRankManager;
    private final TabListManager tabListManager;

    public PlayerListener() {
        playerPointManager = PlayerPointManager.getInstance();
        guildsManager = GuildsManager.getInstance();
        tabListManager = TabListManager.getInstance();
        playerRankManager = PlayerRankManager.getInstance();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        playerPointManager.initPlayerPoints(player);
        tabListManager.updateTabList();
    }

    public int countKillPointsByRank(Player killer, Player victim) {
        int victimRank = playerRankManager.getPlayerRank(victim);
        int killerRank = playerRankManager.getPlayerRank(killer);

        // Logic of damage
        if (victimRank < killerRank) {
            return 10;
        }
        if (victimRank > killerRank) {
            return 30;
        }

        return 20;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player killer = e.getEntity().getKiller();
        if (killer == null) return;
        Player victim = e.getPlayer();
        int killPoints = countKillPointsByRank(killer, victim);

        Guild victimGuild = guildsManager.findGuildByMember(victim);
        Guild killerGuild = guildsManager.findGuildByMember(killer);
        int victimPoints = playerPointManager.getPlayerPoints(victim);
        int killerPoints = playerPointManager.getPlayerPoints(killer);

        playerPointManager.changePlayerPoints(victim, Math.max(victimPoints - killPoints, 0));
        playerPointManager.changePlayerPoints(killer, killerPoints + killPoints);

        playerRankManager.updatePlayerRank(victim, victimPoints);
        playerRankManager.updatePlayerRank(killer, killerPoints);

        if (victimGuild != null)
            victimGuild.updatePoints();
        if (killerGuild != null)
            killerGuild.updatePoints();

        for (Player p : Bukkit.getOnlinePlayers()) {
            Chat.sendMessage(p, "&7Gracz &4" + killer.getName() + "&7[&6+20&7] zabił gracza &4" + victim.getName() + "&7[&4-20&7].");
        }

        tabListManager.updateTabList();
    }
}

