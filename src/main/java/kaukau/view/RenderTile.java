package kaukau.view;

import kaukau.model.Item;
import kaukau.model.Player;

public class RenderTile extends Block {
	// Field for type of tile
	private int tileType;
	// field for the the item the tile holds or null if none
	private Item item = null;
	// field for the the player on this tile or null if none
	private Player player = null;
	
	public RenderTile(int tileType, int x, int y) {
		super(x,y);
		this.tileType = tileType;
	}
	
	/*
	 * Setter methods
	 */
	public void setTileType(int tType){
		this.tileType = tType;
	}
	public void setItem(Item i){
		this.item = i;
	}
	public void setPlayer(Player p) {
		this.player = p;
	}

	/*
	 * Getter methods
	 */
	public int getTileType() { 
		return tileType;	
	}
	public Item getItem() { 
		if(item!=null){ return item; }
		else { return null; }
	}
	public Player getPlayer(){
		if(player!=null){ return player; }
		else { return null; }
	}


}
