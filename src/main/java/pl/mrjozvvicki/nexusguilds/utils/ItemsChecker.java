package pl.mrjozvvicki.nexusguilds.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemsChecker {
    private static ItemsChecker instance;
    private static final Map<Material, Integer> itemsToCheck = new HashMap<>();

    public ItemsChecker() {
        // ItemsToCheck for nexus
        itemsToCheck.put(Material.GOLDEN_APPLE, 8);
        itemsToCheck.put(Material.DIAMOND, 32);
        itemsToCheck.put(Material.CARROT, 64);
        itemsToCheck.put(Material.LEATHER, 32);
    }

    public static ItemsChecker getInstance() {
        if (instance == null) {
            instance = new ItemsChecker();
        }

        return instance;
    }

    public static boolean hasItems(Player player) {
        List<Boolean> hasAllItems = new ArrayList<>();

        for (Map.Entry<Material, Integer> entry : itemsToCheck.entrySet()) {
            Material itemType = entry.getKey();
            int amount = entry.getValue();

            ItemStack itemToCheck = new ItemStack(itemType, amount);
            if (!player.getInventory().containsAtLeast(itemToCheck, amount)) {
                continue;
            }

            hasAllItems.add(true);
        }

        return hasAllItems.size() == itemsToCheck.size();
    }

    public static void removeItems(Player player) {
        for (Map.Entry<Material, Integer> entry : itemsToCheck.entrySet()) {
            Material itemType = entry.getKey();
            int amount = entry.getValue();
            removeItemFromInventory(player, itemType, amount);
        }
    }

    public static void removeItemFromInventory(Player player, Material material, int amount) {
        ItemStack[] items = player.getInventory().getContents();

        for (int i = 0; i < items.length; i++) {
            ItemStack item = items[i];

            if (item != null && item.getType() == material) {
                if (item.getAmount() > amount) {
                    item.setAmount(item.getAmount() - amount);
                    break;
                } else {
                    amount -= item.getAmount();
                    items[i] = null;

                    if (amount == 0) {
                        break;
                    }
                }
            }
        }

        player.getInventory().setContents(items);
    }
}
