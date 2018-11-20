package kaukau.model;

import java.io.Serializable;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * /**
 * A container is an item able hold pickupable type items.
 * Containers are storage for a coinbox, player's inventory...etc.
 * @author Vivienne Yapp, 300339524
 *
 */
@SuppressWarnings("serial")
@XmlRootElement
public class Container implements Item, Serializable{

	private String name;
	private int storageLimit;
	private ArrayList <PickupableItem> storage =  new ArrayList <PickupableItem>( );

	public Container (String name){
		this.name = name;
	}

	/**
	 * Constructor for load and save to XML file purpose.
	 */
	@SuppressWarnings("unused")
	public Container(){
		this(null);
	}

	/**
	 * Protected method to set the max storage amount which is determined when the container is made.
	 * @param amount the limit size of this container
	 */
	protected void setAmount (int amount){
		if(amount > 0){
			this.storageLimit = amount;
		}
	}

	/**
	 * Adds a pickupable item to the container.
	 * @param item the item to add to container.
	 * @return true if the item is successfully added to the container, otherwise false.
	 */
	public boolean addItem(Item item){
		if (item != null && item instanceof PickupableItem){
			if (this.storage.size() < this.storageLimit){
				storage.add((PickupableItem) item);
				return true;
			}
		}
		return false;
	}

	/**
	 * Removes a pickupable item from a container.
	 * @param item the item to be removed
	 * @return true if successfully remove from the container, otherwise false.
	 */
	public boolean removeItem(Item item){
		if (item != null && storage.contains(item)){
			storage.remove((PickupableItem) item);
			return true;
		}
		return false;
	}

	/**
	 * Removes a pickupable item from this container using index number of ArrayList.
	 * @param index the index number of item in the inventory.
	 * @return true if the item is successfully added, otherwise false if the index
	 * is negative value or equals and larger than the size of the container.
	 */
	public boolean removeItem(int index){
		if (index < this.storage.size() && index >= 0){
			storage.remove(index);
			return true;
		}
		return false;
	}

	/**
	 * Get the list of items of this container.
	 * @return the list of items of this container.
	 */
	public ArrayList <PickupableItem> getStorage(){
		return this.storage;
	}

	/**
	 * Returns the item at this index number in the storage.
	 * @param index the index number of the item in the storage
	 * @return an item if index is within storage size, otherwise null
	 */
	public Item getItem(int index){
		if (index >= storageLimit && index >= storage.size()) return null;
		return this.storage.get(index);
	}

	/**
	 * Get the size that used in this container.
	 * @return the size of this container
	 */
	public int getStorageUsed(){
		return this.storage.size();
	}

	/**
	 * Returns the limit size of this container.
	 * @return the maximum size of this container.
	 */
	public int getStorageLimit(){
		return this.storageLimit;
	}

	/**
	 * Check if this container is full or not.
	 * @return true if the container is full, otherwise false.
	 */
	public boolean isStorageFull(){
		return storage.size() >= storageLimit;
	}

	/**
	 * Return the name of this item.
	 * @return the name of this item
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set a list of items to this container.
	 * This method uses for load from XML file.
	 * @param items all the pickupableItem to add
	 */
	public void setStorage(ArrayList<PickupableItem> items) {
		this.storage = items;
	}

}
