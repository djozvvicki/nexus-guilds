package pl.mrjozvvicki.nexusguilds;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import pl.mrjozvvicki.nexusguilds.command.NexusGuildsCommand;
import pl.mrjozvvicki.nexusguilds.listeners.NexusListener;
import pl.mrjozvvicki.nexusguilds.manager.GuildsConfigManager;
import pl.mrjozvvicki.nexusguilds.manager.TabListManager;

import java.io.IOException;

/**
 * Main class for the NexusGuilds plugin.
 */
public final class NexusGuilds extends JavaPlugin {
    private final GuildsConfigManager guildsConfigManager;
    private final TabListManager tabListManager;

    public NexusGuilds() {
        this.guildsConfigManager = new GuildsConfigManager(this);
        this.tabListManager = TabListManager.getInstance();
    }

    @Override
    public void onEnable() {
        loadData();
        registerCommandsAndEvents();
        initializeTabListUpdater();

        getLogger().info("nexusguilds > Plugin enabled");
    }

    @Override
    public void onDisable() {
        saveData();
        getLogger().info("nexusguilds > Plugin disabled");
    }

    private void loadData() {
        try {
            guildsConfigManager.loadData();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load data", e);
        }
    }

    private void saveData() {
        try {
            guildsConfigManager.saveData();
        } catch (IOException e) {
            throw new RuntimeException("Failed to save data", e);
        }
    }

    private void registerCommandsAndEvents() {
        PluginCommand command = getCommand("nexusguilds");
        if (command == null) return;

        command.setExecutor(new NexusGuildsCommand(this));
        getServer().getPluginManager().registerEvents(new NexusListener(), this);
    }

    private void initializeTabListUpdater() {
        tabListManager.updateTabList();

        new BukkitRunnable() {
            @Override
            public void run() {
                tabListManager.updateTabList();
            }
        }.runTaskTimer(this, 0, 20 * 60);
    }
}