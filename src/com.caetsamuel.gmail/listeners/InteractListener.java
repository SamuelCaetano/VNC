package com.caetsamuel.gmail.listeners;

import com.caetsamuel.gmail.util.Callback;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InteractListener implements Listener {

    private final Map<UUID, Callback<PlayerInteractEntityEvent>>entityInteractMap = new HashMap<>();
    private final Map<UUID, Callback<PlayerInteractEvent>>interactMap = new HashMap<>();

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getRightClicked().getType() == EntityType.ITEM_FRAME) {
            Callback<PlayerInteractEntityEvent> callback;
            while ((callback = entityInteractMap.remove(event.getPlayer().getUniqueId())) != null)
                callback.call(event);
        }
    }

    public void listenForEntityInteract(Player player, Callback<PlayerInteractEntityEvent> callback) {
        if (callback == null) { return; }
        if (player != null) {
            entityInteractMap.put(player.getUniqueId(), callback);
        } else {
            callback.call(null);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null) {
            Callback<PlayerInteractEvent> callback;
            while ((callback = interactMap.remove(event.getPlayer().getUniqueId())) != null)
                callback.call(event);
        }
    }

    public void listenForInteract(Player player, Callback<PlayerInteractEvent> callback) {
        if (callback == null) { return; }
        if (player != null) {
            interactMap.put(player.getUniqueId(), callback);
        } else {
            callback.call(null);
        }
    }

}