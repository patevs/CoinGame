package kaukau.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class represents a Key object that can be pickup by the player.
 * @author Vivienne Yapp, 300339524
 *
 */
@SuppressWarnings("serial")
@XmlRootElement
public class Key extends PickupableItem implements Serializable {

	private int code;

	/**
	 * Create a key object.
	 * @param key the key code
	 */
	public Key(int key) {
		super("Key");
		this.code = key;
	}

	/**
	 * Constructor for load and save to XML file purpose.
	 */
	public Key (){
		this(-1);
	}

	/**
	 * Get the key code of this key.
	 * @return a code of this key
	 */
	@XmlElement(name = "getCode")
	public int getCode() {
		return code;
	}

	/**
	 * Set the code for this key.
	 * @param code the key code
	 */
	public void setCode(int code){
		this.code = code;
	}

}
