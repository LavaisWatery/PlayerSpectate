package me.lavaiswatery.playerspectate.manager;

import me.lavaiswatery.playerspectate.PlayerSpectate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpectateManager {
    PlayerSpectate pl = PlayerSpectate.getInstance();

    private List<String> spectateList = new ArrayList<String>();

    public Inventory createPlayerListInventory(Player player, int page, int max_page) {
        Inventory inv = Bukkit.createInventory(player, 9 * 6, "Spectate List (" + page + "/" + max_page + ")");
        ItemStack item = null;
        ItemMeta meta = null;

        ItemStack filler = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 15);
        meta = filler.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_RED + "FILLER");
        filler.setItemMeta(meta);

        for (int i = 0; i < 4; i++) {
            inv.setItem(i, filler);
        }
        for (int i = 5; i < 9; i++) {
            inv.setItem(i, filler);
        }

        item = new ItemStack(Material.PAPER);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Spectate List");
        item.setItemMeta(meta);

        inv.setItem(4, item);

        item = new ItemStack(Material.getMaterial(101));
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Page Up");
        item.setItemMeta(meta);

        inv.setItem(9*6 - 1, item);

        for(int i = 9*6 - 8; i <= 9*6 - 2; i++) {
            inv.setItem(i, filler);
        }

        meta.setDisplayName(ChatColor.RED + "Page Back");
        item.setItemMeta(meta);
        inv.setItem(9*6 - 9, item);

        return inv;
    }

    public void openPlayerListInventory(Player player) {
        List<Player> list = new ArrayList<Player>(Arrays.asList(Bukkit.getServer().getOnlinePlayers()));
        player.setMetadata("playerlist", new FixedMetadataValue(pl, list));

        if(player.hasMetadata("playerlist")) {
            int max_pages = (int) Math.ceil( ((double)list.size()) / 36);

            Inventory inv = createPlayerListInventory(player, 1, max_pages);
            ItemStack item = null;
            ItemMeta meta = null;

            /**
             * Setting up player heads
             */
            int slot = 9;
            for(Player t : list) {
                if(t.getName().equalsIgnoreCase(player.getName())) {
                    item = new ItemStack(Material.SKULL_ITEM);
                } else {
                    item = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
                }
                meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + t.getName());
                item.setItemMeta(meta);
                inv.setItem(slot, item);
                slot = slot + 1;

                if(slot > 44) {
                    break;
                }
            }

            player.openInventory(inv);
            player.setMetadata("spectatepage", new FixedMetadataValue(pl, 1));
        }
        else { }
    }

    public boolean isSpectating(Player player) {
        return spectateList.contains(player.getName());
    }

    public List<String> getSpectateList() { return spectateList; }

}
