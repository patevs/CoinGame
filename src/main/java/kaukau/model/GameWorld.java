package kaukau.model;

import java.util.HashMap;
import java.util.Random;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import kaukau.model.GameMap.TileType;

import java.awt.Point;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

@SuppressWarnings("serial")
@XmlRootElement(name="GameWorld")
//@XmlType(propOrder = { "allPlayers"})
public class GameWorld implements Serializable{

	/**
	 * The current board the game
	 */
	private GameMap board;

	private boolean gameOver;

	/**
	 * The UID is a unique identifier for all characters in the game. This is
	 * required in order to synchronise the movements of different players
	 * across boards.
	 */
	private static int uid = 0;

	/**
	 * The current players of this game. Max number of player = 2.
	 */
	private static HashMap<Integer, Player> players = new HashMap<Integer, Player>();

	/**
	 * Create a new game.
	 */
	public GameWorld(){
		board =  new GameMap();
		gameOver = false;
	}

	/**
	 * Register a new player into the game.
	 * @return the user id of the new player.
	 */
	public synchronized int addPlayer(){
		Random rand = new Random();
		Tile tile = board.getTileAt(new Point(7-(rand.nextInt(5)), 3));
		Player player = new Player(++uid, "Player", tile, Direction.EAST);
		tile.addPlayer(player);
		player.setLocation(tile);
		GameWorld.players.put(uid, player);
		return uid;
	}

	/**
	 * Remove a player from the current game.
	 * @param uid user id of the player
	 */
	public synchronized void removePlayer(int uid){
		if (players.containsKey(uid)){
			players.remove(uid);
		}
	}

	/**
	 * Move player to the new position if the tile of the new position is not occupy. Otherwise, it returns False.
	 * @param uid Player's name
	 * @param direction The direction to move
	 * @return True if the player is move to the new position, otherwise False.
	 */
	public synchronized boolean movePlayer(int uid, Direction direction){
		Player player = players.get(uid);
		Tile oldPos = player.getLocation();
		Point newPos = getPointFromDirection(player, direction);

		// if the tile is emptyTile and not occupy, then move player to this new position
		if (validPoint(newPos)){
			Tile tile = board.getTileAt(newPos);
			if (!tile.isTileOccupied() &&
				(tile.getTileType()==GameMap.TileType.TILE
					||tile.getTileType()==GameMap.TileType.TILE_CRACKED)){
					oldPos.removePlayer();
					player.setLocation(tile);
					player.setfacingDirection(direction);
					tile.addPlayer(player);
					return true;
			}
		}
		player.setfacingDirection(direction);
		return false;
	}

