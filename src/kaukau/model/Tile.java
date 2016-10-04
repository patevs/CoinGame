package kaukau.model;

import java.io.Serializable;

import kaukau.model.Room.TileType;

public class Tile implements Serializable {

	private Item item;
	private Player player;
	private TileType type;
	private int x, y;

	public Tile(TileType type, int x, int y){
		this.type = type;
		this.x = x;
		this.y = y;
		item = null;
		player = null;
	}

	/**
	 * Set this tile with a game item.
	 * This method mostly use to set up the game board.
	 * @param addItem the item to add on this tile
	 * @return true if it is successfully added, otherwise false;
	 */
	public boolean setItem(Item addItem){
		if (item == null && player == null){
			item = addItem;
			return true;
		}
		return false;
	}

	/**
	 * Add a player on this empty tile.
	 * @param player the player that occupy this tile
	 * @return true if the player successfully added, otherwise false.
	 */
	public boolean addPlayer(Player player){
		if (type != TileType.EMPTY) return false;
		else {
			if (this.player == null && this.item == null){
				this.player = player;
				return true;
			}
		}
		return false;
	}

	/**
	 * Remove the player that occupy this tile.
	 * @return true if the player successfully removed, otherwise false.
	 */
	public boolean removePlayer(){
		if (type != TileType.EMPTY) return false;
		else {
			if (this.player != null){
				this.player = null;
				return true;
			}
		}
		return false;
	}

	/**
	 * Drop an item to an empty tile if the tile is not occupy.
	 * @param dropItem the item that drop on this tile
	 * @return true if both the player and item fields are NULL, otherwise false.
	 */
	public boolean dropItem(PickupableItem dropItem){
		if (type != TileType.EMPTY) return false;
		else {
			if (item == null && player == null){
				item = dropItem;
				return true;
			}
		}
		return false;
	}

	/**
	 * Remove an item from a tile when a player pick the item.
	 * @return true if the tile is TileType.Empty and both the player
	 * and item fields are NULL, otherwise false .
	 */
	public boolean removeItem(){
		if (type != TileType.EMPTY) return false;
		else {
			if (item == null) return false;
			else item = null;
		}
		return false;
	}

	/**
	 * Check if this tile contain an pickupable item.
	 * @return true if there is a pickupableItem on an empty tile, otherwise false.
	 */
	public boolean containsPickupItem(){
		if (type != TileType.EMPTY) return false;
		else {
			if (type == TileType.EMPTY && item instanceof PickupableItem){
				return true;
			}
		}
		return false;
	}

	/**
	 * Check whether if this tile is occupy by a player or pickableItem.
	 * @return true if the tile is TileType.Empty, otherwise false if both
	 * the player and item fields are NULL.
	 */
	public boolean isTileOccupied(){
		if (type != TileType.EMPTY)
			return true;
		else if (type == TileType.EMPTY && player == null && item == null)
			return false;
		else return true;
	}

	/**
	 * Return the type of this tile.
	 * @return the type of this tile
	 */
	public TileType getTileType() { return type; }

	/**
	 * Return the item that occupy this tile.
	 * @return an item if it is pickupable item and there is one, otherwise returns null.
	 */
	public Item getItem(){ 
		if (item instanceof PickupableItem) return item;
		return null;
	}
	
	/**
	 * Return the column number of this tile.
	 * @return the column number
	 */
	public int X() { return x; }

	/**
	 * Return the row number of this tile.
	 * @return the row number
	 */
	public int Y() { return y; }

	public String toString(){
		return "row = "+this.x+", col = "+this.y;
	}

}
