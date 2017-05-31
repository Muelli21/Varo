package me.Varo.server.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import me.Varo.server.main.Main;
import me.Varo.server.types.Gamestate;
import me.Varo.server.types.PlayerData;
import me.Varo.server.types.ServerData;

public class PlayerListener implements Listener {

    @EventHandler
    public void onBorder(PlayerMoveEvent e) {

	int border = Main.getPlugin().getConfig().getInt("Varo.Border.size");

	Player p = e.getPlayer();

	if (p.getWorld().getName().equals("world_nether")) { return; }

	double x = Main.getPlugin().getConfig().getDouble("Varo.Border.spawn.x");
	double y = Main.getPlugin().getConfig().getDouble("Varo.Border.spawn.y");
	double z = Main.getPlugin().getConfig().getDouble("Varo.Border.spawn.z");
	String worldname = Main.getPlugin().getConfig().getString("Varo.Border.spawn.world");

	World world = Bukkit.getWorld(worldname);
	Location spawn = new Location(world, x, y, z);

	if (p.getLocation().distance(spawn) > border) {

	    onBox(p);
	}
    }

    @EventHandler
    public void nearbyBorder(PlayerMoveEvent e) {

	Player p = e.getPlayer();

	if (p.getWorld().getName().equals("world_nether")) { return; }

	int border = Main.getPlugin().getConfig().getInt("Varo.Border.size");

	double x = Main.getPlugin().getConfig().getDouble("Varo.Border.spawn.x");
	double y = Main.getPlugin().getConfig().getDouble("Varo.Border.spawn.y");
	double z = Main.getPlugin().getConfig().getDouble("Varo.Border.spawn.z");
	String worldname = Main.getPlugin().getConfig().getString("Varo.Border.spawn.world");

	World world = Bukkit.getWorld(worldname);
	Location spawn = new Location(world, x, y, z);

	if (p.getLocation().distance(spawn) > border - 10) {

	    nearbyBox(p);
	}

    }