	 /** Performs a pickup item for a given Player.
	  *
	  * @param uid user id belongs to this player
	  * @return true if the player successfully pick up an item, otherwise false.
	  */
	public synchronized boolean pickupAnItem(int uid){
		Player player = players.get(uid);
		Point pos = getPointFromDirection(player, player.getfacingDirection());

		// if the tile is EmptyTile type and item is pickupable,
		// then player can pick up the item only if there is one
		if (validPoint(pos)){
			Tile tile = board.getTileAt(pos);
			if (tile.containsPickupItem()){
				if (player.addToBag((PickupableItem)tile.getItem())){
					tile.removeItem();
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Performs a drop item for a given Player and index number of the item in the inventory.
	 * @param uid user id belongs to this player
	 * @param index the number index of the inventory
	 * @return true if the player successfully drop an item on a tile, otherwise false.
	 */
	public synchronized boolean dropAnItem(int uid, int index){
		Player player = players.get(uid);
		Point pos = getPointFromDirection(player, player.getfacingDirection());
		if (validPoint(pos)){
			Tile tile = board.getTileAt(pos);
			if (!tile.isTileOccupied() && player.containItemAtIndex(index) &&
			    (tile.getTileType() == TileType.TILE || tile.getTileType() == TileType.TILE_CRACKED)){
				Item item = player.getInventory().get(index);
				tile.setItem(item);
				return player.removeFromBag(index);
			}
		}
		return false;
	}

	/**
	 * Performs door open by a given Player.
	 * @param uid user id belongs to this player
	 * @return true if the player successfully open door and enter the room, otherwise false.
	 */
	public synchronized boolean openDoor(int uid){
		Player player = players.get(uid);
		Tile oldPos = player.getLocation();
		Point pos = getPointFromDirection(player, player.getfacingDirection());
		if (validPoint(pos)){
			Tile doorTile = board.getTileAt(pos);
			if (doorTile.getTileType() == TileType.DOOR){  // if the facing direction is a door
				Door door = (Door) doorTile.getItem();
				if (!door.isLocked()){
					Point newPos = getDoorPoint(player, player.getfacingDirection());
					if (validPoint(newPos)){ // check if this new point is valid or not
						Tile newTile = board.getTileAt(newPos);
						if (!newTile.isTileOccupied()){
							oldPos.removePlayer();        // remove player from a tile
							player.setLocation(newTile);  // set the player new position
							newTile.addPlayer(player);    // add player to this new location
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * Get the point of a door from the player's facing direction
	 * @param player the current player
	 * @param direction the facing direction of the player
	 * @return the new point of the door
	 */
	private Point getDoorPoint(Player player, Direction direction){
		Tile oldPos = player.getLocation();
		if (player.getfacingDirection() == Direction.NORTH)
			return new Point(oldPos.X()-2, oldPos.Y());
		else if (player.getfacingDirection() == Direction.SOUTH)
			return new Point(oldPos.X()+2, oldPos.Y());
		else if (player.getfacingDirection() == Direction.EAST)
			return new Point(oldPos.X(), oldPos.Y()+2);
		else return new Point(oldPos.X(), oldPos.Y()-2);
	}

	/**
	 * Check if this given point is valid or not.
	 * @param pos the given point
	 * @return true if the x and y is within the board's size, otherwise false
	 */
	public boolean validPoint(Point pos){
		if (board.width() <= pos.x || board.height() <= pos.y ||
				pos.x < 0 || pos.y < 0) {
			return false;
		}
		return true;
	}

	/**
	 * Get the new point from the facing direction of the player.
	 * @param player the current player
	 * @param direct facing direction of the player
	 * @return new point from the facing direction of the player
	 */
	public Point getPointFromDirection(Player player, Direction direct){
		Tile oldPos = player.getLocation();
		if (direct == Direction.NORTH)
			return new Point(oldPos.X()-1, oldPos.Y());
		else if (direct == Direction.SOUTH)
			return new Point(oldPos.X()+1, oldPos.Y());
		else if (direct == Direction.EAST)
			return new Point(oldPos.X(), oldPos.Y()+1);
		else return new Point(oldPos.X(), oldPos.Y()-1);
	}

	/**
	 * The following method converts the current state of the game into a byte
	 * array, such that it can be shipped across a connection to an awaiting client.
	 *
	 * @return the byte array of the current state of the game
	 * @throws IOException Signals that an I/O exception of some sort has occurred.
	 * This class is the general class of exceptions produced by failed or interrupted I/O operations.
	 */
	public synchronized byte[] toByteArray() throws IOException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		ObjectOutputStream dataOutput = new ObjectOutputStream(bout);
		dataOutput.writeBoolean(gameOver);  // game state
		dataOutput.writeObject(this.players);
		dataOutput.writeObject(this.board);
		dataOutput.flush();
		// Finally, return!!
		return bout.toByteArray();
	}

	/**
	 * The following method accepts a byte array representing the state of a
	 * game; this state will be broadcast by a master connection, and is
	 * then used to overwrite the current state (since it should be more up to date).
	 *
	 * @param bytes the objects that need to be read
	 * @throws IOException if the class object is not matched
	 */
	@SuppressWarnings("unchecked")
	public synchronized void fromByteArray(byte[] bytes) throws IOException{
		ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
		try {
			ObjectInputStream dataInput = new ObjectInputStream(bin);
			this.gameOver = dataInput.readBoolean();
			this.players = (HashMap<Integer, Player>) dataInput.readObject();
			this.board = (GameMap) dataInput.readObject();
		} catch (ClassNotFoundException e) {
			System.err.println("Class not found in fromByteArray. " + e);
			e.printStackTrace();
		}
	}

	/**
	 * Returns the player of the game that matched with the userId.
	 * @param uid UserId uses by the client
	 * @return the player with the matching userId
	 */
	public synchronized Player player(int uid){
		if (players.containsKey(uid)){
			return players.get(uid);
		}
		throw new IllegalArgumentException("Invalid Player UID");
	}

	/**
	 * Check if the game is over or not.
	 * @return true if the game is over, otherwise false.
	 */
	public synchronized Boolean gameOver(){
		return gameOver;
	}

	/**
	 * Return the current board of the game.
	 * @return the current board of the game
	 */
	@XmlElement(name="map")
	public GameMap getGameMap(){
		return board;
	}

	/**
	 * Return the current players of the game.
	 * @return all the players in the game
	 */
	@XmlElementWrapper(name="allPlayers")
    @XmlElements({
    @XmlElement(name="player") }
    )
	public HashMap<Integer, Player> getAllPlayers(){
		return players;
	}

	/**
	 * Return the current state of the game.
	 * @return true if the game is over, otherwise false.
	 */
	@XmlElement(name="isGameOver")
	public boolean isOver(){
		return gameOver;
	}

	/**
	 * Return all the tiles of the current board game.
	 * @return all the tiles of the current board
	 */
	public Tile[][] getGameTiles(){
		return board.getBoard();
	}

}
