package me.Varo.server.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import me.Varo.server.main.Main;
import me.Varo.server.types.Gamestate;
import me.Varo.server.types.PlayerData;
import me.Varo.server.types.ServerData;

public class StartCommand implements CommandExecutor {

    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

	Player p = (Player) sender;

	World world = p.getWorld();
	double x = Main.getPlugin().getConfig().getDouble("Varo.Border.spawn.x");
	double y = Main.getPlugin().getConfig().getDouble("Varo.Border.spawn.y");
	double z = Main.getPlugin().getConfig().getDouble("Varo.Border.spawn.z");
	float yaw = (float) Main.getPlugin().getConfig().getDouble("Varo.Border.spawn.yaw");
	float pitch = (float) Main.getPlugin().getConfig().getDouble("Varo.Border.spawn.pitch");

	Location loc = (new Location(world, x, y, z, yaw, pitch));

	if (!p.hasPermission("admin")) { return false; }

	if (args.length != 0) {

	    p.sendMessage(Main.pre + ChatColor.RED + " Please use /start");
	}

	ServerData sd = Main.getServerData();
	sd.setGamestate(Gamestate.START);

	p.performCommand("time set day");
	Bukkit.broadcastMessage(Main.pre + ChatColor.LIGHT_PURPLE + " The project will start in 180 seconds!");

	for (Player ps : Bukkit.getOnlinePlayers()) {

	    ps.getInventory().clear();
	    ps.getActivePotionEffects().clear();

	    World world1 = p.getWorld();
	    double x1 = Main.getPlugin().getConfig().getDouble("Varo.Players." + ps.getName().toString() + ".spawn.x");
	    double y1 = Main.getPlugin().getConfig().getDouble("Varo.Players." + ps.getName().toString() + ".spawn.y");
	    double z1 = Main.getPlugin().getConfig().getDouble("Varo.Players." + ps.getName().toString() + ".spawn.z");
	    float yaw1 = (float) Main.getPlugin().getConfig().getDouble("Varo.Players." + p.getName().toString() + ".spawn.yaw");
	    float pitch1 = (float) Main.getPlugin().getConfig().getDouble("Varo.Players." + p.getName().toString() + ".spawn.pitch");

	    Location loc1 = (new Location(world1, x1, y1, z1, yaw1, pitch1));

	    ps.teleport(loc1);
	    ps.setSaturation(20);
	    ps.setFoodLevel(20);
	}

	new BukkitRunnable() {

	    int time = 180;

	    @Override
	    public void run() {

		Main.getPlugin().reloadConfig();

		time--;

		for (Player ps : Bukkit.getOnlinePlayers()) {

		    ps.setLevel(time);
		}

		if (time == 180) {

		    Bukkit.broadcastMessage(Main.pre + ChatColor.LIGHT_PURPLE + " The project will start in " + time + " seconds!");
		}

		if (time == 120) {

		    Bukkit.broadcastMessage(Main.pre + ChatColor.LIGHT_PURPLE + " The project will start in " + time + " seconds!");
		    p.getWorld().playSound(loc, Sound.CLICK, 10, 10);
		}

		if (time == 60) {

		    Bukkit.broadcastMessage(Main.pre + ChatColor.LIGHT_PURPLE + " The project will start in " + time + " seconds!");
		    firework(p, loc, Color.ORANGE, Color.RED, Type.BALL);
		    p.getWorld().playSound(loc, Sound.CLICK, 10, 10);
		}

		if (time == 30) {

		    Bukkit.broadcastMessage(Main.pre + ChatColor.LIGHT_PURPLE + " The project will start in " + time + " seconds!");
		    firework(p, loc, Color.ORANGE, Color.RED, Type.BALL);
		    p.getWorld().playSound(loc, Sound.CLICK, 10, 10);
		}

		if (time < 15) {

		    Bukkit.broadcastMessage(Main.pre + ChatColor.LIGHT_PURPLE + " The project will start in " + time + " seconds!");
		    firework(p, loc, Color.ORANGE, Color.RED, Type.BURST);
		    p.getWorld().playSound(loc, Sound.CLICK, 10, 10);
		}

		if (time == 0) {

		    Bukkit.broadcastMessage(Main.pre + ChatColor.DARK_PURPLE + " The project successfully started. Have fun!");
		    firework(p, loc, Color.GREEN, Color.LIME, Type.BALL_LARGE);
		    p.getWorld().playSound(loc, Sound.AMBIENCE_THUNDER, 10, 10);
		    sd.setGamestate(Gamestate.GAME);

		    Main.getPlugin().getConfig().set("Varo.Gamestate", "game");
		    Main.getPlugin().getConfig().set("Varo.FirstDate", System.currentTimeMillis());
		    Main.getPlugin().saveConfig();
		    p.getWorld().setTime(3000);

		    for (PlayerData pd : PlayerData.playerdatas.values()) {

			pd.setJointime();
			pd.getPlayer().setHealth(20D);
			pd.getPlayer().setSaturation(20);
			pd.setInvulnerable(false);
			pd.getPlayer().getInventory().clear();

			for (PotionEffect effect : pd.getPlayer().getActivePotionEffects())
			    pd.getPlayer().removePotionEffect(effect.getType());
		    }

		    cancel();
		    return;
		}
	    }
	}.runTaskTimer(Main.getPlugin(), 0L, 20L);

	return false;
    }

    private void firework(Player p, Location loc, Color color, Color color2, Type type) {

	org.bukkit.entity.Firework firework = loc.getWorld().spawn(loc, Firework.class);
	FireworkEffect effect = FireworkEffect.builder().withColor(color).flicker(true).trail(true).withFade(color2).with(type).build();

	FireworkMeta meta = firework.getFireworkMeta();
	meta.addEffect(effect);
	meta.setPower(2);

	firework.setFireworkMeta(meta);
    }

}
