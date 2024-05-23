package pl.mrjozvvicki.myguilds.listeners;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pl.mrjozvvicki.myguilds.entities.Guild;
import pl.mrjozvvicki.myguilds.entities.Nexus;
import pl.mrjozvvicki.myguilds.manager.GuildsManager;
import pl.mrjozvvicki.myguilds.utils.Chat;
import pl.mrjozvvicki.myguilds.utils.InventoryBuilder;

import java.util.Collections;
import java.util.List;

public class NexusListener implements Listener {
    private final GuildsManager guildsManager;

    public NexusListener() {
        this.guildsManager = GuildsManager.getInstance();
    }

    @EventHandler
    public void openInventory(InventoryOpenEvent event) {
        for (Nexus nexus : guildsManager.getAllNexuses()) {
            if (nexus == null) return;
            Guild guild = nexus.getNexusGuild();
            InventoryType type = event.getInventory().getType();

            if (type == InventoryType.CHEST ||
                    type == InventoryType.BEACON ||
                    type == InventoryType.FURNACE ||
                    type == InventoryType.BARREL ||
                    type == InventoryType.HOPPER ||
                    type == InventoryType.DISPENSER ||
                    type == InventoryType.DROPPER ||
                    type == InventoryType.CHISELED_BOOKSHELF ||
                    type == InventoryType.ENCHANTING ||
                    type == InventoryType.SMOKER
            ) {
                Location chestLocation = event.getInventory().getLocation();

                if (chestLocation != null) {
                    double distance = Math.abs(chestLocation.distance(nexus.getLocation()));

                    if (distance <= 10.0f) {
                        if (!guild.getMembers().contains(event.getPlayer().getName())) {
                            Chat.sendMessage((Player) event.getPlayer(), "&7Nie możesz otwierać skrzynek w pobliżu nexusa!");
                            event.setCancelled(true);
                            return;
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onNexusAttack(PlayerInteractEvent event) {
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();

            for (Nexus nexus : guildsManager.getAllNexuses()) {
                if (nexus == null || block == null) return;
                Location nexusLocation = nexus.getLocation();
                Location blockLocation = block.getLocation();

                if (nexusLocation.equals(blockLocation)) {
                    if (nexus.getCurrentHP() != 0.0f) {
                        nexus.takeDamage(10f);
                    }

                    event.setCancelled(true); // Blokada niszczenia nexusa
                }
            }
        }
    }

    private String getMenuTitle(String guildName) {
        return Chat.colorize("&7[&4" + guildName + "&7] Nexus Menu");
    }

    @EventHandler
    public void onNexusClick(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();

            for (Nexus nexus : guildsManager.getAllNexuses()) {
                if (nexus == null || block == null) return;
                Location nexusLocation = nexus.getLocation();
                Location blockLocation = block.getLocation();
                Guild guild = nexus.getNexusGuild();
                Player player = event.getPlayer();
                if (nexusLocation.equals(blockLocation) && player.getName().equals(guild.getLeader())) {
                    Inventory inventory = Bukkit.createInventory(null, 9, Component.text(Chat.colorize(getMenuTitle(guild.getName()))));
                    ItemStack upgradeItem = InventoryBuilder.createMenuItem(Material.ANVIL, "Ulepszenie", "Ulepsz nexus swojej gildii");
                    ItemStack healItem = InventoryBuilder.createMenuItem(Material.HEART_OF_THE_SEA, "Naprawa", "Napraw swój nexus");
                    ItemStack shopItem = InventoryBuilder.createMenuItem(Material.EMERALD, "Sklep nexusa", "Otwórz sklep nexusa!");

                    inventory.setItem(0, upgradeItem);
                    inventory.setItem(4, healItem);
                    inventory.setItem(8, shopItem);

                    player.openInventory(inventory);
                }
            }
        }
    }


    @EventHandler
    public void onNexusMenuClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        Guild guild = guildsManager.findGuildByLeader(player);
        if (guild == null) return;
        if (e.getView().title().equals(Component.text(Chat.colorize(getMenuTitle(guild.getName()))))) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) return;

            switch (e.getCurrentItem().getType()) {
                case ANVIL:
                    Chat.sendMessage(player, "&7Upgrade nexusa!");
                    player.closeInventory();
                    return;
                case HEART_OF_THE_SEA:
                    guild.getNexus().heal();
                    if (guild.getNexus().getCurrentHP() == guild.getNexus().getMaxHP()) {
                        player.closeInventory();
                    }
                    break;
                case EMERALD:
                    Chat.sendMessage(player, "&7Nexus shop");
                    player.closeInventory();
                    break;
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        for (Nexus nexus : guildsManager.getAllNexuses()) {
            Guild guild = nexus.getNexusGuild();
            Block block = event.getBlock();
            Location nexusLocation = nexus.getLocation();
            Location blockLocation = block.getLocation();
            double distance = Math.abs(blockLocation.distance(nexusLocation));

            if (distance <= 10.0f) {
                if (!guild.getMembers().contains(event.getPlayer().getName())) {
                    Material type = block.getType();

                    if (type == Material.CHEST ||
                            type == Material.BEACON ||
                            type == Material.FURNACE ||
                            type == Material.BARREL ||
                            type == Material.HOPPER ||
                            type == Material.DISPENSER ||
                            type == Material.DROPPER ||
                            type == Material.CHISELED_BOOKSHELF ||
                            type == Material.ENCHANTING_TABLE ||
                            type == Material.SMOKER
                    ) {
                        event.setCancelled(true);
                        Chat.sendMessage(event.getPlayer(), "&7Nie możesz zniszczyć tego bloku w pobliżu wrogiego nexusa!");
                        return;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        for (Nexus nexus : guildsManager.getAllNexuses()) {
            if (nexus != null) {
                Location nexusLoc = nexus.getLocation().getBlock().getLocation();

                for (Block block : event.blockList()) {
                    Location blockLoc = block.getLocation();
                    double distance = Math.abs(blockLoc.distance(nexusLoc));

                    if (distance <= 10.0f) {
                        event.setCancelled(true);
                        return;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onExplosionPrime(ExplosionPrimeEvent event) {
        for (Nexus nexus : guildsManager.getAllNexuses()) {
            if (nexus != null) {
                Location nexusLoc = nexus.getLocation().getBlock().getLocation();
                Location entityLoc = event.getEntity().getLocation();

                double distance = entityLoc.distance(nexusLoc);

                if (distance <= 10.0f) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPistonAction(BlockPistonEvent event) {
        if (event instanceof BlockPistonExtendEvent || event instanceof BlockPistonRetractEvent) {
            for (Nexus nexus : guildsManager.getAllNexuses()) {
                if (nexus != null) {
                    List<Block> blocks;
                    if (event instanceof BlockPistonExtendEvent) {
                        blocks = Collections.singletonList(event.getBlock());
                    } else {
                        blocks = ((BlockPistonRetractEvent) event).getBlocks();
                    }

                    for (Block block : blocks) {
                        Location nexusLoc = nexus.getLocation();
                        Location blockLoc = block.getLocation();

                        if (blockLoc.equals(nexusLoc)) {
                            event.setCancelled(true);
                            return;
                        }
                    }
                }
            }
        }
    }
}