    @EventHandler
    public void outsideBorder(PlayerMoveEvent e) {

	int border = Main.getPlugin().getConfig().getInt("Varo.Border.size");

	Player p = e.getPlayer();

	if (p.getWorld().getName().equals("world_nether")) { return; }

	double x = Main.getPlugin().getConfig().getDouble("Varo.Border.spawn.x");
	double y = Main.getPlugin().getConfig().getDouble("Varo.Border.spawn.y");
	double z = Main.getPlugin().getConfig().getDouble("Varo.Border.spawn.z");
	String worldname = Main.getPlugin().getConfig().getString("Varo.Border.spawn.world");

	World world = Bukkit.getWorld(worldname);
	Location spawn = new Location(world, x, y, z);

	if (p.getLocation().distance(spawn) > border + 20) {

	    outsideBorder(p);
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
	p.setVelocity(new Vector(0, 0, 0));
	p.sendMessage(ChatColor.DARK_GREEN + "You have been teleported to the spawn, because your spawn was outside the border!");
    }

    private void onBox(final Player p) {

	double x3 = Main.getPlugin().getConfig().getDouble("Varo.Border.spawn.x");
	double y3 = Main.getPlugin().getConfig().getDouble("Varo.Border.spawn.y");
	double z3 = Main.getPlugin().getConfig().getDouble("Varo.Border.spawn.z");

	double x = x3;
	double y = y3;
	double z = z3;

	double x1 = p.getLocation().getX();
	double y1 = p.getLocation().getY();
	double z1 = p.getLocation().getZ();

	Vector handle = new Vector((x1) - (x), (y1) - (y), (z1) - (z));
	Vector push = handle.normalize();

	push.multiply(-2);
	push.setY(0.5);

	p.setVelocity(push);
	p.sendMessage(Main.pre + ChatColor.RED + "You walked out of the border!");

    }

    private void nearbyBox(final Player p) {

	p.sendMessage(Main.pre + ChatColor.RED + "The Border is nearby");
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {

	Player p = e.getPlayer();
	PlayerData pd = Main.getPlayerData(p);

	if (pd.getTeam().toString().equals("null")) { return; }

	e.setMessage(ChatColor.GOLD + pd.getTeam() + " " + ChatColor.RESET + e.getMessage());
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent e) {

	ServerData sd = Main.getServerData();

	if (!(e.getEntity() instanceof Player)) { return; }

	if (!sd.getGamestate().equals(Gamestate.GAME)) {

	    Player p = (Player) e.getEntity();
	    e.setFoodLevel(20);
	    p.setSaturation(20);
	}
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e) {

	Player p = e.getPlayer();

	if (e.getFrom().getWorldFolder().getName().equals("world_nether")) {

	    World world = Bukkit.getServer().getWorld(Main.getPlugin().getConfig().getString("Varo.Border.spawn.world"));
	    double x = Main.getPlugin().getConfig().getDouble("Varo.Border.spawn.x");
	    double y = Main.getPlugin().getConfig().getDouble("Varo.Border.spawn.y");
	    double z = Main.getPlugin().getConfig().getDouble("Varo.Border.spawn.z");
	    float yaw = (float) Main.getPlugin().getConfig().getDouble("Varo.Border.spawn.yaw");
	    float pitch = (float) Main.getPlugin().getConfig().getDouble("Varo.Border.spawn.pitch");

	    Location loc = (new Location(world, x, y, z, yaw, pitch));
	    p.teleport(loc);
	}
    }

    @EventHandler
    public void onItemCraft(CraftItemEvent e) {

	Player p = (Player) e.getWhoClicked();
	ItemStack item = e.getCurrentItem();

	if (item.getType() == Material.GOLDEN_APPLE && item.getDurability() == 1) {

	    Bukkit.broadcastMessage(Main.pre + "The player " + ChatColor.DARK_RED + p.getName() + ChatColor.RESET + " tried to craft an OPApple.");
	    Bukkit.broadcastMessage("-------------------------");
	    Bukkit.broadcastMessage("His cords are: " + (int) p.getLocation().getX() + " " + (int) p.getLocation().getY() + " " + (int) p.getLocation().getZ());
	    p.closeInventory();
	    e.setCancelled(true);
	}
    }

    @EventHandler
    public void OnPotion(InventoryClickEvent e) {

	if (e.getClickedInventory() == null) { return; }

	if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) { return; }

	Player p = (Player) e.getWhoClicked();
	ItemStack item = e.getCurrentItem();

	if ((item.getType() == Material.POTION && item.getDurability() == 8233) || (item.getType() == Material.POTION && item.getDurability() == 8201)
		|| (item.getType() == Material.POTION && item.getDurability() == 16393) || (item.getType() == Material.POTION && item.getDurability() == 16425)) {

	    p.getInventory().remove(item);
	    e.getInventory().remove(item);

	    Bukkit.broadcastMessage(Main.pre + "The player " + ChatColor.DARK_RED + p.getName() + ChatColor.RESET + " tried to craft a Potion which is not allowed.");
	    Bukkit.broadcastMessage("-------------------------");
	    Bukkit.broadcastMessage("His cords are: " + (int) p.getLocation().getX() + " " + (int) p.getLocation().getY() + " " + (int) p.getLocation().getZ());

	    p.closeInventory();
	    e.setCancelled(true);
	}
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {

	if (!(e.getDamager() instanceof Player)) { return; }

	if (!(e.getEntity() instanceof Player)) { return; }

	Player p = (Player) e.getEntity();
	Player target = (Player) e.getDamager();
	PlayerData pd = Main.getPlayerData(p);
	PlayerData td = Main.getPlayerData(target);

	if (pd.getTeam().equals(td.getTeam())) {

	    e.setDamage(0D);
	}
    }
}
