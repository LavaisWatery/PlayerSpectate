package me.lavaiswatery.playerspectate.listener;

import me.lavaiswatery.playerspectate.PlayerSpectate;
import me.lavaiswatery.playerspectate.manager.MessageManager;
import me.lavaiswatery.playerspectate.manager.SpectateManager;
import me.lavaiswatery.playerspectate.util.CommonUtility;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;

public class Listener_playerlistener implements Listener {
    PlayerSpectate pl = PlayerSpectate.getInstance();

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if(pl.getInit().getSpectateManager().isSpectating(player)) {
            pl.getInit().getSpectateManager().getSpectateList().remove(player.getName());

            CommonUtility.clearPotions(player);
            CommonUtility.clearAllInventory(player);
            player.setGameMode(GameMode.SURVIVAL);
            player.teleport(player.getWorld().getSpawnLocation());

            for(Player tmp : Bukkit.getServer().getOnlinePlayers()) {
                tmp.showPlayer(player);
            }
        }
    }

    @EventHandler
    public void onPickupItems(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        if(pl.getInit().getSpectateManager().isSpectating(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if(pl.getInit().getSpectateManager().isSpectating(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryEvent(InventoryClickEvent event) {
        if(event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            if(pl.getInit().getSpectateManager().isSpectating(player)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerHitPlayer(EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            if(pl.getInit().getSpectateManager().isSpectating(player)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if(pl.getInit().getSpectateManager().isSpectating(player)) {
            for(Entity e : player.getNearbyEntities(3.0, 3.0, 3.0)) {
                if(!(e instanceof Player)) {
                    continue;
                }
                Player tmp = (Player) e;
                if(!pl.getInit().getSpectateManager().isSpectating(tmp)) {
                    Vector direction = player.getLocation().toVector().subtract(tmp.getLocation().toVector()).normalize();
                    direction.setX(direction.getX() * 1.0D);
                    direction.setY(1);
                    direction.setZ(direction.getZ() * 1.0D);

                    player.setVelocity(direction);
                }
            }
        }
        else {
            for (Entity e : player.getNearbyEntities(3.0, 3.0, 3.0)) {
                if (e instanceof Player) {
                    if(!(e instanceof Player)) {
                        continue;
                    }
                    Player tmp = (Player) e;
                    if (pl.getInit().getSpectateManager().isSpectating(player) && pl.getInit().getSpectateManager().isSpectating(tmp)) {
                        continue;
                    }
                    if (pl.getInit().getSpectateManager().isSpectating(tmp)) {
                        Vector direction = tmp.getLocation().toVector().subtract(player.getLocation().toVector()).normalize();
                        direction.setX(direction.getX() * 1.0D);
                        direction.setY(1);
                        direction.setZ(direction.getZ() * 1.0D);

                        tmp.setVelocity(direction);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerSpectate(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if(pl.getInit().getSpectateManager().isSpectating(player)) {
            String split[] = event.getMessage().split(" ");
            if(split[0].equalsIgnoreCase("/spectate") || split[0].equalsIgnoreCase("/spec") || split[0].equalsIgnoreCase("/reply") || split[0].equalsIgnoreCase("/r") || split[0].equalsIgnoreCase("/ping") || split[0].equalsIgnoreCase("/togglepms") || split[0].equalsIgnoreCase("/mute") || split[0].equalsIgnoreCase("/rules") || split[0].equalsIgnoreCase("/help") || split[0].equalsIgnoreCase("/warps") || split[0].equalsIgnoreCase("/spawn") || split[0].equalsIgnoreCase("/warp") || split[0].equalsIgnoreCase("/msg") || split[0].equalsIgnoreCase("/message") || split[0].equalsIgnoreCase("/tell") || split[0].equalsIgnoreCase("/w") || split[0].equalsIgnoreCase("/m")) {
                return;
            }
            else {
                MessageManager.message(player, pl.getInit().getLanguageStrings().get("noillegalcommands"));
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerOpenInventory(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if(pl.getInit().getSpectateManager().isSpectating(player)) {
            if(event.getItem() != null && event.getItem().getType() != Material.AIR) {
                if(event.getItem().getType() == Material.getMaterial(397) && event.getItem().getDurability() == 3) {
                    if(!event.getItem().hasItemMeta()) {
                        return;
                    }
                    ItemMeta meta = event.getItem().getItemMeta();
                    if((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && ChatColor.stripColor(meta.getDisplayName()).equalsIgnoreCase("Find Players!")) {
                        pl.getInit().getSpectateManager().openPlayerListInventory(player);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if(pl.getInit().getSpectateManager().isSpectating(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if(pl.getInit().getSpectateManager().isSpectating(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerEmptyBucket(PlayerBucketEmptyEvent event) {
        Player player = event.getPlayer();
        if(pl.getInit().getSpectateManager().isSpectating(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerFillBucket(PlayerBucketFillEvent event) {
        Player player = event.getPlayer();
        if(pl.getInit().getSpectateManager().isSpectating(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPaintingBreakByEntity(HangingBreakByEntityEvent event) {
        if(event.getRemover() instanceof Player) {
            Player player = (Player) event.getRemover();
            if(pl.getInit().getSpectateManager().isSpectating(player)) {
                event.setCancelled(true);
            }
        }
        if(event.getRemover() instanceof Projectile) {
            Projectile proj = (Projectile) event.getRemover();
            if(proj.getShooter() instanceof Player) {
                Player player = (Player) proj.getShooter();
                if(pl.getInit().getSpectateManager().isSpectating(player)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onItemRemoveFromItemFrame(EntityDamageByEntityEvent event) {
        Entity e = event.getEntity();
        if(e instanceof ItemFrame) {
            if(event.getDamager() instanceof Player) {
                Player player = (Player) event.getDamager();
                if(pl.getInit().getSpectateManager().isSpectating(player)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPaintingPlace(HangingPlaceEvent event) {
        Player player = event.getPlayer();
        if(pl.getInit().getSpectateManager().isSpectating(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteractWithEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        if(pl.getInit().getSpectateManager().isSpectating(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onSpectateMenuClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            final Player player = (Player) event.getWhoClicked();

            if(!pl.getInit().getSpectateManager().isSpectating(player)) {
                return;
            }

            String title = ChatColor.stripColor(event.getInventory().getTitle());
            if (title.contains("Spectate List")) {
                event.setCancelled(true);
                if (event.getClickedInventory() == event.getInventory()) {

                    if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) {
                        return;
                    }

                    ItemStack item = event.getCurrentItem();
                    ItemMeta meta = null;

                    if (!item.hasItemMeta()) {
                        return;
                    }

                    meta = item.getItemMeta();

                    if(item.getTypeId() == 101) {
                        String name = ChatColor.stripColor(meta.getDisplayName());
                        final List<Player> playerList = (List<Player>) player.getMetadata("playerlist").get(0).value();

                        final int page = player.getMetadata("spectatepage").get(0).asInt();
                        final int max_page = (int) Math.ceil( ((double)playerList.size()) / 36);

                        if(name.equalsIgnoreCase("Page Up")) {
                            if (page >= max_page) {
                                MessageManager.message(player, pl.getInit().getLanguageStrings().get("farthestpage"));
                                return;
                            }
                            else {
                                player.setMetadata("spectatepage", new FixedMetadataValue(pl, page + 1));
                                player.closeInventory();

                                new BukkitRunnable() {
                                    public void run() {
                                        /**
                                         * Fuck my life
                                         */
                                        Inventory inv = pl.getInit().getSpectateManager().createPlayerListInventory(player, (page + 1), max_page);
                                        ItemStack item = null;
                                        ItemMeta meta = null;

                                        // 36 heads in full menu

                                        int slot = 9;
                                        int starting_index = (playerList.size() > 36 ? page * 36 : 0);
                                        int ending_index = (playerList.size() <= starting_index + 36 ? playerList.size() - 1 : starting_index + 36);

                                        for(int i = starting_index; i <= ending_index; i++) {
                                            if(playerList.get(i).getName().equalsIgnoreCase(player.getName())) {
                                                item = new ItemStack(Material.SKULL_ITEM);
                                            } else {
                                                item = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
                                            }
                                            meta = item.getItemMeta();
                                            meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + playerList.get(i).getName());
                                            item.setItemMeta(meta);
                                            inv.setItem(slot, item);
                                            slot = slot + 1;

                                            if(slot > 44) {
                                                break;
                                            }
                                        }

                                        player.openInventory(inv);
                                    }
                                }.runTaskLater(pl, 1L);
                            }
                        }
                        else if(name.equalsIgnoreCase("Page Back")) {
                            if (player.getMetadata("spectatepage").get(0).asInt() <= 1) {
                                MessageManager.message(player, pl.getInit().getLanguageStrings().get("lowestpage"));
                                return;
                            }
                            else {
                                player.setMetadata("spectatepage", new FixedMetadataValue(pl, page - 1));
                                player.closeInventory();

                                new BukkitRunnable() {
                                    public void run() {
                                        /**
                                         * Fuck my life
                                         */
                                        Inventory inv = pl.getInit().getSpectateManager().createPlayerListInventory(player, player.getMetadata("spectatepage").get(0).asInt(), max_page);
                                        ItemStack item = null;
                                        ItemMeta meta = null;

                                        int slot = 9;
                                        int starting_index = (playerList.size() > 36 ? (page - 2) * 36 : 0);
                                        int ending_index = (playerList.size() <= starting_index + 36 ? playerList.size() - 1 : starting_index + 36);

                                        for(int i = starting_index; i <= ending_index; i++) {
                                            if(playerList.get(i).getName().equalsIgnoreCase(player.getName())) {
                                                item = new ItemStack(Material.SKULL_ITEM);
                                            } else {
                                                item = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
                                            }
                                            meta = item.getItemMeta();
                                            meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + playerList.get(i).getName());
                                            item.setItemMeta(meta);
                                            inv.setItem(slot, item);
                                            slot = slot + 1;

                                            if(slot > 44) {
                                                break;
                                            }
                                        }

                                        player.openInventory(inv);
                                    }
                                }.runTaskLater(pl, 1L);
                            }
                        }
                    }

                    if(item.getType() == Material.SKULL_ITEM && item.getDurability() == 0) {
                        MessageManager.message(player, pl.getInit().getLanguageStrings().get("cannotteleporttoself"));
                        event.setCancelled(true);
                        player.closeInventory();
                        return;
                    }
                    else if (item.getType() == Material.SKULL_ITEM) {
                        final String name = ChatColor.stripColor(meta.getDisplayName());
                        if(name != null && !name.equalsIgnoreCase("")) {
                            if(Bukkit.getPlayer(name) != null) {
                                event.setCancelled(true);
                                player.closeInventory();
                                player.teleport(Bukkit.getPlayer(name));
                                String teleportToTarget = pl.getInit().getLanguageStrings().get("teleportedtotarget").replace("%p", Bukkit.getPlayer(name).getName());
                                MessageManager.message(player, teleportToTarget);
                            }
                            else {
                                event.setCancelled(true);
                                player.closeInventory();
                                MessageManager.message(player, pl.getInit().getLanguageStrings().get("cannotteleporttoofflineplayers"));
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

}
