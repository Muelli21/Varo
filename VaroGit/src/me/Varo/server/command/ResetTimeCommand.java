package me.Varo.server.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.Varo.server.main.Main;
import me.Varo.server.types.Gamestate;
import me.Varo.server.types.ServerData;

public class ResetTimeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

	ServerData sd = Main.getServerData();

	if (!sd.getGamestate().equals(Gamestate.GAME)) {

	    sender.sendMessage(Main.pre + ChatColor.RED + "You can only use this command if the game has already been started!");
	    return false;
	}

	if (args.length == 0) {

	    if (Main.getPlugin().getConfig().get("Varo.FirstDate") == null) {

		System.out.print("FirstDate nicht gefunden!");
		return false;
	    }

	    long firstdate = Main.getPlugin().getConfig().getLong("Varo.FirstDate");

	    long milis = 24 * 60 * 60 * 1000 - (System.currentTimeMillis() - firstdate);

	    long seconds = (milis / 1000) % 60;
	    long minutes = (milis / (1000 * 60)) % 60;
	    long hours = (milis / (1000 * 60 * 60)) % 24;

	    sender.sendMessage(Main.pre + ChatColor.GOLD + "The next timereset will be in " + hours + ":" + minutes + ":" + seconds + " !");
	}
	return false;
    }
}
