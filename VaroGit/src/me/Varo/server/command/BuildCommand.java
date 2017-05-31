package me.Varo.server.command;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Varo.server.main.Main;
import me.Varo.server.types.PlayerData;

public class BuildCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

	if (!(sender instanceof Player)) {

	    sender.sendMessage("You can not build!");
	    return false;
	}

	Player p = (Player) sender;
	PlayerData pd = Main.getPlayerData(p);

	if (!p.hasPermission("administration")) {

	    p.sendMessage(ChatColor.RED + "You do not have the permissions to do this!");
	    return false;
	}

	if (args.length == 0) {

	    if (!pd.isAbletobuild()) {

		pd.setAbletobuild(true);
		p.setGameMode(GameMode.CREATIVE);
		p.sendMessage(ChatColor.GREEN + "Now you are able to build!");
		return false;
	    }

	    if (pd.isAbletobuild()) {

		pd.setAbletobuild(false);
		p.setGameMode(GameMode.SURVIVAL);
		p.sendMessage(ChatColor.RED + "You are not able to build anymore!");
		return false;
	    }
	}

	return false;
    }

}
