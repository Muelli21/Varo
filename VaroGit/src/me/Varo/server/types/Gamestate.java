package me.Varo.server.types;

public enum Gamestate {

    PREGAME(
	    "Pregame"),
    GAME(
	    "Game"),
    START(
	    "Start");

    String name;

    private Gamestate(String name) {

	this.name = name;
    }

    public String getName() {

	return this.name;
    }
}
