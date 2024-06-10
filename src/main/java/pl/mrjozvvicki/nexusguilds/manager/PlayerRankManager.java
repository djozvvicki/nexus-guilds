package pl.mrjozvvicki.nexusguilds.manager;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PlayerRankManager {
    private static PlayerRankManager instance;
    private static final Map<String, Integer> ranks = new HashMap<>();

    public PlayerRankManager() {
        for (OfflinePlayer op :
                Bukkit.getOfflinePlayers()) {
            ranks.put(op.getName(), 0);
        }
    }

    public static PlayerRankManager getInstance() {
        if (instance != null) {
            instance = new PlayerRankManager();
        }

        return instance;
    }

    public Integer getPlayerRank(Player p) {
        String playerName = p.getName();

        for (Map.Entry<String, Integer> entry : ranks.entrySet()) {
            if (playerName.equalsIgnoreCase(entry.getKey())) {
                return ranks.get(playerName);
            }
        }

        return 0;
    }

    public void updatePlayerRank(Player p, Integer playerPoints) {
        String playerName = p.getName();
        if (ranks.containsKey(playerName)) return;

        if (playerPoints >= 1000) {
            ranks.replace(playerName, 4);
            return;
        }

        if (playerPoints >= 900) {
            ranks.replace(playerName, 3);
            return;
        }

        if (playerPoints >= 800) {
            ranks.replace(playerName, 2);
            return;
        }

        if (playerPoints >= 700) {
            ranks.replace(playerName, 2);
            return;
        }

        if (playerPoints >= 600) {
            ranks.replace(playerName, 1);
            return;
        }

        ranks.replace(playerName, 0);
    }
}
