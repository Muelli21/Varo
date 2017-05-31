package me.Varo.server.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Varo.server.main.Main;
import me.Varo.server.types.Gamestate;
import me.Varo.server.types.ServerData;

public class GamestateCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

	Player p = (Player) sender;
	ServerData sd = Main.getServerData();

	if (args.length == 1) {

	    if (!p.hasPermission("administration")) { return false; }

	    if (args[0].equalsIgnoreCase("game")) {

		sd.setGamestate(Gamestate.GAME);
		Main.getPlugin().getConfig().set("Varo.Gamestate", "game");
		Main.getPlugin().saveConfig();
		Bukkit.broadcastMessage(Main.pre + ChatColor.GREEN + "The gamestate has been changed to " + sd.getGamestate());

	    }

	    if (args[0].equalsIgnoreCase("pregame")) {

		sd.setGamestate(Gamestate.PREGAME);
		Main.getPlugin().getConfig().set("Varo.Gamestate", "pregame");
		Main.getPlugin().saveConfig();
		Bukkit.broadcastMessage(Main.pre + ChatColor.GREEN + "The gamestate has been changed to " + sd.getGamestate());
	    }

	    return false;
	}

	if (args.length > 1) {

	    p.sendMessage(Main.pre + ChatColor.RED + "Please use /gamestate");
	    return false;
	}

	if (args.length == 0) {

	    p.sendMessage(Main.pre + ChatColor.DARK_GREEN + "The current gamstate is " + sd.getGamestate());
	    return false;
	}

	return false;
    }
}
