package kaukau;

import java.util.HashMap;

import kaukau.model.Coin;
import kaukau.model.Direction;
import kaukau.model.GameMap;
import kaukau.model.GameWorld;
import kaukau.model.Key;
import kaukau.model.Player;
import kaukau.storage.JAXBJavaToXml;

public class DataStorageToXmlTest {
	private static GameWorld game;
	private static GameMap map;
	private static Player currentPlayer;
	
	/**
	 * FOR TESTING
	 * Initializes a dummy game to set the current player and map
	 * */
	public static void createDummyGame() {
		// create dummy game world
		game = new GameWorld();
		// create 2 players
		game.addPlayer();
		game.addPlayer();
		// add items already in inventory including coinBox
		HashMap<Integer, Player> players = game.getAllPlayers();
		currentPlayer = game.player(1);
		Player p2 = game.player(2);
		currentPlayer.addToBag(new Coin(50));
		currentPlayer.addToBag(new Coin(100));
		currentPlayer.addToBag(new Key(7));
		p2.addToBag(new Key(71));
		game.movePlayer(currentPlayer.getUserId(), Direction.NORTH);
		map = game.getGameMap();
	}
	
	/**
	 * Returns the dummy player
	 * @return Player
	 * */
	private static Player getPlayer(){
		return currentPlayer;
	}

	/**
	 * Returns the dummy Map
	 * @return GameMap
	 * */
	private static GameMap getMap(){
		return map;
	}	
	
	public static void main(String[] args) {
		JAXBJavaToXml marshaling = new JAXBJavaToXml();
		createDummyGame();
		marshaling.marshal(getPlayer(), getMap());
	}

}
