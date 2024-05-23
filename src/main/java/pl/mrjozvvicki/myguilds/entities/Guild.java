package pl.mrjozvvicki.myguilds.entities;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import pl.mrjozvvicki.myguilds.utils.Chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Guild {
    private final String name;
    private String leader;
    private final List<String> members;
    private final Map<String, Integer> memberPoints = new HashMap<>();
    private Nexus nexus;

    public Guild(String name, String leader) {
        this.name = name;
        this.leader = leader;
        this.members = new ArrayList<>();
        this.addMember(leader);
        if (!this.memberPoints.containsKey(leader)) {
            this.memberPoints.put(leader, 500);
        }
    }

    public int getPoints() {
        int guildPoints = 0;

        for (Integer memberPoints : memberPoints.values()) {
            guildPoints += memberPoints;
        }

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
        if (!players.contains(Bukkit.getOfflinePlayer(member))) {
            return false;
        }

        Player player = Bukkit.getPlayer(member);
        if (player != null) {
            player.displayName(Component.text(Chat.colorize("&7[&4" + getName() + "&7]" + player.displayName())));
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
