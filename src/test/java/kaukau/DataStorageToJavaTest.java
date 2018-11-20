package kaukau;

import kaukau.model.Coin;
import kaukau.model.CoinBox;
import kaukau.model.GameMap;
import kaukau.model.Key;
import kaukau.model.PickupableItem;
import kaukau.model.Player;
import kaukau.model.Row;
import kaukau.model.Tile;
import kaukau.storage.JAXBXmlToJava;

public class DataStorageToJavaTest {

	public static void testUnmarshalledPlayer(Player player) {
		if (player != null) {
			System.out.println("Player name: " + player.getName());
			System.out.println("Player id: " + player.getUserId());
			System.out.println("Player direction: " + player.getfacingDirection());
			System.out.println("Player location: " + player.getLocation().toString() + " with tile type: "
					+ player.getLocation().getTileType());
			System.out.println("Player inventory limit: " + player.getStorageSize());
			for (PickupableItem p : player.getInventory()) {
				if (p instanceof Key) {
					Key k = (Key) p;
					System.out.println("Player inventory key code: " + k.getCode());
				} else if (p instanceof CoinBox) {
					CoinBox cb = (CoinBox) p;
					for (PickupableItem pi : cb.getStorage()) {
						Coin c = (Coin) pi;
						System.out.println("Coin : " + c.getAmount());
					}
					System.out.println("Player coinbox ammount: " + cb.totalCoins());
					System.out.println("Player coinbox is full?: " + cb.isStorageFull());
				}
			}
		}
	}

	public static void testUnmarshalledMap(GameMap map) {
		if (map != null) {

			for (Row r : map.getBoardTiles()) {				
				if (r == null) {
					System.out.println("row is null!");
				} else {					
					for(Tile t : r.getRows()){
						if(t.containsPickupItem()){
							System.out.println("Tile location: "+t.toString());
							System.out.println("Tile type: "+t.getTileType());						
							System.out.println("Tile item: "+t.getItem().getName());
						}						
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		JAXBXmlToJava unmarshaling = new JAXBXmlToJava();
		Player player = unmarshaling.unmarshalPlayer(1);
		//testUnmarshalledPlayer(player);
		GameMap map = unmarshaling.unmarshalMap(1);
		testUnmarshalledMap(map);
	}


}
