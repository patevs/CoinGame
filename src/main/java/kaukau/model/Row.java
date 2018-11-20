package kaukau.model;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The Row class stores a list of tiles representing a row of teh game board.
 * The Row class is used for XML binding.
 * Since JAXB cannot marshal and unmarshal Tile [][], the game map has another method to turn the Tile [][] to
 * an list of Row objects.
 * @author Shaika*/
@XmlRootElement
 public class Row {
	private ArrayList<Tile> row; //list of tiles representing a row

	public Row(){
		row = new ArrayList<Tile>();
	}
	
	/**
	 * sets the list of tiles with the parameter.
	 * 
	 * @param ArrayList<Tile> row2
	 * */
	public void setRow(ArrayList<Tile> row2) {
		this.row = row2;
	}

	/**
	 * gets the list of tiles
	 * 
	 * @return ArrayList<Tile> row
	 * */
	@XmlElementWrapper(name = "getRows")
	@XmlElements({ @XmlElement(name = "tile") })
	public ArrayList<Tile> getRows() {
		return this.row;
	}
}
