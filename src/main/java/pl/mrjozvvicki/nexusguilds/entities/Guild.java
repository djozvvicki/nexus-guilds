package pl.mrjozvvicki.nexusguilds.entities;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import pl.mrjozvvicki.nexusguilds.manager.PlayerPointManager;

import java.util.ArrayList;
import java.util.List;

public class Guild {
    private final String name;
    private String leader;
    private final List<String> members;
    private Integer guildPoints;
    private Nexus nexus;

    public Guild(String name, String leader) {
        this.name = name;
        this.members = new ArrayList<>();
        this.changeLeader(leader);
        this.updatePoints();
    }

    public void updatePoints() {
        PlayerPointManager playerPointManager = PlayerPointManager.getInstance();

        int newGuildPoints = 0;
        for (String member : members) {
            Player p = Bukkit.getPlayer(member);
            if (p == null) continue;
            newGuildPoints += playerPointManager.getPlayerPoints(p);
        }

        guildPoints = newGuildPoints;
    }


    public int getPoints() {
        return guildPoints;
    }

    public void setNexus(Nexus nexus) {
        this.nexus = nexus;
    }

    public void destroyNexus() {
        nexus.destroy();
        this.nexus = null;
    }

    public Nexus getNexus() {
        return this.nexus;
    }

    public String getName() {
        return this.name;
    }

    public String getLeader() {
        return this.leader;
    }

    public List<String> getMembers() {
        return this.members;
    }

    public boolean addMember(String member) {
        if (hasMember(member)) {
            return false;
        }

        List<OfflinePlayer> players = List.of(Bukkit.getOfflinePlayers());
        OfflinePlayer memberOP = Bukkit.getOfflinePlayer(member);
        if (!players.contains(memberOP)) {
            return false;
        }

        if (memberOP.getPlayer() != null) {
            this.members.add(member);
            return true;
        }

        return false;
    }

    public boolean removeMember(String member) {
        if (!hasMember(member)) {
            return false;
        }

        this.members.remove(member);
        return true;
    }

    public boolean hasMember(String member) {
        return this.members.contains(member);
    }

    public void changeLeader(String newLeader) {
        this.members.remove(leader);
        this.leader = newLeader;
        this.members.add(newLeader);
    }
}
