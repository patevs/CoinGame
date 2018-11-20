package kaukau.model;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("serial")
@XmlRootElement
public class Room implements Serializable{

	@XmlElement(name="name")
	private String name;
	
	private int startX;
	
	private int startY;
	private ArrayList<Door> doors;

	/**
	 * Create a room object.
	 * @param name the name of this room
	 * @param x the start x position in the 2d array
	 * @param y the start y position in the 2d array
	 */
	public Room(String name, int x, int y){
		this.name = name;
		this.startX = x;
		this.startY = y;
		doors = new ArrayList<Door>();
	}

	/**
	 * Constructor for load and save to XML file purpose.
	 */
	public Room(){
		this(null, -1, -1);
	}

	/**
	 * Add a door to this room object
	 * @param door the door to add
	 */
	public void addDoor(Door door){
		this.doors.add(door);
	}

	/**
	 * Set the start x position of this room.
	 * @param x the position in the 2D array
	 */
	public void setStartX(int x){
		startX = x;
	}

	/**
	 * Set the start y position of this room
	 * @param y the y position in the 2D array
	 */
	public void setStartY(int y){
		startY = y;
	}

	/**
	 * Get the start x position of the room
	 * @return the start x position
	 */
	@XmlElement(name="startX")
	public int getStartX(){
		return startX;
	}

	/**
	 * Get the start y position of the room
	 * @return the start y position
	 */
	@XmlElement(name="startY")
	public int getStartY(){
		return startY;
	}

	/**
	 * Get the list of doors of this room.
	 * @return a list of doors
	 */
	public ArrayList<Door> getDoors(){
		return doors;
	}

	/**
	 * Return the start point of this room
	 * @return the point of x and y
	 */
	public Point getStartPoint(){
		return new Point(startX, startY);
	}

	/**
	 * Return the name of this item.
	 * @return the name of this item
	 */
	public String getName(){ return name; }
}
