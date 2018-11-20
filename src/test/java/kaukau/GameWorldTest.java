package kaukau;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Point;

import org.junit.Test;

import kaukau.model.Coin;
import kaukau.model.CoinBox;
import kaukau.model.Direction;
import kaukau.model.GameMap;
import kaukau.model.GameMap.TileType;
import kaukau.model.GameWorld;
import kaukau.model.Key;
import kaukau.model.Player;
import kaukau.model.Tile;

/**
 * Tests for GameWorld's logic and state.
 * @author Vivienne Yapp, 300339524
 *
 */
public class GameWorldTest {

	/**
	 * This test construct a game then add one player to the game.
	 * Check if the player get the right user id and start location.
	 */
	@Test
	public void testAddPlayer_1() {
		GameWorld game = new GameWorld();
		int uid = game.addPlayer();
		assertTrue(uid == game.getAllPlayers().size());
	}

	/**
	 * This test construct a game then add one player to the game.
	 * Check if the player get the right user id and start location.
	 * Also check if the player's inventory contain one item which is a CoinBox.
	 */
	@Test
	public void testAddPlayer_2() {
		GameWorld game = new GameWorld();
		int uid = game.addPlayer();
		Player player = game.player(uid);
		int size = player.getInventory().size();
		assertEquals("Player's inventory size is " + size + " players but should have 1.", 1, size);
		assertTrue(player.getInventory().get(0) instanceof CoinBox);
		CoinBox coinbox = player.getCoinBox();
		assertTrue(!coinbox.isStorageFull());
	}


	/**
	 * This test construct a game then add two player to the game.
	 * Check if the player's id is increment correctly by 1.
	 */
	@Test
	public void testAddPlayer_3() {
		GameWorld game = new GameWorld();
		GameMap board = game.getGameMap();
		int uid1 = game.addPlayer();
		assertTrue(uid1 == game.getAllPlayers().size());

		int uid2 = game.addPlayer();
		assertTrue(uid2 == game.getAllPlayers().size());
	}

	/**
	 * Test player move to an empty tile.
	 */
	@Test
	public void testMovePlayer_1() {
		GameWorld game = new GameWorld();
		GameMap board = game.getGameMap();
		int uid = game.addPlayer();
		Player player = game.player(uid);
		assertTrue(game.movePlayer(uid, Direction.SOUTH));
		assertTrue(player.getfacingDirection() == Direction.SOUTH);
		assertTrue(player.getLocation().isTileOccupied());
	}

	/**
	 * Test player move to an empty tile and check player's facing
	 * direction is on the right direction.
	 */
	@Test
	public void testMovePlayer_2() {
		GameWorld game = new GameWorld();
		GameMap board = game.getGameMap();
		int uid = game.addPlayer();
		Player player = game.player(uid);
		assertTrue(game.movePlayer(uid, Direction.SOUTH));
		assertTrue(player.getfacingDirection() == Direction.SOUTH);
		assertTrue(game.movePlayer(uid, Direction.EAST));
		assertTrue(player.getfacingDirection() == Direction.EAST);
		assertTrue(player.getLocation().isTileOccupied());
	}

	/**
	 * Move player around the board, old position should be not ocuppy
	 * after player move to the new position. The new position should be marked as occupy.
	 */
	@Test
	public void testMovePlayer_3() {
		GameWorld game = new GameWorld();
		GameMap board = game.getGameMap();
		int uid = game.addPlayer();
		Player player = game.player(uid);
		Tile tile = player.getLocation();
		Point oldPos = new Point(tile.X(), tile.Y());
		assertTrue(board.getTileAt(oldPos).isTileOccupied());
		assertTrue(game.movePlayer(uid, Direction.SOUTH));
		assertTrue(board.getTileAt(new Point(oldPos.x+1, oldPos.y)).isTileOccupied());
		assertTrue(player.getfacingDirection() == Direction.SOUTH);
		assertTrue(game.movePlayer(uid, Direction.EAST));
		assertTrue(player.getfacingDirection() == Direction.EAST);
	}

	/**
	 * Test a player to pick up a key on the board. Player only allow to pick
	 * up an item in front of them and the pickup item must be pickupable type.
	 */
	@Test
	public void testPickupItem_1() {
		GameWorld game = new GameWorld();
		GameMap board = game.getGameMap();
		int uid = game.addPlayer();
		Player player = game.player(uid);
		assertTrue(game.movePlayer(uid, Direction.EAST));
		assertTrue(game.movePlayer(uid, Direction.EAST));
		Tile item = board.getTileAt(new Point(5, 6));
		assertTrue(player.getInventory().size() == 1);
		player.setLocation(board.getTileAt(new Point(5, 5)));
		player.setfacingDirection(Direction.EAST);
		assertTrue(item.containsPickupItem());
		assertTrue(game.pickupAnItem(uid));
		assertTrue(player.getInventory().size() == 2);
		assertTrue(player.getInventory().get(1) instanceof Key);
	}

