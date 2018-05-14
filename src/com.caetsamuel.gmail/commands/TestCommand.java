package com.caetsamuel.gmail.commands;

import com.caetsamuel.gmail.VNCScreen;
import com.caetsamuel.gmail.VNCraft;
import com.caetsamuel.gmail.util.Callback;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;


//Used to test the setting of itemframes on a wall.
public class TestCommand implements CommandExecutor {

    VNCraft plugin = VNCraft.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (label.equalsIgnoreCase("test")) {
                System.out.println("FRAME SETUP!");

                final Player player = (Player) sender;

                player.sendMessage("  ");
                player.sendMessage("Right click the first location!");
                plugin.interactListener.listenForInteract(player, new Callback<PlayerInteractEvent>() {
                    @Override
                    public void call(PlayerInteractEvent event) {
                        if (event != null && event.getClickedBlock() != null) {
                            final Block block1 = event.getClickedBlock();
                            player.sendMessage("First location has been set!");
                            player.sendMessage("  ");

                            Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                                @Override
                                public void run() {
                                    player.sendMessage("Right click the second location!");
                                    plugin.interactListener.listenForInteract(player, new Callback<PlayerInteractEvent>() {
                                        @Override
                                        public void call(final PlayerInteractEvent event) {
                                            if (event != null && event.getClickedBlock() != null) {
                                                final Block block2 = event.getClickedBlock();
                                                player.sendMessage("Second location has been set!");
                                                player.sendMessage("  ");

                                                player.sendMessage("Setup Complete!");
                                                player.sendMessage("  ");

                                                VNCScreen screen = new VNCScreen(block1.getLocation(), block2.getLocation());
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
