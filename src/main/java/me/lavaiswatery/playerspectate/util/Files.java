package me.lavaiswatery.playerspectate.util;

import me.lavaiswatery.playerspectate.PlayerSpectate;

import java.io.File;

public class Files {

    public static File createFile(String s) {
        return new File(PlayerSpectate.getInstance().getDataFolder(), s + ".yml");
    }

    @SuppressWarnings("null")
    public static void saveDefaultConfig(File customConfigFile) {
        if (customConfigFile == null) {
            customConfigFile = new File(PlayerSpectate.getInstance().getDataFolder(), customConfigFile.getName());
        }
        if (!customConfigFile.exists()) {
            PlayerSpectate.getInstance().saveResource(customConfigFile.getName(), false);
        }
    }

}
