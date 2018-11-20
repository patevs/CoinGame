package kaukau.model;

import java.io.Serializable;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * This class represent a Player for GameWorld.
 * @author Vivienne Yapp, 300339524 (Getter and setter methods added by Shaika Khan)
 *
 */
@SuppressWarnings("serial")
@XmlRootElement // (namespace = "Team_24.kaukau.model.GameWorld")
@XmlType(propOrder = { "name", "userId", "facingDirection", "location", "inventory"})
public class Player implements Serializable {
	@XmlElement
	private String name;
	private Tile location;
	private Container inventory;
	private CoinBox coinbox;
	private Direction facing;
	@XmlElement
	private final int userId;

	/**
	 * Create a player with a user id, facing direction and start location.
	 * @param uid user id
	 * @param name player name
	 * @param startLocation start location of this player
	 * @param facing the facing direction
	 */
	public Player(int uid, String name, Tile startLocation, Direction facing) {
		this.userId = uid;
		this.name = name;
		location = startLocation;
		this.facing = facing;
		inventory = new Container("Backpack");
		inventory.setAmount(8);
		this.coinbox = new CoinBox(this);
		this.inventory.addItem(this.coinbox);
		this.coinbox.setPlayer(this);
	}

	/**
	 * Constructor for load and save to XML file purpose.
	 */
	@SuppressWarnings("unused")
	private Player() {
		this(-1, null, null, null);
	}

	/**
	 * Sets the new location of this player.
	 * @param loc the new location of the tile
	 */
	public void setLocation(Tile loc) {
		this.location = loc;
	}

	/**
	 * Sets the facing direction of this player.
	 * @param direct the new facing direction
	 */
	public void setfacingDirection(Direction direct) {
		this.facing = direct;
	}

	/**
	 * Get the facing direction of this player.
	 * @return facing direction
	 */
	@XmlElement(name = "getFacingDirection")
	public Direction getfacingDirection() {
		return facing;
	}

	/**
	 * Gets the name of the player.
	 *
	 * @return the name of the player
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Gets the tile location of this player.
	 *
	 * @return the tile location of this player
	 */
	@XmlElement(name = "getLocation")
	public Tile getLocation() {
		return this.location;
	}

	/**
	 * Return the coinbox that belongs to this player.
	 *
	 * @return a CoinBox type object, otherwise null if player drop it.
	 */
	public CoinBox getCoinBox() {
		return this.coinbox;
	}

	/**
	 * Add an item to player's inventory. If the item is a Coin type,
	 * first it will check if the coinbox is full or not. If it is full, then
	 * it will store in the inventory.
	 * @param item the pickupableItem
	 * @return true if the item successfully added to the inventory, otherwise false.
	 */
	public boolean addToBag(Item item) {
		if (item instanceof Coin && !coinbox.isStorageFull()) {
			return coinbox.addCoin(item);
		} else if (item instanceof CoinBox && coinbox != null) { // player only allow one coinbox
			return false;
		}
		return inventory.addItem(item);
	}

	/**
	 * Removes an item from the player's inventory
	 * @param index the number index of the item in the inventory
	 * @return true if it is successfully removed from the inventory, otherwise false.
	 */
	public boolean removeFromBag(int index) {
		Item item = inventory.getItem(index);
		if (item != null && item instanceof CoinBox)
			coinbox = null;
		return inventory.removeItem(index);
	}

	/**
	 * Get the list of items in the player's inventory.
	 * @return the list of items in the player's inventory
	 */
	@XmlElementWrapper(name = "inventory")
	@XmlElement(name = "item")
	public ArrayList<PickupableItem> getInventory() {
		return inventory.getStorage();
	}

	/**
	 * Set the inventory of this player. Mainly use for loading the game from XML file.
	 * @param inv the list of pickupableItem
	 */
	public void setInventory(ArrayList<PickupableItem> inv) {
		inventory.setStorage(inv);
	}

	/**
	 * Return the user id of this player.
	 *
	 * @return the user id of this player.
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * Returns the total money in the player's coinBox if the player has one.
	 *
	 * @return total amount of coin or zero if there is not coinBox
	 */
	public int totalMoney() {
		if (coinbox != null)
			return coinbox.totalCoins();
		return 0;
	}

	/**
	 * Return the size of the player's storage.
	 *
	 * @return the user id of this player.
	 */
	public int getStorageSize() {
		return inventory.getStorageLimit();
	}

	/**
	 * Returns the string description of this player.
	 *
	 * @return String
	 */
	public String toString() {
		return "Player " + this.name + " at position " + this.location.toString();
	}

	/**
	 * Check if the inventory of the player contains a key to this given door or not.
	 * @param door the door to unlock
	 * @return true if the player has the key that matches the given door.
	 */
	public boolean containsKey(Door door){
		for(PickupableItem item : inventory.getStorage()){
			if (item instanceof Key){
				if (((Key) item).getCode() == door.getDoorCode()){
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Check if the player's inventory contain item at this index number
	 * @param index the index number
	 * @return true if the index within the size of the inventory
	 */
	public boolean containItemAtIndex(int index){
		if (index < inventory.getStorage().size() && index >= 0){
			return true;
		}
		return false;
	}

}
