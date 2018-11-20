package kaukau.storage;

import java.io.File;
import javax.xml.bind.JAXBContext;

import javax.xml.bind.JAXBException;

import javax.xml.bind.Unmarshaller;

import kaukau.model.GameMap;
import kaukau.model.Player;

/**This class is used to unmarshal the xml files of player and game map into player and GameMap objects.
 * Uses JAXB to create instances with the players id to pick which file for which player.
 * 
 * @author Shaika*/
public class JAXBXmlToJava {	

	/**
	 * Unmarshals the player xml file depending on the id passed in.
	 * instantizes a new player object from the xml and returns it.
	 * 
	 * @return Player player
	 * @param int playerID
	 * */
	public Player unmarshalPlayer(int playerID) {
		Player player = null;
		try {
			// create JAXB context and initializing Marshaller
			JAXBContext jaxbContext = JAXBContext.newInstance(Player.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			// specify the location and name of xml file to be read
			File XMLfile = new File("Player"+playerID+".xml");
			//File XMLfile = new File("Player.xml");
			// this will create Java object - country from the XML file
			player = (Player) jaxbUnmarshaller.unmarshal(XMLfile);
		} catch (JAXBException e) {
			// some exception occured
			e.printStackTrace();
		}

		return player;

	}

	/**
	 * Unmarshals the gameMap xml file depending on the id passed in.
	 * instantizes a new gameMap object from the xml and returns it.
	 * 
	 * @return GameMap map
	 * @param int playerID
	 * */
	public GameMap unmarshalMap(int playerID) {
		GameMap map = null;
		try {
			// create JAXB context and initializing Marshaller
			JAXBContext jaxbContext = JAXBContext.newInstance(GameMap.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			// specify the location and name of xml file to be read
			 File XMLfile = new File("Map"+playerID+".xml");
			//File XMLfile = new File("Map.xml");
			// this will create Java object - country from the XML file
			map = (GameMap) jaxbUnmarshaller.unmarshal(XMLfile);
		} catch (JAXBException e) {
			// some exception occured
			e.printStackTrace();
		}
		return map;
	}

}
