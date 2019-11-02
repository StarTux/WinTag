package com.winthier.tag;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class WinTagPlugin extends JavaPlugin {
    private Game game;
    private PlayerListener playerListener = new PlayerListener(this);
    private Configuration configuration;

    @Override
    public void onEnable() {
        loadConfiguration();
        playerListener.onEnable();
    }

    @Override
    public void onDisable() {
    }

    void startGame(Player player) {
        game = new Game(this);
        game.start(player);
    }

    void stopGame() {
        game.stop();
        game = null;
    }

    Game getGame() {
        return game;
    }

    Configuration getConfiguration() {
        return configuration;
    }

    void loadConfiguration() {
        reloadConfig();
        configuration = new Configuration(this);
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    boolean checkPermission(CommandSender sender, String string) {
        if (!sender.hasPermission(string)) {
            configuration.sendMessage(sender, "" + ChatColor.RED
                                      + "You don't have permission!");
            return false;
        }
        return true;
    }

    void msg(CommandSender sender, String msg) {
        msg = ChatColor.translateAlternateColorCodes('&', msg);
        sender.sendMessage(msg);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command,
                             String string, String[] arrstring) {
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }
        if (arrstring.length == 0) {
            msg(sender, "&8Usage: &f/tag");
            if (game != null && game.getIt() != null) {
                msg(sender, ("&b" + game.getIt().getName() + "&7 is it!"));
            }
            if (sender.hasPermission("wintag.start")) {
                msg(sender, "&b/tag start&7 - start a game of tag");
            }
            if (sender.hasPermission("wintag.stop")) {
                msg(sender, "&b/tag stop&7 - Stop the current game");
            }
            if (sender.hasPermission("wintag.reload")) {
                msg(sender, "&b/tag reload&7 - Reload configuration");
            }
            return true;
        }
        if (arrstring.length == 1 && arrstring[0].equals("start")) {
            if (!checkPermission(sender, "wintag.start")) {
                return true;
            }
            if (player == null) {
                sender.sendMessage("Player expected!");
                return true;
            }
            if (game != null) {
                String string2 = configuration.alreadyRunning;
                configuration.sendMessage(player, string2);
                return true;
            }
            startGame(player);
            return true;
        }
        if (arrstring.length == 1 && arrstring[0].equals("stop")) {
            if (!checkPermission(sender, "wintag.stop")) {
                return true;
            }
            if (game == null) {
                String string3 = configuration.noGameRunning;
                configuration.sendMessage(sender, string3);
                return true;
            }
            stopGame();
            return true;
        }
        if (arrstring.length == 1 && arrstring[0].equals("reload")) {
            if (!checkPermission(sender, "wintag.reload")) {
                return true;
            }
            loadConfiguration();
            String string4 = configuration.configReloaded;
            configuration.sendMessage(sender, string4);
            return true;
        }
        return false;
    }
}
