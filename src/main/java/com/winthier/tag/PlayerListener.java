package com.winthier.tag;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

class PlayerListener implements Listener {
    final WinTagPlugin plugin;

    PlayerListener(final WinTagPlugin winTagPlugin) {
        plugin = winTagPlugin;
    }

    void onEnable() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(ignoreCancelled = false, priority = EventPriority.LOW)
    void onEntityDamageByEntityEvent(EntityDamageByEntityEvent entityDamageByEntityEvent) {
        Game game = plugin.getGame();
        if (game == null) {
            return;
        }
        if (!(entityDamageByEntityEvent.getEntity() instanceof Player)) {
            return;
        }
        if (!(entityDamageByEntityEvent.getDamager() instanceof Player)) {
            return;
        }
        Player player = (Player) entityDamageByEntityEvent.getDamager();
        Player player2 = (Player) entityDamageByEntityEvent.getEntity();
        game.onPlayerHitPlayer(player, player2, entityDamageByEntityEvent);
    }
}

