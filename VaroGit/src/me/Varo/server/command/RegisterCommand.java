package me.Varo.server.command;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.schematic.MCEditSchematicFormat;
import com.sk89q.worldedit.world.DataException;

import me.Varo.server.main.Main;

public class RegisterCommand implements CommandExecutor {

    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

	Player p = (Player) sender;

	if (!p.hasPermission("admin")) { return false; }

	if (args.length == 1) {

	    if (args[0].equalsIgnoreCase("border")) {

		Main.getPlugin().getConfig().getStringList("Varo.Border");
		Main.getPlugin().getConfig().set("Varo.Border.size", 1000);
		Main.getPlugin().getConfig().set("Varo.Border.spawn.world", p.getLocation().getWorld().getName());
		Main.getPlugin().getConfig().set("Varo.Border.spawn.x", p.getLocation().getX());
		Main.getPlugin().getConfig().set("Varo.Border.spawn.y", p.getLocation().getY());
		Main.getPlugin().getConfig().set("Varo.Border.spawn.z", p.getLocation().getZ());
		Main.getPlugin().getConfig().set("Varo.Border.spawn.yaw", p.getLocation().getYaw());
		Main.getPlugin().getConfig().set("Varo.Border.spawn.pitch", p.getLocation().getPitch());
		Main.getPlugin().saveConfig();

		p.sendMessage(Main.pre + ChatColor.GREEN + "Successfully registered the spawn for the border!");

		return false;
	    }

	    if (args[0].equalsIgnoreCase("spawn")) {

		if (!new File("plugins/Varo/spawn.schematic").exists()) {

		    p.sendMessage(Main.pre + ChatColor.RED + "The file for the Spawn does not exist!");
		    return false;
		}

		p.sendMessage(Main.pre + ChatColor.DARK_GREEN + "Successfully loaded spawn structures!");

		Location loc = p.getLocation();
		WorldEditPlugin we = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
		File schematic = new File("plugins/Varo/spawn.schematic");
		EditSession session = we.getWorldEdit().getEditSessionFactory().getEditSession(new BukkitWorld(loc.getWorld()), 1000000);

		try {
		    MCEditSchematicFormat.getFormat(schematic).load(schematic).paste(session, new Vector(0, 200, 0), false);
		    p.sendMessage(Main.pre + ChatColor.GREEN + "Successfully set the spawn structures!");
		} catch (MaxChangedBlocksException | DataException | IOException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}

		return false;
	    }
	}

	if (args.length == 3) {

	    if (!args[0].equalsIgnoreCase("player")) { return false; }

	    Main.getPlugin().getConfig().set("Varo.Players." + args[1] + ".team", args[2]);
	    Main.getPlugin().getConfig().set("Varo.Players." + args[1] + ".allowedtojoin", "true");
	    Main.getPlugin().getConfig().set("Varo.Players." + args[1] + ".spawn.world", p.getLocation().getWorld().getName());
	    Main.getPlugin().getConfig().set("Varo.Players." + args[1] + ".spawn.x", p.getLocation().getX());
	    Main.getPlugin().getConfig().set("Varo.Players." + args[1] + ".spawn.y", p.getLocation().getY());
	    Main.getPlugin().getConfig().set("Varo.Players." + args[1] + ".spawn.z", p.getLocation().getZ());
	    Main.getPlugin().getConfig().set("Varo.Players." + args[1] + ".spawn.yaw", p.getLocation().getYaw());
	    Main.getPlugin().getConfig().set("Varo.Players." + args[1] + ".spawn.pitch", p.getLocation().getPitch());
	    Main.getPlugin().saveConfig();

	    p.sendMessage(Main.pre + ChatColor.GREEN + "Successfully registered the player " + args[1] + "and set him into the team " + args[2] + "!");
	}

	return false;
    }
}
