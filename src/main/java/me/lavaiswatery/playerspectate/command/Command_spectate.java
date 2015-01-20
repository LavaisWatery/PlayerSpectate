package me.lavaiswatery.playerspectate.command;

import me.lavaiswatery.playerspectate.PlayerSpectate;
import me.lavaiswatery.playerspectate.manager.MessageManager;
import me.lavaiswatery.playerspectate.manager.SpectateManager;
import me.lavaiswatery.playerspectate.util.CommonUtility;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Command_spectate implements CommandExecutor {
    PlayerSpectate pl = PlayerSpectate.getInstance();

    public boolean onCommand(CommandSender sender, Command command, String title, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if(!player.hasPermission("playerspectate.spectate")) {
                return MessageManager.message(player, pl.getInit().getLanguageStrings().get("nopermission"));
            }
            if(pl.getInit().getSpectateManager().isSpectating(player)) {
                pl.getInit().getSpectateManager().getSpectateList().remove(player.getName());

                CommonUtility.clearPotions(player);
                CommonUtility.clearAllInventory(player);
                player.setGameMode(GameMode.SURVIVAL);
                player.teleport(player.getWorld().getSpawnLocation());

                for(Player tmp : Bukkit.getServer().getOnlinePlayers()) {
                    tmp.showPlayer(player);
                }

                MessageManager.message(player, pl.getInit().getLanguageStrings().get("leavespectate"));
            }
            else {
                pl.getInit().getSpectateManager().getSpectateList().add(player.getName());

                CommonUtility.clearPotions(player);
                CommonUtility.clearAllInventory(player);
                player.setGameMode(GameMode.CREATIVE);

                for(Player tmp : Bukkit.getServer().getOnlinePlayers()) {
                    if(tmp.hasPermission("playerspectate.seeplayers")) {
                        continue;
                    }
                    tmp.hidePlayer(player);
                }

                ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Find Players!");
                item.setItemMeta(meta);
                player.getInventory().addItem(item);

                MessageManager.message(player, pl.getInit().getLanguageStrings().get("enterspectate"));
            }
        }
        else {
            MessageManager.message(sender, pl.getInit().getLanguageStrings().get("mustbeplayer"));
        }

        return true;
    }

}
