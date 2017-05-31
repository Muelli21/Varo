package me.Varo.server.types;

import org.bukkit.Server;

public class ServerData {

    public static ServerData sd;

    private Server server;
    private Gamestate gamestate = Gamestate.PREGAME;
    private String name;
    private long starttime;

    public ServerData() {

	sd = this;
    }

    public Gamestate getGamestate() {
	return gamestate;
    }

    public void setGamestate(Gamestate gamestate) {
	this.gamestate = gamestate;
    }

    public Server getServer() {
	return server;
    }

    public void setServer(Server server) {
	this.server = server;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public long getStarttime() {
	return starttime;
    }

    public void setStarttime() {
	this.starttime = System.currentTimeMillis();
    }

}
