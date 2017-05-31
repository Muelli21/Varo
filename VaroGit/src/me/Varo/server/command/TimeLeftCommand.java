package me.Varo.server.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Varo.server.main.Main;
import me.Varo.server.types.Gamestate;
import me.Varo.server.types.PlayerData;
import me.Varo.server.types.ServerData;

public class TimeLeftCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

	Player p = (Player) sender;
	PlayerData pd = Main.getPlayerData(p);
	ServerData sd = Main.getServerData();

	long time = (System.currentTimeMillis() - pd.getJointime());
	long milis = ((Main.timetoplay * 60 * 1000) - time);
	long seconds = (milis / 1000) % 60;
	long minutes = (milis / (1000 * 60)) % 60;
	long hours = (milis / (1000 * 60 * 60)) % 24;

	if (!sd.getGamestate().equals(Gamestate.GAME)) {

	    p.sendMessage(Main.pre + ChatColor.RED + "You can only use this command if the game has already been started!");
	    return false;
	}

	if (args.length == 0) {
	    p.sendMessage(Main.pre + ChatColor.GREEN + "You still have " + hours + ":" + minutes + ":" + seconds + " left to play!");
	}

	if (args.length != 0) {
	    p.sendMessage(Main.pre + ChatColor.RED + "Please use /timeleft");
	}

	return false;
    }
}
