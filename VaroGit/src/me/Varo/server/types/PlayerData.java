package me.Varo.server.types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PlayerData {

    public static HashMap<Player, PlayerData> playerdatas = new HashMap<>();

    private Player player, lastDamager = this.getPlayer();
    private boolean adminmode = false, frozen = false, ingame = false, in1v1 = false, disguised = false, challenge = false, invulnerable = true, abletobuild = false;

    private Location frozenloc;
    private long lastReport;
    private long jointime;
    private long lastDamage;
    private boolean infight;
    private String team;

    private long cooldown;
    private String nick, lastmessage;
    private List<String> duels = new ArrayList<String>();

    public PlayerData(Player p) {

	this.player = p;
	playerdatas.put(p, this);
    }

    public void delete() {

	playerdatas.remove(player);
    }

    public void setPlayer(Player p) {

	this.player = p;
    }

    public List<String> getDuels() {

	return this.duels;
    }

    public boolean isInChallenge() {

	return this.challenge;
    }

    public void setInChallenge(boolean mode) {

	this.challenge = mode;
    }

    public String getLastMessage() {

	return this.lastmessage;
    }

    public void setLastMessage(String s) {

	this.lastmessage = s;
    }

    public String getNick() {

	return this.nick;
    }

    public void setNick(String nick) {

	this.nick = nick;
    }

    public void duel(Player p) {

	duels.add(p.getName());
    }

    public void removeDuel(String s) {

	duels.remove(s);
    }

    public void report() {

	this.lastReport = System.currentTimeMillis();
    }

    public long getLastReport() {

	return this.lastReport;
    }

    public Player getLastDamager() {

	return this.lastDamager;
    }

    public void setLastDamager(Player p) {

	this.lastDamager = p;
    }

    public void setIn1v1(boolean mode) {

	this.in1v1 = mode;
    }

    public boolean isIn1v1() {

	return this.in1v1;
    }

    public void setDisguised(Boolean mode) {

	this.disguised = mode;
    }

    public Boolean isDisguised() {

	return this.disguised;
    }

    public void setIngame(Boolean mode) {

	this.ingame = mode;
    }

    public Boolean isIngame() {

	return this.ingame;
    }

    public void setAdminmode(boolean mode) {

	this.adminmode = mode;
    }

    public Boolean isInAdminmode() {

	return this.adminmode;
    }

    public void setFrozen(boolean mode) {

	this.frozen = mode;
    }

    public Boolean isFrozen() {

	return this.frozen;
    }

    public Player getPlayer() {

	return this.player;
    }

    public void setFrozenLocation(Location loc) {

	this.frozenloc = loc;
    }

    public Location getFrozenLocation() {

	return this.frozenloc;
    }

    public long getCooldown() {
	return cooldown;
    }

    public void useAbility() {
	this.cooldown = System.currentTimeMillis();
    }

    public long getJointime() {
	return jointime;
    }

    public void setJointime() {
	this.jointime = System.currentTimeMillis();
    }

    public boolean isInfight() {
	return infight;
    }

    public void setInfight(boolean infight) {
	this.infight = infight;
    }

    public boolean isInvulnerable() {
	return invulnerable;
    }

    public void setInvulnerable(boolean invulnerable) {
	this.invulnerable = invulnerable;
    }

    public String getTeam() {
	return team;
    }

    public void setTeam(String team) {
	this.team = team;
    }

    public boolean isAbletobuild() {
	return abletobuild;
    }

    public void setAbletobuild(boolean abletobuild) {
	this.abletobuild = abletobuild;
    }

    public long getLastDamage() {
	return lastDamage;
    }

    public void setLastDamage() {
	this.lastDamage = System.currentTimeMillis();
    }

}
