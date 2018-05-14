package com.caetsamuel.gmail.commands;

import com.caetsamuel.gmail.MinecraftVNCPlugin;
import com.caetsamuel.gmail.VNCScreen;
import com.caetsamuel.gmail.util.Callback;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;


//Used to test the setting of itemframes on a wall.
public class TestCommand implements CommandExecutor {

    MinecraftVNCPlugin plugin = MinecraftVNCPlugin.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (label.equalsIgnoreCase("test")) {
                System.out.println("FRAME SETUP!");

                final Player player = (Player) sender;

                player.sendMessage("  ");
                player.sendMessage("Right click the first location!");
                plugin.interactListener.listenForEntityInteract(player, new Callback<PlayerInteractEntityEvent>() {
                    @Override
                    public void call(PlayerInteractEntityEvent event) {
                        if (event != null && event.getRightClicked().getType() == EntityType.ITEM_FRAME) {
                            final ItemFrame firstFrame = (ItemFrame) event.getRightClicked();
                            player.sendMessage("First location has been set!");
                            player.sendMessage("  ");

                            Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                                @Override
                                public void run() {
                                    player.sendMessage("Right click the second location!");
                                    plugin.interactListener.listenForEntityInteract(player, new Callback<PlayerInteractEntityEvent>() {
                                        @Override
                                        public void call(final PlayerInteractEntityEvent event) {
                                            if (event != null && event.getRightClicked().getType() == EntityType.ITEM_FRAME) {
                                                final ItemFrame secondFrame = (ItemFrame) event.getRightClicked();
                                                player.sendMessage("Second location has been set!");
                                                player.sendMessage("  ");

                                                player.sendMessage("Setup Complete!");
                                                player.sendMessage("  ");

                                                VNCScreen screen = new VNCScreen(firstFrame.getLocation(), secondFrame.getLocation());
                                            }
                                        }
                                    });
                                }
                            }, 10);
                        }
                    }
                });
            }
        }

        return true;
    }
}
