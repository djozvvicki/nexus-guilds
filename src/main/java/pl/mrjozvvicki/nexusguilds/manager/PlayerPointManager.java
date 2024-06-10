package pl.mrjozvvicki.nexusguilds.manager;

import org.bukkit.entity.Player;
import pl.mrjozvvicki.nexusguilds.entities.Guild;

import java.util.HashMap;
import java.util.Map;

public class PlayerPointManager {
    private static PlayerPointManager instance;
    private static final Map<String, Integer> playerPoints = new HashMap<>();

    public static PlayerPointManager getInstance() {
        if (instance == null) {
            instance = new PlayerPointManager();
        }

        return instance;
    }

    public void initPlayerPoints(Player p) {
        if (playerPoints.containsKey(p.getName())) return;
        playerPoints.put(p.getName(), 500);
    }

    public void changePlayerPoints(Player p, int newPoints) {
        if (!playerPoints.containsKey(p.getName())) return;

        playerPoints.replace(p.getName(), newPoints);
        GuildsManager guildsManager = GuildsManager.getInstance();
        Guild guild = guildsManager.findGuildByMember(p);
        if (guild != null) guild.updatePoints();
    }

    public Integer getPlayerPoints(Player p) {
        if (!playerPoints.containsKey(p.getName())) return 500;
        return playerPoints.get(p.getName());
    }
}
