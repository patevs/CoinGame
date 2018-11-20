package kaukau;

public class NetworkTest {

//	@Test
//	public void start(){
//		EventQueue.invokeLater(new Runnable(){
//			@Override
//			public void run() {
//		    	Server server = HelperMethods.server();
//		    	GameWorld mainGame = server.getGame();
//				ApplicationWindow aw = null;
//				aw = new ApplicationWindow(new GameWorld());
//				aw.setVisible(true);
//				Client pClient = new Client(aw);
//		    	pClient.start();
//		    	aw.rw.addClient(pClient);
//		    	assertEquals("Player offline should have a uid of 0.", 0, pClient.getUID());
//
//		    	Tile tile = aw.getGame().getAllPlayers().get(0).getLocation();
//		    	assertEquals(2, tile.X());
//		    	assertEquals(2, tile.Y());
//		    	aw.getGame().movePlayer(pClient.getUID(), Direction.NORTH);
//		    	Tile tile2 = aw.getGame().getAllPlayers().get(0).getLocation();
//		    	assertEquals(2, tile.X());
//		    	assertEquals(1, tile.Y());
//			}
//		});
//	}
}
