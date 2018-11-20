package kaukau.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class represent a Door object that can placed on GameMap's tile.
 * @author Vivienne Yapp, 300339524
 *
 */

@SuppressWarnings("serial")
@XmlRootElement
public class Door implements Item, Serializable{

	private final int keyCode;
	private boolean locked;

	/**
	 * Create a door with a key code and the location of the door
	 * @param key code to match with a key if it is locked
	 */
	public Door(int key){		
		this.keyCode = key;
		if (keyCode > 0) this.locked = true;
		else this.locked = false;
	}

	public Door(){
		this(-1);
	}

	/**
	 * Check if this door is locked or not
	 * @return true if it is locked, otherwise false.
	 */
	@XmlElement(name="isLocked")
	public boolean isLocked(){
		return locked;
	}

	/**
	 * Check if the player can used this key to open a door
	 * @param item the key object
	 * @return true if the item is a Key type and the door is locked, otherwise false
	 */
	public boolean lockOrUnlock(Item item){
		if (item instanceof Key && locked){
			Key key = (Key) item;
			if (keyCode == key.getCode()){
				locked = false;
				return true;
			}
		}
		return false;
	}

	/**
	 * Return the door code that need to match with a key.
	 * @return the door code of this door
	 */
	@XmlElement(name="doorCode")
	public int getDoorCode(){
		return keyCode;
	}

	/**
	 * Return
	 */
	public String toString(){
		return "Door";
	}

	/**
	 * Return the name of this item.
	 * @return the name of this item
	 */
	public String getName() {
		return "Door";
	}

}
