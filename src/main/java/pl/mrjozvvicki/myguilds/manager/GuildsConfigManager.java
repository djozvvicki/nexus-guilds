package pl.mrjozvvicki.myguilds.manager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import pl.mrjozvvicki.myguilds.entities.Guild;
import pl.mrjozvvicki.myguilds.entities.Nexus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuildsConfigManager {
    private final JavaPlugin plugin;
    private final String fileName = "guilds.yml";
    private final GuildsManager guildsManager;

    public GuildsConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.guildsManager = GuildsManager.getInstance();
    }

    public void loadData() throws IOException {
        try {
            loadGuildsData(new File(plugin.getDataFolder(), fileName));
            plugin.getLogger().info("Guilds loaded successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveData() throws IOException {
        try {
            saveGuildsData(new File(plugin.getDataFolder(), fileName));
            plugin.getLogger().info("Guilds saved successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadGuildsData(File file) throws IOException {
        if (!file.exists()) {
            plugin.saveResource(fileName, false);
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection guildsSection = config.getConfigurationSection("guilds");

        plugin.getLogger().info("Wczytywanie gildii");
        if (guildsSection != null) {
            for (String key : guildsSection.getKeys(false)) {
                ConfigurationSection guildSection = guildsSection.getConfigurationSection(key);

                String guildName = guildSection.getString("name");
                String leader = guildSection.getString("leader");
                List<String> members = guildSection.getStringList("members");
                plugin.getLogger().info("Wczytano gildie: " + guildName);

                Guild createdGuild = new Guild(guildName, leader);

                for (String member : members) {
                    if (member.equals(leader)) continue;
                    createdGuild.addMember(member);
                }

                guildsManager.addGuild(createdGuild);
                plugin.getLogger().info("Dodano gildie: " + guildName);

                ConfigurationSection nexusSection = guildSection.getConfigurationSection("nexus");
                if (nexusSection != null) {
                    double currentHP = nexusSection.getDouble("currentHP");
                    double maxHP = nexusSection.getDouble("maxHP");
                    String locationString = nexusSection.getString("location");
                    Location location = parseLocation(locationString);
                    plugin.getLogger().info("Wczytano nexus gildii" + guildName);

                    Nexus nexus = new Nexus(createdGuild, location, (float) currentHP, (float) maxHP);

                    createdGuild.setNexus(nexus);
                    plugin.getLogger().info("Dodano nexus gildii" + guildName);
                }


            }
        }
    }

    private Location parseLocation(String locationString) {
        String[] parts = locationString.split(",");
        String world = parts[0];
        double x = Double.parseDouble(parts[1]);
        double y = Double.parseDouble(parts[2]);
        double z = Double.parseDouble(parts[3]);
        return new Location(Bukkit.getWorld(world), x, y, z);
    }

    private String formatLocation(Location location) {
        return location.getWorld().getName() + "," + location.getX() + "," + location.getY() + "," + location.getZ();
    }

    public void saveGuildsData(File file) throws IOException {
        List<Guild> guilds = guildsManager.getGuilds();
        FileConfiguration config = new YamlConfiguration();
        ConfigurationSection guildsSection = config.createSection("guilds");

        for (int i = 0; i < guilds.size(); i++) {
            Guild guild = guilds.get(i);
            ConfigurationSection guildSection = guildsSection.createSection(String.valueOf(i));

            guildSection.set("name", guild.getName());
            guildSection.set("leader", guild.getLeader());

            List<String> members = new ArrayList<>(guild.getMembers());

            guildSection.set("members", members);

            if (guild.getNexus() != null) {
                ConfigurationSection nexusSection = guildSection.createSection("nexus");
                Nexus nexus = guild.getNexus();
                nexusSection.set("currentHP", nexus.getCurrentHP());
                nexusSection.set("maxHP", nexus.getMaxHP());
                nexusSection.set("location", formatLocation(nexus.getLocation()));
            }
        }

        config.save(file);
    }
}
