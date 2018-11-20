package kaukau.view;

public class Wall extends Block {

	// Field for type of tile
	private char wallType;
	
	public Wall(char wallType, int x, int y){
		super(x,y);
		this.wallType = wallType;
	}
	
	/*
	 * Setter methods
	 */
	public void setWallType(char wType){
		this.wallType = wType;
	}

	/*
	 * Getter methods
	 */
	public char getWallType() { return wallType; }	
}
