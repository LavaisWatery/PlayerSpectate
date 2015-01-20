package me.lavaiswatery.playerspectate;

import me.lavaiswatery.playerspectate.manager.MessageManager;
import me.lavaiswatery.playerspectate.util.CommonUtility;
import me.lavaiswatery.playerspectate.util.Init;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Team;

public class PlayerSpectate extends JavaPlugin {
    private static PlayerSpectate instance;

    private Init init;

    public void onEnable() {
        instance = this;

        init = new Init();

        MessageManager.log("PlayerSpectate loaded");
    }

    public void onDisable() {
        for(Player player : getServer().getOnlinePlayers()) {
            if(init.getSpectateManager().getSpectateList().contains(player.getName())) {
                init.getSpectateManager().getSpectateList().remove(player.getName());

                CommonUtility.clearPotions(player);
                CommonUtility.clearAllInventory(player);
                player.setGameMode(GameMode.SURVIVAL);
                player.teleport(player.getWorld().getSpawnLocation());

                for (Player tmp : getServer().getOnlinePlayers()) {
                    tmp.showPlayer(player);
                }
            }
        }
    }

    public Init getInit() { return init; }

    public static PlayerSpectate getInstance() { return instance; }

}
