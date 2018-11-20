package kaukau.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;
/**
 * A Pickupable object is something a player can pick up, inspect, store in a container or drop.
 * Pickupable items include Keys, pieces of maps, notes and clues.
 * @author Vivienne Yapp, 300339524
 *
 */
@SuppressWarnings("serial")
@XmlRootElement
@XmlSeeAlso({CoinBox.class, Coin.class, Key.class})
@XmlTransient
public abstract class PickupableItem implements Item, Serializable{

	protected String name;

	/**
	 * Create a pickupable item for the game.
	 * @param name the name of the pick up item
	 */
	public PickupableItem(String name){
		this.name = name;
	}

	/**
	 * Constructor for load and save to XML file purpose.
	 */
	@SuppressWarnings("unused")
	public PickupableItem(){
		this(null);
	}

	/**
	 * Return the name of this item.
	 * @return the name of this item
	 */
	public String getName(){
		return name;
	}

}
