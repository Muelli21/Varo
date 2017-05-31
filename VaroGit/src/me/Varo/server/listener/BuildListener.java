package me.Varo.server.listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import me.Varo.server.main.Main;
import me.Varo.server.types.Gamestate;
import me.Varo.server.types.PlayerData;
import me.Varo.server.types.ServerData;

public class BuildListener implements Listener {

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {

	Player p = e.getPlayer();
	PlayerData pd = Main.getPlayerData(p);
	ServerData sd = Main.getServerData();

	if (pd.isAbletobuild()) { return; }

	if ((sd.getGamestate().equals(Gamestate.PREGAME) || (sd.getGamestate().equals(Gamestate.START)))) {

	    e.setCancelled(true);
	    p.sendMessage(Main.pre + ChatColor.RED + "The gamestate is " + sd.getGamestate() + ", which means you are not allowed to build!");
	}
    }

    @EventHandler
    public void onPlace(BlockBreakEvent e) {

	Player p = e.getPlayer();
	PlayerData pd = Main.getPlayerData(p);
	ServerData sd = Main.getServerData();

	if (pd.isAbletobuild()) { return; }

	if ((sd.getGamestate().equals(Gamestate.PREGAME) || (sd.getGamestate().equals(Gamestate.START)))) {

	    e.setCancelled(true);
	    p.sendMessage(Main.pre + ChatColor.RED + "The gamestate is " + sd.getGamestate() + ", which means you are not allowed to build!");
	}
    }
}
