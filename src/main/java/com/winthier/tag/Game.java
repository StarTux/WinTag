package com.winthier.tag;

import java.util.HashSet;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.scheduler.BukkitRunnable;

final class Game {
    private final WinTagPlugin plugin;
    private Player it;
    private long lastHit = 0L;
    private Set<Player> participants = new HashSet<Player>();
    private BukkitRunnable timer;

    Game(final WinTagPlugin winTagPlugin) {
        plugin = winTagPlugin;
    }

    Player getIt() {
        return it;
    }

    private void stopTimer() {
        if (timer != null) {
            try {
                timer.cancel();
            } catch (IllegalStateException illegalStateException) {
                // empty catch block
            }
            timer = null;
        }
    }

    private void resetTimer() {
        stopTimer();
        timer = new BukkitRunnable() {
            public void run() {
                plugin.stopGame();
            }
        };
        timer.runTaskLater(plugin, plugin.getConfiguration().gameTime * 20L);
    }

    void start(Player player) {
        it = player;
        participants.add(it);
        String string = plugin.getConfiguration().gameStart;
        string = plugin.getConfiguration().replaceVariables(string, player);
        broadcast(string);
        resetTimer();
    }

    void stop() {
        stopTimer();
        String string = plugin.getConfiguration().gameStop;
        string = plugin.getConfiguration().replaceVariables(string, it);
        broadcast(string);
        it = null;
    }

    void onPlayerHitPlayer(Player player, Player player2, Cancellable cancellable) {
        if (player != it) {
            return;
        }
        if (player.equals(player2)) {
            return;
        }
        if (System.currentTimeMillis() - lastHit < plugin.getConfiguration().graceTime) {
            return;
        }
        lastHit = System.currentTimeMillis();
        it = player2;
        participants.add(it);
        String string = plugin.getConfiguration().someoneIsIt;
        string = plugin.getConfiguration().replaceVariables(string, it);
        broadcast(string);
        string = plugin.getConfiguration().youAreIt;
        plugin.getConfiguration().sendMessage(it, string);
        resetTimer();
        cancellable.setCancelled(true);
    }

    void broadcast(String string) {
        if (it == null) {
            plugin.getLogger().warning("Game.broadcast called when no one is it");
            return;
        }
        HashSet<Player> hashSet = new HashSet<Player>(participants);
        Location location = it.getLocation();
        Location location2 = it.getLocation();
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            if (hashSet.contains(player)) continue;
            location2 = player.getLocation(location2);
            if (!location.getWorld().equals(location2.getWorld())) continue;
            int dx = Math.abs(location.getBlockX() - location2.getBlockX());
            if (dx > plugin.getConfiguration().radius) {
                continue;
            }
            int dz = Math.abs(location.getBlockZ() - location2.getBlockZ());
            if (dz > plugin.getConfiguration().radius) {
                continue;
            }
            hashSet.add(player);
        }
        for (Player player : hashSet) {
            plugin.getConfiguration().sendMessage(player, string);
            player.sendTitle("", string);
        }
    }

}
