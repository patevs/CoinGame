package kaukau.model;

/**
 * Item interface is the backbone of all the items in the game the player can interact with
 * ie collectible objects, puzzles, clues, doors and containers such as the players bag and lockers.*/
public interface Item{

	/**
	 * Sets the items location to be the argument
	 * */
	public void setLocation(Tile loc);

	/**
	 * Gets the items location
	 * @return Tile*/
	public Tile getLocation();

	/**
	 * Gets the item name
	 * @return String name
	 * */
	public String getName();

}
