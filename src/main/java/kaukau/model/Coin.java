package kaukau.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class represents a Coin object which can be pickup or drop by the
 * player. A coin can be stored in the player's inventory or in a CoinBox
 * object.
 *
 * @author Vivienne Yapp, 300339524
 *
 */
@SuppressWarnings("serial")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Coin extends PickupableItem implements Serializable {
	@XmlElement
	private int amount;

	/**
	 * Create a Coin object.
	 *
	 * @param amount
	 *            the amount of this coin
	 */
	public Coin(int amount) {
		super("Coin");
		this.amount = amount;
	}

	/**
	 * Constructor for load and save to XML file purpose.
	 */
	@SuppressWarnings("unused")
	public Coin() {
		this(-1);
	}

	/**
	 * Get the amount of this coin.
	 *
	 * @return the amount of this coin
	 */
	public int getAmount() {
		return amount;
	}

    /**
     * Set amount of this coin.
     * @param amount the amount of the coin
     */
    public void setAmount(int amount){
    	this.amount = amount;
    }
}
