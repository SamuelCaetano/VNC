package com.caetsamuel.gmail;

import com.caetsamuel.gmail.commands.TestCommand;
import com.caetsamuel.gmail.listeners.InteractListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class MinecraftVNCPlugin extends JavaPlugin implements Listener {

    private static MinecraftVNCPlugin instance;

    public InteractListener interactListener;

    private ConsoleCommandSender console;

    @Override
    public void onEnable() {
        instance = this;
        console = getServer().getConsoleSender();

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
            console.sendMessage(ChatColor.GREEN + "MinecraftVNC has been initialized!");
        }

        Bukkit.getPluginManager().registerEvents(interactListener = new InteractListener(), this);

        getServer().getPluginManager().registerEvents(instance, this);

        instance.getCommand("test").setExecutor(new TestCommand());
    }

    @Override
    public void onLoad() {
        super.onLoad();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public static MinecraftVNCPlugin getInstance(){
        return instance;
    }
}
