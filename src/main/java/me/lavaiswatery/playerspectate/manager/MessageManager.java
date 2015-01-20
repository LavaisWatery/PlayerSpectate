package me.lavaiswatery.playerspectate.manager;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class MessageManager {

    public static boolean message(Player player, String s) {
        player.sendMessage(s);
        return true;
    }

    public static boolean message(CommandSender sender, String s) {
        sender.sendMessage(s);
        return true;
    }

    public static boolean message(List<Player> players, String s) {
        for(Player player : players) {
            player.sendMessage(s);
        }
        return true;
    }

    public static boolean message(Player[] players, String s) {
        for(Player player : players) {
            player.sendMessage(s);
        }
        return true;
    }

    public static boolean log(String s) {
        Bukkit.getServer().getLogger().info(s);
        return true;
    }

}
