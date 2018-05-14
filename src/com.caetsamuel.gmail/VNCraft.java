package com.caetsamuel.gmail;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The main plugin class.
 * Created by SecondAmendment on 5/11/2018.
 */
public class VNCraft extends JavaPlugin implements Listener {
    @Getter private static VNCraft instance;

    @Override
    public void onEnable() {
        instance = this;

        if (!Bukkit.getPluginManager().isPluginEnabled("MapManager")) {
            getLogger().warning("**************************************************");
            getLogger().warning("                                                  ");
            getLogger().warning("         This plugin depends on MapManager        ");
            getLogger().warning("             https://r.spiget.org/19198           ");
            getLogger().warning("                                                  ");
            getLogger().warning("**************************************************");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        else{
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "MinecraftVNC has been initialized!");
        }

        getServer().getPluginManager().registerEvents(getInstance(), this);
    }
}
