package me.lavaiswatery.playerspectate;

import me.lavaiswatery.playerspectate.manager.MessageManager;
import me.lavaiswatery.playerspectate.util.Init;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerSpectate extends JavaPlugin {
    private static PlayerSpectate instance;

    private Init init;

    public void onEnable() {
        instance = this;

        init = new Init();

        MessageManager.log("PlayerSpectate loaded");
    }

    public void onDisable() {

    }

    public Init getInit() { return init; }

    public static PlayerSpectate getInstance() { return instance; }

}
