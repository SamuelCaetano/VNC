package com.caetsamuel.gmail;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.inventivetalent.animatedframes.AnimatedFrame;
import org.inventivetalent.animatedframes.AnimatedFramesPlugin;

public class MinecraftVNCPlugin extends JavaPlugin implements Listener {

    private static MinecraftVNCPlugin instance;
    private ConsoleCommandSender console;

    boolean doOnce = true;

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

        getServer().getPluginManager().registerEvents(instance, this);
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

    @EventHandler
    public void onText(PlayerEggThrowEvent event){
        System.out.println("THIS WORKED!");
        if(doOnce = true) {
            AnimatedFramesPlugin plugin = (AnimatedFramesPlugin) Bukkit.getPluginManager().getPlugin("AnimatedFrames");
            VNCScreen test = new VNCScreen(plugin.frameManager.getFrame("screen1"));
        }
        doOnce = false;
    }
}
