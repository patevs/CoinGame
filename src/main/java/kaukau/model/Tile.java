package kaukau.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import kaukau.model.GameMap.TileType;

@SuppressWarnings("serial")
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Tile implements Serializable {

	private Key itemIsKey;
	private CoinBox itemIsCoinBox;
	private Coin itemIsCoin;
	private Door itemIsDoor;
	private Player player;
	private TileType type;
	private int x, y;

	/**
	 * Create a tile object for GameWorld.
	 * 
	 * @param type
	 *            the type of this tile
	 * @param x
	 *            the x coordinate of this tile
	 * @param y
	 *            the y coordinate of this tile
	 */
	public Tile(TileType type, int x, int y) {
		this.type = type;
		this.x = x;
		this.y = y;
		player = null;
	}

	/**
	 * Constructor for load and save to XML file purpose.
	 */
	public Tile() {
		this(null, -1, -1);
	}

	/**
	 * Set this tile with a game item. This method mostly use to set up the game
	 * board.
	 *
	 * @param addItem
	 *            the item to add on this tile
	 * @return true if it is successfully added, otherwise false;
	 */
	public boolean setItem(Item addItem) {
		if (addItem instanceof Key && addItem != null) {
			this.itemIsKey = (Key) addItem;
			return true;
		} else if (addItem instanceof CoinBox && addItem != null) {
			this.itemIsCoinBox = (CoinBox) addItem;
			return true;
		} else if (addItem instanceof Coin && addItem != null) {
			this.itemIsCoin = (Coin) addItem;
			return true;
		} else if (addItem instanceof Door && addItem != null) {
			this.itemIsDoor = (Door) addItem;
			return true;
		}
		return false;
	}

	/**
	 * Add a player on this empty tile.
	 *
	 * @param player
	 *            the player that occupy this tile
	 * @return true if the player successfully added, otherwise false.
	 */
	public boolean addPlayer(Player player) {
		if (this.player == null) {
			this.player = player;
			return true;
		}
		return false;
	}

	/**
	 * Remove the player that occupy this tile.
	 *
	 * @return true if the player successfully removed, otherwise false.
	 */
	public boolean removePlayer() {
		if (this.player != null) {
			this.player = null;
			return true;
		}
		return false;
	}

	/**
	 * Returns the player currently on this tile or null if none
	 * 
	 * @return the player that occupy this tile
	 */
	public Player getPlayer() {
		if (this.player != null) {
			return player;
		} else {
			return null;
		}
	}

	/**
	 * Drop an item to an empty tile if the tile is not occupy.
	 *
	 * @param dropItem
	 *            the item that drop on this tile
	 * @return true if both the player and item fields are NULL, otherwise
	 *         false.
	 */
	public boolean dropItem(PickupableItem dropItem) {
		if ((type == TileType.TILE || type == TileType.TILE_CRACKED) && player == null
				&& dropItem instanceof PickupableItem) {
			setItem(dropItem);
			return true;
		}
		return false;
	}

	/**
	 * Remove an item from a tile when a player pick the item.
	 *
	 * @return true if the tile is TileType.Empty and both the player and item
	 *         fields are NULL, otherwise false .
	 */
	public boolean removeItem() {
		if ((type == TileType.TILE || type == TileType.TILE_CRACKED)) {
			if (itemIsKey != null) {
				itemIsKey = null;
				return true;
			} else if (itemIsCoinBox != null) {
				itemIsCoinBox = null;
				return true;
			} else if (itemIsCoin != null) {
				itemIsCoin = null;
				return true;
			} else if (itemIsDoor != null) {
				itemIsDoor = null;
				return true;
			}

		}
		return false;
	}

	/**
	 * Check if this tile contain an pickupable item.
	 *
	 * @return true if there is a pickupableItem on an empty tile, otherwise
	 *         false.
	 */
	public boolean containsPickupItem() {
		if ((type == TileType.TILE || type == TileType.TILE_CRACKED)) {
			if (itemIsDoor == null && (itemIsKey != null || itemIsCoinBox != null || itemIsCoin != null)) {
				itemIsDoor = null;
				return true;
			}
		}
		return false;
	}

	
	/**
	 * Return the key item on this tile. This method is uses for load and save
	 * XML file.
	 * 
	 * @return a Key object
	 */	
	public Key getKey() {
		if (this.itemIsKey != null)
			return this.itemIsKey;
		return null;
	}

	/**
	 * Sets the Key item at this tile IF the tile is a empty /cracked tile with no objects on it
	 * 
	 * @param Key key
	 * */
	public void setKey(Key key) {
		if (!isTileOccupied()) {
			this.itemIsKey = key;
		}
	}

	/**
	 * Return the coinbox object on this tile. This method is uses for load and
	 * save XML file.
	 * 
	 * @return a CoinBox object
	 */
	public CoinBox getCoinBox() {
		if (this.itemIsCoinBox != null)
			return this.itemIsCoinBox;
		return null;
	}

	/**
	 * Sets the CoinBox item at this tile IF the tile is a empty /cracked tile with no objects on it
	 * 
	 * @param CoinBox coinBox
	 * */
	public void setCoinBox(CoinBox coinbox) {
		if (!isTileOccupied()) {
			this.itemIsCoinBox = coinbox;
		}
	}

	/**
	 * Return the coin object on this tile. This method is uses for load and
	 * save XML file.
	 * 
	 * @return a Coin object
	 */
	public Coin getCoin() {
		if (this.itemIsCoin != null)
			return this.itemIsCoin;
		return null;
	}

	/**
	 * Sets the Coin item at this tile IF the tile is a empty /cracked tile with no objects on it
	 * 
	 * @param Coin coin
	 * */
	public void setCoin(Coin coin) {
		if (!isTileOccupied()) {
			this.itemIsCoin = coin;
		}
	}

	/**
	 * Return the Door object on this tile. This method is uses for load and
	 * save XML file.
	 * 
	 * @return a Door object
	 */
	public Door getDoor() {
		if (this.itemIsDoor != null)
			return this.itemIsDoor;
		return null;
	}

	/**
	 * Sets the Door item at this tile IF the tile is a empty /cracked tile with no objects on it
	 * 
	 * @param Door door
	 * */
	public void setDoor(Door door) {
		if (!isTileOccupied()) {
			this.itemIsDoor = door;
		}
	}

	/**
	 * Check whether if this tile is occupy by a player or pickableItem.
	 * 
	 * @return true if the tile is TileType.Empty, otherwise false if both the
	 *         player and item fields are NULL.
	 */
	public boolean isTileOccupied() {
		if (type == TileType.TILE || type == TileType.TILE_CRACKED) {
			if (itemIsDoor == null  && !containsPickupItem() && player == null) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Return the type of this tile.
	 *
	 * @return the type of this tile
	 */
	public TileType getTileType() {
		return type;
	}

	/**
	 * Set the tile type of this tile.
	 * 
	 * @param type
	 *            the TileType of this tile
	 */
	public void setTileType(TileType type) {
		this.type = type;
	}

	/**
	 * Return the item that occupy this tile.
	 *
	 * @return an item if it is pickupable item and there is one, otherwise
	 *         returns null.
	 */
	@XmlAnyElement
	public Item getItem() {
		if (itemIsKey != null) {			
			return itemIsKey;
		} else if (itemIsCoinBox != null) {
			return itemIsCoinBox;
		} else if (itemIsCoin != null) {
			return itemIsCoin;
		} else if (itemIsDoor != null) {
			return itemIsDoor;
		}return null;
	}

	/**
	 * Return the column number of this tile.
	 *
	 * @return the column number
	 */
	public int X() {
		return x;
	}

	@XmlElement(name = "xCoord")
	public int getX() {
		return x;
	}

	/**
	 * Return the row number of this tile.
	 *
	 * @return the row number
	 */
	public int Y() {
		return y;
	}

	/**
	 * Return the row number of this tile.
	 *
	 * @return the row number
	 */
	@XmlElement(name = "yCoord")
	public int getY() {
		return y;
	}

	/**
	 * Set the x position for this tile. This method is uses for load and save
	 * XML file.
	 * 
	 * @param x
	 *            the x position of the tile
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Set the y position for this tile. This method is uses for load and save
	 * XML file.
	 * 
	 * @param y
	 *            the y position of the tile
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * Return the row and col of this tile.
	 * 
	 * @return the row and col of this tile.
	 */
	public String toString() {
		return "row = " + this.x + ", col = " + this.y;
	}

}