	/**
	 * Test a player to pick up a Coin on the board. Player only allow to pick
	 * up an item in front of them and the pickup item must be pickupable type.
	 */
	@Test
	public void testPickupItem_2() {
		GameWorld game = new GameWorld();
		GameMap board = game.getGameMap();
		int uid = game.addPlayer();
		Player player = game.player(uid);
		Tile coin = board.getTileAt(new Point(7,6));
		Tile newPos = board.getTileAt(new Point(7,5));
		int coinAmount = ((Coin) coin.getItem()).getAmount();
		player.setLocation(newPos);
		player.setfacingDirection(Direction.EAST);
		assertTrue(coin.getTileType() == TileType.TILE || coin.getTileType() == TileType.TILE_CRACKED);
		assertTrue(coin.getItem() instanceof Coin);
		assertTrue(game.pickupAnItem(uid));   // player pick up the coin
		assertFalse(coin.isTileOccupied());   // the tile should be not occupy after player pickup coin
		int size = player.getInventory().size();
		assertEquals("Player's inventory size is " + size + " players but should have 1 after pick up a coin."
				+ "Coin should be stored in the Coinbox that inside the inventory.", 1, size);
		CoinBox coinBox = player.getCoinBox();
		assertEquals("The total money of the player is " + player.totalMoney() + ", it should be 30", coinAmount, player.totalMoney());
		assertEquals("The total coin in the CoinBox is " + coinBox.getStorage().size() + ", it should be 1.", 1, coinBox.getStorage().size());
	}

	/**
	 * Test a player to pick up a coin on the board but the coinbox is full so the coin should be added
	 * to the player's inventory. Player only allow to pick up an item in front of them and
	 * the pickup item must be pickupable type.
	 */
	@Test
	public void testPickupItem_3() {
		GameWorld game = new GameWorld();
		GameMap board = game.getGameMap();
		int uid = game.addPlayer();
		Player player = game.player(uid);
		player.addToBag(new Coin(120));
		player.addToBag(new Coin(130));
		assertTrue(player.getCoinBox().isStorageFull());
		Tile coin = board.getTileAt(new Point(7,6));
		Tile newPos = board.getTileAt(new Point(7,5));
		player.setLocation(newPos);
		player.setfacingDirection(Direction.EAST);
		assertTrue(coin.getTileType() == TileType.TILE || coin.getTileType() == TileType.TILE_CRACKED);
		assertTrue(coin.getItem() instanceof Coin);
		assertTrue(coin.containsPickupItem());  // the tile should contain the key
		assertTrue(game.pickupAnItem(uid));   // player pick up the coin
		assertFalse(coin.isTileOccupied());   // the tile should be not occupy after player pickup coin
		assertTrue(game.dropAnItem(uid, 1));
		assertTrue(coin.containsPickupItem());
		assertTrue(coin.getItem() instanceof Coin);
		int size = player.getInventory().size();
		assertEquals("Player's inventory size is " + size + " players but should have 1 after pick up the three coin.", 1, size);
	}

	/**
	 * Test a player to pick up a coin on the board but the coinbox is full so the coin should be added
	 * to the player's inventory. Player only allow to pick up an item in front of them and
	 * the pickup item must be pickupable type.
	 */
	@Test
	public void testDropItem_1() {
		GameWorld game = new GameWorld();
		GameMap board = game.getGameMap();
		int uid = game.addPlayer();
		Player player = game.player(uid);
		player.addToBag(new Coin(120));
		player.addToBag(new Coin(130));
		assertTrue(player.getCoinBox().isStorageFull());
		Tile key = board.getTileAt(new Point(5,6));
		Tile newPos = board.getTileAt(new Point(6,6));
		player.setfacingDirection(Direction.NORTH);
		player.setLocation(newPos);
		assertTrue(game.pickupAnItem(uid));
		assertFalse(key.containsPickupItem());
		assertTrue(game.dropAnItem(uid, 1));
		int size = player.getInventory().size();
		assertEquals("Player's inventory size is " + size + " players but should have 1 after pick up the three coin.", 1, size);
	}

	/**
	 * Test invalid move when player try to move into a door,
	 * player use enter commands to interact with door.
	 */
	@Test
	public void testInvalidMovePlayer_1() {
		GameWorld game = new GameWorld();
		GameMap board = game.getGameMap();
		int uid = game.addPlayer();
		Player player = game.player(uid);
		Tile oldPos = player.getLocation();
		Tile newPos = board.getTileAt(new Point(3, 8));
		Tile wall = board.getTileAt(new Point(3, 9));
		player.setLocation(newPos);
		player.setfacingDirection(Direction.EAST);
		assertTrue(wall.getTileType() == TileType.WALL);
		assertFalse(game.movePlayer(uid, Direction.EAST));
		assertTrue(player.getLocation().X() == newPos.X());
		assertTrue(player.getLocation().Y() == newPos.Y());
	}

	/**
	 * Test invalid move when player try to move into a tile that contain an item.
	 */
	@Test
	public void testInvalidMovePlayer_2() {
		GameWorld game = new GameWorld();
		GameMap board = game.getGameMap();
		int uid = game.addPlayer();
		Player player = game.player(uid);
		Tile coin = board.getTileAt(new Point(7,6));
		Tile newPos = board.getTileAt(new Point(7,5));
		player.setLocation(newPos);
		player.setfacingDirection(Direction.EAST);
		assertTrue(coin.getTileType() == TileType.TILE || coin.getTileType() == TileType.TILE_CRACKED);
		assertTrue(coin.getItem() instanceof Coin);
		assertFalse(game.movePlayer(uid, Direction.EAST));
	}


	@Test
	public void testCreateGame_1() {
		GameWorld game = new GameWorld();
		GameMap board = game.getGameMap();
		kaukau.model.Tile[][] tiles = board.getBoard();
		for(int row = 0; row < board.height(); row++){
			for (int col = 0; col < board.width(); col++){
				assertTrue(tiles[col][row] != null);
			}
		}
	}


}
