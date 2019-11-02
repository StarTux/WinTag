package com.winthier.tag;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

final class Configuration {
    final int radius;
    final long graceTime;
    final long gameTime;
    final String prefix;
    final String youAreIt;
    final String someoneIsIt;
    final String gameStart;
    final String gameStop;
    final String youLost;
    final String alreadyRunning;
    final String noGameRunning;
    final String configReloaded;

    Configuration(final WinTagPlugin winTagPlugin) {
        this.radius = winTagPlugin.getConfig().getInt("Radius");
        this.graceTime = winTagPlugin.getConfig().getLong("GraceTime") * 1000L;
        this.gameTime = winTagPlugin.getConfig().getLong("GameTime");
        this.prefix = this.getColoredMessage(winTagPlugin, "Prefix");
        this.youAreIt = this.getColoredMessage(winTagPlugin, "YouAreIt");
        this.someoneIsIt = this.getColoredMessage(winTagPlugin, "SomeoneIsIt");
        this.gameStart = this.getColoredMessage(winTagPlugin, "GameStart");
        this.gameStop = this.getColoredMessage(winTagPlugin, "GameStop");
        this.youLost = this.getColoredMessage(winTagPlugin, "YouLost");
        this.alreadyRunning = this.getColoredMessage(winTagPlugin, "AlreadyRunning");
        this.noGameRunning = this.getColoredMessage(winTagPlugin, "NoGameRunning");
        this.configReloaded = this.getColoredMessage(winTagPlugin, "ConfigReloaded");
    }

    private String getColoredMessage(WinTagPlugin winTagPlugin, String string) {
        String msg = winTagPlugin.getConfig().getString("messages." + string);
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    void sendMessage(CommandSender commandSender, String string) {
        commandSender.sendMessage(this.prefix + string);
    }

    String replaceVariables(String string, Player player) {
        return string.replace("{player}", player.getName());
    }
}

