package me.Varo.server.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.Varo.server.main.Main;

public class PlayerStatusCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

	if (!sender.hasPermission("administration")) { return false; }

	if (args.length == 0) {

	    for (String s : Main.getPlugin().getConfig().getConfigurationSection("Varo.Players").getKeys(false)) {

		if (Main.getPlugin().getConfig().getString("Varo.Players." + s + ".allowedtojoin").equals("dead")) {

		    Bukkit.broadcastMessage(ChatColor.RED + "The player" + s + " is dead");
		    continue;
		}

		if (Main.getPlugin().getConfig().getString("Varo.Players." + s + ".allowedtojoin").equals("true")) {

		    Bukkit.broadcastMessage(ChatColor.GREEN + "The player" + s + " is allowed to join");
		    continue;
		}

		if (Main.getPlugin().getConfig().getString("Varo.Players." + s + ".allowedtojoin").equals("false")) {

		    Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "The player" + s + " is not allowed to join");
		    continue;
		}
	    }

	} else

	if (args.length == 1) {

	    if (Main.getPlugin().getConfig().getString("Varo.Players." + args[0] + ".allowedtojoin") == null) {

		sender.sendMessage("This Player is not registered for this event");
		return false;
	    }

	    if (Main.getPlugin().getConfig().getString("Varo.Players." + args[0] + ".allowedtojoin").equals("dead")) {

		Bukkit.broadcastMessage(ChatColor.RED + "The player" + args[0] + " is dead");
		return false;
	    }

	    if (Main.getPlugin().getConfig().getString("Varo.Players." + args[0] + ".allowedtojoin").equals("true")) {

		Bukkit.broadcastMessage(ChatColor.GREEN + "The player" + args[0] + " is allowed to join");
		return false;
	    }

	    if (Main.getPlugin().getConfig().getString("Varo.Players." + args[0] + ".allowedtojoin").equals("false")) {

		Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "The player" + args[0] + " is not allowed to join");
		return false;
	    }

	} else {

	    sender.sendMessage(ChatColor.RED + "Please use /playerstatus or /playerstatus [name]");
	}

	return false;
    }
}
