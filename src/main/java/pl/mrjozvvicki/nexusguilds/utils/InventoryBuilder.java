package pl.mrjozvvicki.nexusguilds.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class InventoryBuilder {
    public static ItemStack createMenuItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.displayName(Component.text(Chat.colorize("&4" + name)));
            meta.lore(Arrays.stream(lore).map(Component::text).toList());
            item.setItemMeta(meta);
        }

        return item;
    }
}
