package pl.mrjozvvicki.myguilds;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import pl.mrjozvvicki.myguilds.command.MyGuildsCommand;
import pl.mrjozvvicki.myguilds.listeners.NexusListener;
import pl.mrjozvvicki.myguilds.manager.GuildsConfigManager;
import pl.mrjozvvicki.myguilds.manager.TabListManager;

import java.io.IOException;
import java.util.Objects;

/**
 * Main class for the MyGuilds plugin.
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

        getLogger().info("MyGuilds > Plugin enabled");
    }

    @Override
    public void onDisable() {
        saveData();
        getLogger().info("MyGuilds > Plugin disabled");
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
        Objects.requireNonNull(getCommand("myguilds")).setExecutor(new MyGuildsCommand(this));
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