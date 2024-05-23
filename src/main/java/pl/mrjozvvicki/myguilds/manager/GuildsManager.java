package pl.mrjozvvicki.myguilds.manager;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import pl.mrjozvvicki.myguilds.entities.Guild;
import pl.mrjozvvicki.myguilds.entities.Nexus;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GuildsManager {
    private static GuildsManager instance;
    private final Material nexusMaterial = Material.AMETHYST_BLOCK;
    private final List<Guild> guilds = new ArrayList<>();

    public void createNexus(Player p, Guild guild) {
        Location loc = p.getLocation();
        Block blockInPlayerLocation = loc.getBlock();

        blockInPlayerLocation.setType(nexusMaterial);
        Nexus nexus = new Nexus(guild, blockInPlayerLocation.getLocation(), 200.0f, 200.0f);
        guild.setNexus(nexus);
    }

    public void addGuild(Guild guild) {
        guilds.add(guild);
    }

    public void removeGuild(Guild guild) {
        guilds.remove(guild);
    }

    public List<Guild> getGuilds() {
        return guilds;
    }

    public int getGuildsCount() {
        return guilds.size();
    }

    public List<Guild> getTopGuilds() {
        List<Guild> topGuilds = new ArrayList<>(guilds);
        topGuilds.sort((g1, g2) -> Integer.compare(g2.getPoints(), g1.getPoints()));
        return topGuilds;
    }

    @Nullable
    public Guild findGuildByLeader(Player player) {
        for (Guild guild : guilds) {
            if (Objects.equals(guild.getLeader(), player.getName())) {
                return guild;
            }
        }

        return null;
    }

    @Nullable
    public Guild findGuildByMember(Player player) {
        for (Guild guild : guilds) {
            if (guild.getMembers().contains(player.getName())) {
                return guild;
            }
        }

        return null;
    }


    public List<Nexus> getAllNexuses() {
        List<Nexus> nexuses = new ArrayList<>();
        for (Guild guild : guilds) {
            nexuses.add(guild.getNexus());
        }

        return nexuses;
    }

    public static GuildsManager getInstance() {
        if (instance == null) {
            instance = new GuildsManager();
        }
        return instance;
    }
}
