package me.Varo.server.main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.Varo.server.command.BuildCommand;
import me.Varo.server.command.GamestateCommand;
import me.Varo.server.command.PlayerStatusCommand;
import me.Varo.server.command.RegisterCommand;
import me.Varo.server.command.ResetTimeCommand;
import me.Varo.server.command.StartCommand;
import me.Varo.server.command.TimeLeftCommand;
import me.Varo.server.listener.BuildListener;
import me.Varo.server.listener.DamageListener;
import me.Varo.server.listener.DeathListener;
import me.Varo.server.listener.JoinListener;
import me.Varo.server.listener.PlayerListener;
import me.Varo.server.types.Gamestate;
import me.Varo.server.types.PlayerData;
import me.Varo.server.types.ServerData;

public class Main extends JavaPlugin implements Listener {

    public static Main plugin;

    public static Main getPlugin() {
	return plugin;
    }

    public static final int timetoplay = 30;

    public static final String pre = "[TheProject] ";

    public void onEnable() {

	plugin = this;

	System.out.print(ChatColor.RED + pre + " has been enabled!");

	loadListeners();
	loadCommands();
	loadPlayerDatas();
	loadServerData();
	loadPlayerCheck();
	loadGamestate();

    }

    private void loadPlayerCheck() {

	ServerData sd = Main.getServerData();

	new BukkitRunnable() {

	    @Override
	    public void run() {

		Main.getPlugin().reloadConfig();

		if (!sd.getGamestate().equals(Gamestate.GAME)) { return; }

		// Check to allow the players to join each day
		if (getConfig().get("Varo.FirstDate") == null) {

		    System.out.print("FirstDate nicht gefunden!");
		    return;
		}

		if (getConfig().getConfigurationSection("Varo.Players") == null) {
		    System.out.print("Keine Player gefunden!");
		    return;
		}

		long firstdate = getConfig().getLong("Varo.FirstDate");

		if (System.currentTimeMillis() - firstdate > (24 * 60 * 60 * 1000)) {

		    for (String s : getConfig().getConfigurationSection("Varo.Players").getKeys(false)) {

			if (getConfig().getString("Varo.Players." + s + ".allowedtojoin").equals("dead")) {

			    Bukkit.broadcastMessage(ChatColor.RED + "The player" + s + "is dead");
			    continue;
			}

			getConfig().set("Varo.Players." + s + ".allowedtojoin", "true");
			saveConfig();

			Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "The player " + s + " is now able to join again!");
		    }

		    Main.getPlugin().getConfig().set("Varo.FirstDate", System.currentTimeMillis());
		    Main.getPlugin().saveConfig();

		}

		// Kicks the player after some Minutes of played time
		for (PlayerData pds : PlayerData.playerdatas.values()) {

		    if (!pds.getPlayer().isOnline()) {
			continue;
		    }

		    if (getConfig().getString("Varo.Players." + pds.getPlayer().getName().toString() + ".allowedtojoin").equals("dead")) {
			continue;
		    }

		    if (System.currentTimeMillis() - pds.getJointime() > (timetoplay - 0.25) * 60 * 1000) {

			Bukkit.broadcastMessage(Main.pre + ChatColor.DARK_RED + "The player " + pds.getPlayer().getName() + " will be kicked in a few seconds!");
		    }

		    if (System.currentTimeMillis() - pds.getJointime() > (timetoplay) * 60 * 1000) {

			if ((System.currentTimeMillis() - pds.getLastDamage()) < 30 * 1000) {

			    pds.getPlayer().sendMessage(pre + ChatColor.RED + "You could not be kicked from the server because you are in a fight!");

			} else if ((System.currentTimeMillis() - pds.getLastDamage()) > 30 * 1000) {

			    Bukkit.broadcastMessage(Main.pre + ChatColor.DARK_RED + "The player " + pds.getPlayer().getName() + " has been kicked from the server!");
			    pds.getPlayer().kickPlayer("Your time is over! Please join tomorrow!");
			    getConfig().set("Varo.Players." + pds.getPlayer().getName() + ".allowedtojoin", "false");
			    saveConfig();
			    pds.delete();
			    continue;
			}
		    }
		}
	    }
	}.runTaskTimer(Main.getPlugin(), 20L, 5 * 20L);

    }

    public void onDisable() {

	System.out.print(ChatColor.RED + pre + " has been disabled!");
    }

    private void loadCommands() {

	getCommand("register").setExecutor(new RegisterCommand());
	getCommand("start").setExecutor(new StartCommand());
	getCommand("timeleft").setExecutor(new TimeLeftCommand());
	getCommand("gamestate").setExecutor(new GamestateCommand());
	getCommand("build").setExecutor(new BuildCommand());
	getCommand("timetoreset").setExecutor(new ResetTimeCommand());
	getCommand("playerstatus").setExecutor(new PlayerStatusCommand());
    }

    private void loadListeners() {
	PluginManager pm = Bukkit.getPluginManager();

	pm.registerEvents(new JoinListener(), this);
	pm.registerEvents(new DeathListener(), this);
	pm.registerEvents(new PlayerListener(), this);
	pm.registerEvents(new BuildListener(), this);
	pm.registerEvents(new DamageListener(), this);

    }

    @SuppressWarnings("deprecation")
    private void loadPlayerDatas() {
	for (Player ps : Bukkit.getOnlinePlayers()) {

	    PlayerData pd = new PlayerData(ps);

	    if (Main.getPlugin().getConfig().getString("Varo.Players." + pd.getPlayer().getName() + ".team") != null) {

		String teamname = Main.getPlugin().getConfig().getString("Varo.Players." + pd.getPlayer().getName() + ".team");
		pd.setTeam(teamname);

	    }

	    if (Main.getPlugin().getConfig().getString("Varo.Players." + pd.getPlayer().getName() + ".team") == null) {

		char t1 = pd.getPlayer().getName().toString().charAt(0);
		char t2 = pd.getPlayer().toString().charAt(1);
		char t3 = pd.getPlayer().toString().charAt(2);
		char t4 = pd.getPlayer().toString().charAt(3);

		String team1 = "" + t1;
		String team2 = "" + t2;
		String team3 = "" + t3;
		String team4 = "" + t4;

		String teamname = team1 + team2 + team3 + team4;
		pd.setTeam(teamname);
	    }
	}
    }

    public static PlayerData getPlayerData(Player p) {

	PlayerData pd = PlayerData.playerdatas.get(p);
	return pd;
    }

    private void loadServerData() {

	new ServerData();
    }

    private void loadGamestate() {

	ServerData sd = Main.getServerData();

	if (getConfig().getString("Varo.Gamestate") == null) { return; }

	if (getConfig().getString("Varo.Gamestate").equals("game")) {

	    sd.setGamestate(Gamestate.GAME);
	} else {

	    sd.setGamestate(Gamestate.PREGAME);
	}
    }

    public static ServerData getServerData() {

	ServerData sd = ServerData.sd;
	return sd;
    }
}
