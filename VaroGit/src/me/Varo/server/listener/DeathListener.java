package me.Varo.server.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.Varo.server.main.Main;

public class DeathListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {

	Player p = e.getEntity();

	Main.getPlugin().getConfig().set("Varo.Players." + p.getName() + ".allowedtojoin", "dead");
	Main.getPlugin().saveConfig();

	p.getWorld().strikeLightning(p.getLocation());

	p.getWorld().getBlockAt(p.getLocation()).setType(Material.CHEST);
	p.getWorld().getBlockAt(p.getLocation().clone().add(1, 0, 0)).setType(Material.CHEST);
	Chest c = (Chest) p.getLocation().getBlock().getState();
	Chest c1 = (Chest) p.getLocation().clone().add(1, 0, 0).getBlock().getState();
	Inventory chest = c.getBlockInventory();
	Inventory chest1 = c1.getBlockInventory();

	for (ItemStack item : p.getInventory().getContents()) {

	    if (item == null)
		continue;

	    chest.addItem(item);
	}

	for (ItemStack item : p.getInventory().getArmorContents()) {

	    if (item == null)
		continue;

	    chest1.addItem(item);
	}

	p.kickPlayer(ChatColor.RED + "You are dead, you can not join the game anymore!");
    }

    @EventHandler
    public void onSheepDeath(EntityDeathEvent e) {

	if (Bukkit.getServer().getVersion().toString().contains("1.8")) { return; }
	if (!(e.getEntity() instanceof Sheep)) { return; }

	Entity entity = e.getEntity();
	entity.getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(Material.PORK, 2));
    }

}
