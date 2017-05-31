package me.Varo.server.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.Varo.server.main.Main;
import me.Varo.server.types.Gamestate;
import me.Varo.server.types.PlayerData;
import me.Varo.server.types.ServerData;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

	Player p = e.getPlayer();

	if (Main.getPlayerData(p) != null) {

	    PlayerData pd = Main.getPlayerData(p);
	    pd.setPlayer(p);
	    spawnOutsideBorder(p);
	    registerTeam(p);

	    p.sendMessage(ChatColor.GREEN + "You reloged successfully!");
	    return;
	} else {

	    PlayerData pd = new PlayerData(p);

	    registerTeam(p);
	    pd.setJointime();
	    spawnOutsideBorder(p);
	}
    }

    @EventHandler
    public void onFirstJoin(PlayerJoinEvent e) {

	Player p = e.getPlayer();
	ServerData sd = Main.getServerData();

	if (sd.getGamestate().equals(Gamestate.PREGAME)) {

	    if (Main.getPlugin().getConfig().get("Varo.Players." + p.getName() + ".spawn") == null) {

		Bukkit.broadcastMessage("Could not find the spawn for the player " + p.getName());
		return;
	    }

	    World world = Bukkit.getServer().getWorld(Main.getPlugin().getConfig().getString("Varo.Players." + p.getName().toString() + ".spawn.world"));
	    double x = Main.getPlugin().getConfig().getDouble("Varo.Players." + p.getName().toString() + ".spawn.x");
	    double y = Main.getPlugin().getConfig().getDouble("Varo.Players." + p.getName().toString() + ".spawn.y");
	    double z = Main.getPlugin().getConfig().getDouble("Varo.Players." + p.getName().toString() + ".spawn.z");
	    float yaw = (float) Main.getPlugin().getConfig().getDouble("Varo.Players." + p.getName().toString() + ".spawn.yaw");
	    float pitch = (float) Main.getPlugin().getConfig().getDouble("Varo.Players." + p.getName().toString() + ".spawn.pitch");

	    Location loc = (new Location(world, x, y, z, yaw, pitch));

	    p.teleport(loc);
	    p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 200));

	} else if (sd.getGamestate().equals(Gamestate.GAME)) {

	    return;

	} else if (sd.getGamestate().equals(Gamestate.START)) { return; }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {

	final Player p = e.getPlayer();
	final PlayerData pd = Main.getPlayerData(p);
	ServerData sd = Main.getServerData();

	if (!sd.getGamestate().equals(Gamestate.GAME)) { return; }

	if (Main.getPlugin().getConfig().getString("Varo.Players." + p.getName()) == null) { return; }

	if (Main.getPlugin().getConfig().getString("Varo.Players." + p.getName() + ".allowedtojoin").equals("dead")) { return; }

	String name = p.getName();

	new BukkitRunnable() {

	    @Override
	    public void run() {

		if (p.isOnline()) { return; }

		Main.getPlugin().getConfig().set("Varo.Players." + name + ".allowedtojoin", "false");
		Main.getPlugin().saveConfig();

		if (pd != null) {
		    pd.delete();
		}
	    }
	}.runTaskLater(Main.getPlugin(), 60 * 20L);
    }

    @EventHandler
    public void onPreJoin(AsyncPlayerPreLoginEvent e) {

	if (Main.getPlugin().getConfig().getString("Varo.Players." + e.getName() + ".allowedtojoin") == null) {

	    e.disallow(Result.KICK_OTHER, ChatColor.RED + "You are not registered for this event!");
	    return;
	}

	if (Main.getPlugin().getConfig().getString("Varo.Players." + e.getName() + ".allowedtojoin").equals("false")) {

	    e.disallow(Result.KICK_OTHER, ChatColor.GREEN + "You are not allowed to join today. Please come back tomorrow!");
	    return;
	}

	if (Main.getPlugin().getConfig().getString("Varo.Players." + e.getName() + ".allowedtojoin").equals("dead")) {

	    e.disallow(Result.KICK_OTHER, ChatColor.RED + "You are dead, you can not join the game anymore!");
	    return;
	}

    }

    public void outsideBorder(Player p) {

	World world = Bukkit.getServer().getWorld(Main.getPlugin().getConfig().getString("Varo.Border.spawn.world"));
	double x = Main.getPlugin().getConfig().getDouble("Varo.Border.spawn.x");
	double y = Main.getPlugin().getConfig().getDouble("Varo.Border.spawn.y");
	double z = Main.getPlugin().getConfig().getDouble("Varo.Border.spawn.z");
	float yaw = (float) Main.getPlugin().getConfig().getDouble("Varo.Border.spawn.yaw");
	float pitch = (float) Main.getPlugin().getConfig().getDouble("Varo.Border.spawn.pitch");

	Location loc = (new Location(world, x, y, z, yaw, pitch));
	p.teleport(loc);
	p.sendMessage(ChatColor.DARK_GREEN + "You have been teleported to the spawn, because your spawn was outside the border!");
    }

    public void spawnOutsideBorder(Player p) {

	if (p.getWorld().getName().equals("world_nether")) { return; }

	int border = Main.getPlugin().getConfig().getInt("Varo.Border.size");

	double x = Main.getPlugin().getConfig().getDouble("Varo.Border.spawn.x");
	double y = Main.getPlugin().getConfig().getDouble("Varo.Border.spawn.y");
	double z = Main.getPlugin().getConfig().getDouble("Varo.Border.spawn.z");
	String worldname = Main.getPlugin().getConfig().getString("Varo.Border.spawn.world");

	World world = Bukkit.getWorld(worldname);
	Location spawn = new Location(world, x, y, z);

	if (p.getLocation().distance(spawn) > border) {

	    outsideBorder(p);
	}
    }

    public void registerTeam(Player p) {

	PlayerData pd = Main.getPlayerData(p);
	ServerData sd = Main.getServerData();

	// register Team
	if (Main.getPlugin().getConfig().getString("Varo.Players." + p.getName() + ".team") != null) {

	    String teamname = Main.getPlugin().getConfig().getString("Varo.Players." + p.getName() + ".team");
	    pd.setTeam(teamname);

	    p.setGameMode(GameMode.SURVIVAL);

	    if (sd.getGamestate().equals(Gamestate.GAME)) {

		new BukkitRunnable() {

		    int invulnerable = 15;

		    @Override
		    public void run() {

			invulnerable--;

			if (!p.isOnline()) {

			    cancel();
			}

			if (invulnerable < 5) {

			    Bukkit.broadcastMessage(Main.pre + ChatColor.DARK_GREEN + p.getName() + " will be ready to fight in " + invulnerable + " seconds!");
			    p.getWorld().playSound(p.getLocation(), Sound.CLICK, 10, 10);
			}

			if (invulnerable == 0) {

			    p.getWorld().playSound(p.getLocation(), Sound.AMBIENCE_THUNDER, 10, 10);
			    pd.setInvulnerable(false);
			    cancel();
			}
		    }
		}.runTaskTimer(Main.getPlugin(), 0L, 20L);
	    }
	}

	if (Main.getPlugin().getConfig().getString("Varo.Players." + p.getName() + ".team") == null) {

	    char t1 = p.getName().toString().charAt(0);
	    char t2 = p.getName().toString().charAt(1);
	    char t3 = p.getName().toString().charAt(2);
	    char t4 = p.getName().toString().charAt(3);

	    String team1 = "" + t1;
	    String team2 = "" + t2;
	    String team3 = "" + t3;
	    String team4 = "" + t4;
	    String team = team1 + team2 + team3 + team4;

	    pd.setTeam(team);
	    p.setGameMode(GameMode.SURVIVAL);

	    if (sd.getGamestate().equals(Gamestate.GAME)) {

		new BukkitRunnable() {

		    int invulnerable = 15;

		    @Override
		    public void run() {

			invulnerable--;

			if (!p.isOnline()) {

			    cancel();
			}

			if (invulnerable < 5) {

			    Bukkit.broadcastMessage(Main.pre + ChatColor.DARK_GREEN + "The Player " + p.getName() + " will be ready to fight in " + invulnerable + " seconds!");
			    p.getWorld().playSound(p.getLocation(), Sound.CLICK, 10, 10);
			}

			if (invulnerable == 0) {

			    p.getWorld().playSound(p.getLocation(), Sound.AMBIENCE_THUNDER, 10, 10);
			    pd.setInvulnerable(false);
			    cancel();
			}
		    }
		}.runTaskTimer(Main.getPlugin(), 0L, 20L);
	    }
	}

    }
}
