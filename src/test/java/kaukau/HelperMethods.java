package kaukau;

import kaukau.control.Server;
import kaukau.model.Direction;
import kaukau.model.GameWorld;
import kaukau.model.Player;
import kaukau.model.Tile;

public class HelperMethods {

	/**
	 * Creates a simple game.
	 * @return
	 */
	public static GameWorld simpleGame(){
		GameWorld game = new GameWorld();
		game.addPlayer();
		return game;
	}

	/**
	 * Create a basic player.
	 * @return
	 */
	public static Player player(){
		return new Player(0, "Test1", new Tile(), Direction.EAST);
	}

	/**
	 * Creates a server associated with a game.
	 * @return
	 */
	public static Server server(){
    	// make and run a server
    	return new Server();
	}
}
