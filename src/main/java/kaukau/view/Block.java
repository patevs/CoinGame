package kaukau.view;

public abstract class Block {
	
	// Fields for x & y position of tile
	private final int x;
	private final int y;
	
	public Block(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	/*
	 * Getter methods
	 */
	public int X() { return x; }
	public int Y() { return y; }
}
