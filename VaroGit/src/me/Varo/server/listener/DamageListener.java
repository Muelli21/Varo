package me.Varo.server.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import me.Varo.server.main.Main;
import me.Varo.server.types.PlayerData;

public class DamageListener implements Listener {

    @EventHandler
    public void onInvulnerable(EntityDamageEvent e) {

	if (!(e.getEntity() instanceof Player)) { return; }

	Player p = (Player) e.getEntity();
	PlayerData pd = Main.getPlayerData(p);

	if (pd.isInvulnerable())
	    e.setCancelled(true);

    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {

	if (!(e.getDamager() instanceof Player)) { return; }
	if (!(e.getEntity() instanceof Player)) { return; }

	Player p = (Player) e.getDamager();
	Player target = (Player) e.getEntity();

	PlayerData pd = Main.getPlayerData(p);
	PlayerData td = Main.getPlayerData(target);

	if (pd.getTeam().toString().equals(td.getTeam().toString())) { return; }

	td.setLastDamage();

    }

}
