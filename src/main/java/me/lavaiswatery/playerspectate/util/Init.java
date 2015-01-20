package me.lavaiswatery.playerspectate.util;

import me.lavaiswatery.playerspectate.PlayerSpectate;
import me.lavaiswatery.playerspectate.command.Command_spectate;
import me.lavaiswatery.playerspectate.listener.Listener_playerlistener;
import me.lavaiswatery.playerspectate.manager.MessageManager;
import me.lavaiswatery.playerspectate.manager.SpectateManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.HashMap;

public class Init {
    PlayerSpectate pl = PlayerSpectate.getInstance();

    public Init() {
        initLang();
        initInstances();
        registerEvents();
        registerCommands();
    }

    private SpectateManager spectateManager;

    private HashMap<String, String> langStrings = new HashMap<String, String>();

    public void initLang() {
        File file = new File(pl.getDataFolder(), "lang.yml");
        Files.saveDefaultConfig(file);
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        /**
         * Loading all the strings
         */
        langStrings.put("nopermission", ChatColor.translateAlternateColorCodes('&', config.getString("nopermission")));
        langStrings.put("mustbeplayer", ChatColor.translateAlternateColorCodes('&', config.getString("must-be-player")));
        langStrings.put("enterspectate", ChatColor.translateAlternateColorCodes('&', config.getString("enterspectate")));
        langStrings.put("leavespectate", ChatColor.translateAlternateColorCodes('&', config.getString("leavespectate")));
        langStrings.put("noillegalcommands", ChatColor.translateAlternateColorCodes('&', config.getString("noillegalcommands")));
        langStrings.put("farthestpage", ChatColor.translateAlternateColorCodes('&', config.getString("farthestpage")));
        langStrings.put("lowestpage", ChatColor.translateAlternateColorCodes('&', config.getString("lowestpage")));
        langStrings.put("cannotteleporttoself", ChatColor.translateAlternateColorCodes('&', config.getString("cannotteleporttoself")));
        langStrings.put("cannotteleporttoofflineplayers", ChatColor.translateAlternateColorCodes('&', config.getString("cannotteleporttoofflineplayers")));
        langStrings.put("teleportedtotarget", ChatColor.translateAlternateColorCodes('&', config.getString("teleportedtotarget")));
    }

    public void initInstances() {
        spectateManager = new SpectateManager();
    }

    public void registerEvents() {
        new BukkitRunnable() {
            public void run() {
                String[] events = {"playerlistener", "chatlistener"};
                ClassLoader classloader = PlayerSpectate.class.getClassLoader();
                for(String kit : events) {
                    if (CommonUtility.isClass(String.format("%s.%s", Listener_playerlistener.class.getPackage().getName(), "Listener_" + kit.toLowerCase()))) {
                        try {
                            CommonUtility.getPluginManager().registerEvents((Listener) classloader.loadClass(String.format("%s.%s", Listener_playerlistener.class.getPackage().getName(), "Listener_" + kit.toLowerCase())).newInstance(), pl);
                        }
                        catch(InstantiationException ex) {
                            ex.printStackTrace();
                        }
                        catch(ClassNotFoundException ex) {
                            ex.printStackTrace();
                        }
                        catch(IllegalAccessException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }.runTaskLater(pl, 1L);
    }

    public void registerCommands() {
        pl.getCommand("spectate").setExecutor(new Command_spectate());
    }

    public SpectateManager getSpectateManager() { return spectateManager; }

    public HashMap<String, String> getLanguageStrings() { return langStrings; }

}
