package kaukau.control;

/**
 * Server to listen to actions from players and communicate updates to other players.
 * @author Maria Legaspi, 30030499
 */

import kaukau.model.*;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server{
	// port as all clients should be able to access this port
	public static int portNumber = 4000;
	// server sockets
	private ServerSocket listener; // accepts clients
	private Thread listeningThread;
	// client sockets
	private static HashMap<Integer, Socket> sockets = new HashMap<Integer, Socket>();
	private static HashMap<Integer, ObjectInputStream> in = new HashMap<Integer, ObjectInputStream>();
	private static HashMap<Integer, ObjectOutputStream> out = new HashMap<Integer, ObjectOutputStream>();
	// game
	private static GameWorld game;



	/**
	 * Creates the listening socket and the two threads to connect clients and handle player actions.
	 * @param game
	 */
	public Server(){
		game = new GameWorld();
        try {
			// start connecting and listening to clients
			listener = new ServerSocket(portNumber);
			listeningThread = makeListeningThread();
			listeningThread.start();
			System.out.println("Listening");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Constructs a thread to handle player commands.
	 * @return
	 */
    public Thread makeCommandThread(final int player){
		Thread commandThread = new Thread() {
		    @SuppressWarnings("deprecation")
			public void run() {
		    	try{
			    	while(atleastOneConnection()) {
						while(!game.isOver()) {
							Socket sock = sockets.get(player);
							// read input from player
							ObjectInputStream input = getInputStream(sock);
							// reads in commands
							int uid = input.readInt();
							int act = input.readInt();
							System.out.println("Player: " + uid + ", action: " + act);
							// update game with player action
							switch(act) {
								// move right
								case KeyEvent.VK_RIGHT:
								case KeyEvent.VK_KP_RIGHT:
									game.movePlayer(uid, Direction.EAST);
									break;
								// move left
								case KeyEvent.VK_LEFT:
								case KeyEvent.VK_KP_LEFT:
									game.movePlayer(uid, Direction.WEST);
									break;
								// move up
								case KeyEvent.VK_UP:
								case KeyEvent.VK_KP_UP:
									game.movePlayer(uid, Direction.NORTH);
									break;
								// move down
								case KeyEvent.VK_DOWN:
								case KeyEvent.VK_KP_DOWN:
									game.movePlayer(uid, Direction.SOUTH);
									break;
								// pick up item
								case Client.pickUpItem:
									game.pickupAnItem(uid);
									break;
								// drop item
								case Client.dropItem:
									int index = input.readInt();
									game.dropAnItem(uid, index);
									break;
								// remove this player from the game and server
								case Client.leaveGame:
									sockets.remove(uid);
									in.remove(uid);
									out.remove(uid);
									game.removePlayer(uid);
									this.stop();
									System.out.println("Player " + uid + " has left the game.");
									return;
							}
							// send back to clients
							updateAll();
						}
					}
		    	} catch(IOException e){
//		    		e.printStackTrace();
		    	}
		    }
		};
		return commandThread;
    }

    /**
     * Returns a thread to connect clients.
     * @return
     */
	public Thread makeListeningThread(){
		Thread listeningThread = new Thread() {
		    public void run() {
		        try{
		            while(true){
		            	// gets socket and streams of the new player
						Socket socket = listener.accept();
	            		ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
	            		ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
						// if there are more than two players then don't accept the new player
		            	if(sockets.size() > 2){
		    				output.writeBoolean(false);
				        	output.flush();
		            	}
		            	// otherwise accept a new player
		            	else{
		            		output.writeBoolean(true); // tells client they're playing on the server
		            		// stores access to each player's socket and streams
							int uid = game.addPlayer();
		            		sockets.put(uid, socket);
							out.put(uid, output);
							in.put(uid, input);
							System.out.println("New socket: " + socket.getPort() + ", UID: " + uid);
							// send the client's uid to the client
							output.writeInt(uid);
				        	// send new game to all clients
							Thread commandThread = makeCommandThread(uid);
							commandThread.start();
				        	// send new game to all players
				        	updateAll();
		            	}
		            }
		        } catch(IOException e){
		        	e.printStackTrace();
//		        	System.out.println("Failed to accept client on port " + portNumber + "\n");
		        } catch(Exception e){
		        	e.printStackTrace();
		        } finally {
		        	try{
		        		listener.close();
		        	}
		        	catch(IOException e){
		        		e.printStackTrace();
		        	}
		        }
		    }
		};
		return listeningThread;
	}

	/**
     * Sends updated game to all clients.
     * @param message
     */
    public static void updateAll(){
    	try{
          for(Socket sock: sockets.values()){
        	  if(sock.isClosed())
        		  continue;
        	  // sends game as a byte array to each client
        	  ObjectOutputStream output = getOutputStream(sock);
        	  output.writeObject(game.toByteArray());
        	  output.flush();
        	  System.out.println("Updated sent updated game to player");
          }
    	}
    	catch(IOException e){
    		e.printStackTrace();
//    		System.out.println("Failed to create buffered writer.\n");
    	}
    }

    /**
     * Returns the input stream of a given sock.
     * @param sock
     * @return
     */
    private static ObjectInputStream getInputStream(Socket sock){
    	for(Integer i: sockets.keySet())
    		if(sockets.get(i).equals(sock))
    			return in.get(i);
    	return null;
    }

    /**
     * Returns the output stream of a given sock.
     * @param sock
     * @return
     */
    private static ObjectOutputStream getOutputStream(Socket sock){
    	for(Integer i: sockets.keySet())
    		if(sockets.get(i).equals(sock)){
    			return out.get(i);
    		}
    	return null;
    }

	/**
	 * Checks there's at least one socket that's connected.
	 * @return
	 */
	private static boolean atleastOneConnection() {
		for (Socket s : sockets.values()) {
			if (s.isConnected()) {
				return true;
			}
		}
		return false;
	}

	/**
     * Closes all sockets.
     */
    public void closeAll(){
    	for(Socket sock: sockets.values()){
    		try {
				sock.close();
			} catch (IOException e) {
				// error closing socket
			}
    	}
    }

    /**
     * Returns the main game.
     * @return
     */
    public static GameWorld getGame(){
    	return game;
    }

    /**
     * Starts a game and associates it with a server.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException{
    	// make and run a server
    	new Server();
	}
}
