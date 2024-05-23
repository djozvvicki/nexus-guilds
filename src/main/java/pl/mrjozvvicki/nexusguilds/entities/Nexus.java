package pl.mrjozvvicki.nexusguilds.entities;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import pl.mrjozvvicki.nexusguilds.utils.Chat;

public class Nexus {
    private float currentHP;
    private final float maxHP;
    private final Location loc;
    private final Guild guild;
    private Hologram hologram;

    public Nexus(Guild guild, Location loc, float currentHP, float maxHP) {
        this.loc = loc;
        this.guild = guild;
        this.currentHP = currentHP;
        this.maxHP = maxHP;
        createHologram();
    }

    public Location getLocation() {
        return this.loc;
    }

    public void createHologram() {
        Location centerRegionLocation = loc.toCenterLocation();

        Hologram hologram = DHAPI.createHologram(guild.getName() + "-nexus",
                centerRegionLocation.clone().add(0, 1.5, 0));
        DHAPI.addHologramLine(hologram, Chat.colorize("&7[&4" + guild.getName() + "&7] Nexus"));
        DHAPI.addHologramLine(hologram, Chat.colorize("&7[&4Lider&7] " + guild.getLeader()));
        DHAPI.addHologramLine(hologram, Chat.colorize("&7HP: &4" + currentHP + "&7/&4" + maxHP));
        hologram.setDisplayRange(10);

        this.hologram = hologram;
    }

    public void updateHPLine() {
        DHAPI.setHologramLine(hologram, 2, Chat.colorize("&7HP: &4" + currentHP + "&7/&4" + maxHP));
    }

    public void takeDamage(float damage) {
        if (this.currentHP <= 0.0f) {
            for (String member : guild.getMembers()) {
                OfflinePlayer player = Bukkit.getOfflinePlayer(member);

                if (player.isOnline()) {
                    Chat.sendMessage((Player) player, "&7Twój nexus został uszkodzony!");
                }
            }
            return;
        }

        this.currentHP -= damage;

        if (this.currentHP < 0.0f) {
            this.currentHP = 0.0f;
            return;
        }
        updateHPLine();
    }

    public void heal() {
        if (this.currentHP < this.maxHP) {
            this.currentHP += 10.0f;
            updateHPLine();
        }
    }

    public Guild getNexusGuild() {
        return this.guild;
    }

    public float getCurrentHP() {
        return this.currentHP;
    }

    public float getMaxHP() {
        return this.maxHP;
    }

    public void destroy() {
        hologram.destroy();
        loc.getBlock().setType(Material.AIR);
        this.hologram = null;
    }
}
