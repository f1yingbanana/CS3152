package com.ramenstudio.sandglass;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

/**
 * The root model class for storing all information in a single level.
 * 
 * @author Jiacong Xu
 * @author Nathaniel Hunter
 */
public class GameModel implements Drawable {
	// Array of GameObjects in this level
	private GameObject[] objects;
	// The amount of time left in the overworld half of the sandglass.
	private int overtime;
	// The amount of time left in the underworld half of the sandglass.
	private int undertime;
	// The number of ship pieces to be found on this level
	private int pieces;
	// The number of ship pieces the player has collected
	private int collected_pieces;
	
 	/**Initializer.*/
	public GameModel() {
	}
  
	/**GETTERS & SETTERS*/
	
	/**Returns an array of all GameObjects in the level*/
	public GameObject[] getObjects(){
		return objects;
	}
	
	/**Returns the amount of time left in the overworld*/
	public int getOverworldTime(){
		return overtime;
	}
	
	/**Returns the amount of time left in the underworld*/
	public int getUnderworldTime(){
		return undertime;
	}
	
	/**Returns the number of ship pieces to be found on this level*/
	public int getNumberOfPieces(){
		return pieces;
	}
	
	/**Returns the number of of ship pieces the player has collected do far*/
	public int getNumberCollected(){
		return collected_pieces;
	}
	
	/**Decrements the amount of time left in either the overworld or the underworld
	 * @param overworld - boolean, true if decrementing time from the overworld,
	 * 			false if from the underworld
	 * */
	public void decrementTime(boolean overworld){
		if (overworld){
			overtime--;
		} else {
			undertime--;
		}
	}
	
	/**Increments the amount of time left in either the overworld or the underworld
	 * @param overworld - boolean, true if incrementing time from the overworld,
	 * 			false if from the underworld
	 * */
	public void incrementTime(boolean overworld){
		if (overworld){
			overtime++;
		} else {
			undertime++;
		}
	}
	
	/**OTHER PUBLIC METHODS*/
	
	@Override
	/**Draws all objects in the level to the Game Canvas
	 * @param canvas - the GameCanvas to draw to
	 * */
	public void draw(GameCanvas canvas) {
    // Tells all objects to render themselves with the given canvas.
		for (GameObject o : objects){
			o.draw(canvas);
		}
	}
	
	/**Updates the count of ship pieces that the player has collected*/
	public void collectPiece(){
		collected_pieces++;
	}
	
	/**Returns true if player has collected all necessary ship pieces*/
	public boolean areAllPiecesCollected(){
		return collected_pieces == pieces;
	}
	
	/**PRIVATE METHODS*/
	/**Populates the objects array of the GameModel*/
	private void populateObjects(){
		
	}
}
