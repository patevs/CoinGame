package kaukau.model;

import java.io.Serializable;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A CoinBox is a pickupable item which can be carry by player. This CoinBox
 * object can be stored in Player's inventory and CoinBox can used to store Coin
 * object only.
 *
 * @author Vivienne Yapp, 300339524
 *
 */
@SuppressWarnings("serial")
@XmlRootElement
public class CoinBox extends PickupableItem implements Serializable {

	private Container storage;
	@SuppressWarnings("unused")
	private Player player;
	int totalCoinAmount = 0;

	/**
	 * Create a coinbox for the given player.
	 * @param player the coinbox that belongs to this player
	 */
	public CoinBox(Player player) {
		super("Coin Box");
		setPlayer(player);
		// this.storage = new Container("Coin Box", player.getLocation());
		this.storage = new Container("Coin Box");
		storage.setAmount(2);
	}

	/**
	 * Constructor for load and save to XML file purpose.
	 */
	@SuppressWarnings("unused")
	public CoinBox() {
		this(null);
	}

	/**
	 * Add coin to the coinbox.
	 *
	 * @param item the item to add
	 * @return true if the item is a coin and the coinBox is not full, otherwise false
	 */
	public boolean addCoin(Item item) {

		if (item instanceof Coin) {
			Coin coin = (Coin) item;
			if (storage.addItem(item) && !storage.isStorageFull()) {
				totalCoinAmount += coin.getAmount();
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the list of coin in the coin box.
	 *
	 * @return return all the coin objects
	 */
	@XmlElementWrapper(name = "coins")
	@XmlElements({ @XmlElement(name = "coin") })
	public ArrayList<Coin> getStorage() {
		ArrayList<Coin> coins = new ArrayList<Coin>();
		for (PickupableItem p : this.storage.getStorage()) {
			coins.add((Coin) p);
		}
		return coins;
	}

	/**
	 * Set this coinbox with a set of coins object.
	 * @param coins the set of coins.
	 */
	public void setStorage(ArrayList<Coin> coins) {
		for (Coin c : coins) {
			addCoin(c);
		}
	}

	/**
	 * Return the total coins in this coinBox.
	 *
	 * @return the total coins of this coinbox.
	 */
	public int totalCoins() {
		int total = 0;
		for (PickupableItem p : getStorage()) {
			Coin c = (Coin) p;
			total += c.getAmount();
		}
		return total;
	}

	/**
	 * Check if this coin box is full or not.
	 *
	 * @return true if the coinbox is full, otherwise false.
	 */
	@XmlElement(name = "isFull")
	public boolean isStorageFull() {
		return storage.isStorageFull();
	}

	/**
	 * Set the owner of this coinbox.
	 * @param player the owner of this coinbox
	 */
	void setPlayer(Player player) {
		this.player = player;
	}

}
