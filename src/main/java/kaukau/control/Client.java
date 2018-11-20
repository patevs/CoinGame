package kaukau.control;

/**
 * Client thread to communicate between server and players.
 * @author Maria Legaspi, 30030499
 */

import kaukau.model.*;
import kaukau.view.*;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;

/**
 * A client to connects
 */
public class Client extends Thread {
	// network information
	private String address = "127.0.0.1";
	private Socket sock;
	private boolean connected;
	// stream information
	private ObjectInputStream input;
	private ObjectOutputStream output;
	// game information
	private GameWorld game;
	private ApplicationWindow aw;
	// player setup information
	private int uid;
	private boolean initialRun = true;
	// action commands
	public static final int pickUpItem = 1;
	public static final int dropItem = 2;
	public static final int leaveGame = 3;


	/**
	 * Creates a client for the player.
	 */
	public Client(ApplicationWindow aw){
		// sets references to the game and ui window
		this.aw = aw;
		this.game = aw.getGame();
		// sets networking fields
        setupSocket();
	}

	/**
	 * Sets the socket and streams to communicate with server.
	 */
	private void setupSocket(){
		try{
			// sets networking fields
	        sock = new Socket(address, Server.portNumber);
	        output = new ObjectOutputStream(sock.getOutputStream());
	        input = new ObjectInputStream(sock.getInputStream());
	        connected = true;
	    } catch(EOFException e){
			// server not running so in single player mode
		} catch(ConnectException e){
			// could not connect to server so running single player mode
		} catch(IOException e){
			e.printStackTrace();
		}
	}

	@Override
	public void run(){
		try {
			// single player game
			if(!connected){
				// keep trying to connect to server
				setupSocket();
				return;
			}
			// multiplayer game
			if(initialRun){
				// checks if client is connected to the server
				boolean accepted = false;
				if(input!= null)
					accepted = input.readBoolean();
				// if there were too many players
				if(!accepted){
					setupSocket();
					return;
				}
				uid = input.readInt();
				initialRun = false;
			}
			// play game
			boolean closed = false;
			while(!closed){
				// wait for game updates from server
				if(sock.isClosed())
					break;
				game.fromByteArray((byte[])input.readObject());
				// update rendering
				aw.setGame(game);
				//try {
					//Thread.sleep(200);
				//} catch (InterruptedException e1) {	}
				aw.repaint();
			}
		} catch(ConnectException e){
			// not connected to server
		} catch(SocketException e){
			// socket closed
		} catch(IOException e) {
			// server message not received
		} catch (ClassNotFoundException e) {
			// in case of casting issue
		}
	}

	/**
	 * Closes the sock when the server is closed.
	 */
	public void closeClientSock(){
		try{
			if(sock!=null && !sock.isClosed()){
				sock.close();
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}

	/**
	 * Sends the action command to the server.
	 * @param code
	 */
	public void sendAction(int code){
		try {
			if(connected){
				output.writeInt(uid); // uid of player doing action
				output.writeInt(code); // action
				output.flush();
			}
		} catch (IOException e) {
			// problem with sending to the server
			e.printStackTrace();
		}
	}

	/**
	 * Sends action command to server with the index of a given item in the inventory.
	 * @param code
	 */
	public void sendAction(int code, int index){
		try {
			if(connected){
				output.writeInt(uid);
				output.writeInt(code); // action with item
				output.writeInt(index); // index of item in inventory
				output.flush();
			}
		} catch (IOException e) {
			// problem with sending to the server
			e.printStackTrace();
		}
	}

	/**
	 * Returns the player's uid.
	 * @return
	 */
	public int getUID(){
		return uid;
	}
}