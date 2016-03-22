package com.ramenstudio.sandglass.game.model;

import com.badlogic.gdx.math.Vector2;
import com.ramenstudio.sandglass.game.view.GameCanvas;
import com.ramenstudio.sandglass.util.Drawable;

/**
 * The root model class for storing all information in a single level.
 * 
 * @author flyingbanana
 * @author Nathaniel Hunter
 */
public class GameModel implements Drawable {
	
	//number of ship pieces in this level
	private int pieces;
	//number of pieces the player has collected in this level
	private int collected_pieces;
	//amount of time remaining in overworld
	private int overtime;
	//amount of time remaining in underworld
	private int undertime;
	//flag for whether or not we are in the overworld
	private boolean in_overworld;
	//flag for whether this level has been completed or not
	private boolean completed;
	//position of the exit gate in this level
	private Vector2 gate_pos;
	//array of objects in this level (land, resources, etc)
	private GameObject[] objects = {};
	
	
  /**
   * Initializer.
   */
  public GameModel() {
	  pieces = 1;
	  overtime = 60;
	  undertime = 0;
	  collected_pieces = 0;
	  in_overworld = true;
	  completed = false;
  }
  
  /**GETTERS + SETTERS */
  /**@return number of ship pieces to be collected in this level*/
  public int getNumberOfPieces(){
	  return pieces;
  }
  
  /**Sets the number of ship pieces that will be in this level
   * @param i number of pieces*/
  public void setNumberOfPieces(int i){
	  pieces = i;
  }
  
  /**@return the number of ship pieces the player has collected thus far*/
  public int getCollectedPieces(){
	  return collected_pieces;
  }
  
  /**Sets the number of pieces the player has collected thus far
   * @param int i number of pieces*/
  public void setCollectedPieces(int i){
	  collected_pieces = i;
  }
  
  /**@return true of all pieces have been collected, false otherwise*/
  public boolean areAllPiecesCollected(){
	  return pieces == collected_pieces;
  }
  
  /**Increments the number of pieces the player has collected*/
  public void collectPiece(){
	  collected_pieces ++;
  }
  
  /**@return the amount of time remaining in the overworld*/
  public int getOverworldTime(){
	  return overtime;
  }
  
  /**Sets the amount of time remaining in the overworld
   * @param i the amount of time*/
  public void setOverworldTime(int i){
	  overtime = i;
  }
  
  /**@return the amount of time remaining in the underworld*/
  public int getUnderworldTime(){
	  return undertime;
  }
  
  /**Sets the amount of time remaining in the underworld
   * @param int i the amount of time*/
  public void setUnderworldTime(int i){
	  undertime = i;
  }
  
  /**Appropriately increments the amount of time remaining in the overworld
   * and in the underworld. I.e., if we are in the overworld, the amount of overworld
   * time is decreased and the amount of underworld time is increased, vice versa if 
   * we are in the underworld.*/
  public void incrementTime(){
	  if (in_overworld){
		  overtime --;
		  undertime ++;
	  } else {
		  overtime ++;
		  undertime --;
	  }
  }
  
  /**@return true if we are in the overworld, false if we are in the underworld*/
  public boolean isInOverworld(){
	  return in_overworld;
  }
  
  /**Sets whether we are in the underworld or the overworld
   * @param in_over true if we are in the overworld, false if we are in the underworld*/
  public void setWorldPosition(boolean in_over){
	  in_overworld = in_over;
  }
  
  /**@return true if the level is completed*/
  public boolean isLevelCompleted(){
	  return completed;
  }
  
  /**Sets whether the level has been completed
   * @param t true if the level is completed, false otherwise*/
  public void setLevelCompleted(boolean t){
	  completed = t;
  }
  
  /**@return the Vector2 position of the exit gate in this level*/
  public Vector2 getGatePosition(){
	  return gate_pos;
  }
  
  /**Sets the position of the exit gate in this level
   * @param v the Vector2 position of the gate*/
  public void setGatePosition(Vector2 v){
	  gate_pos = v;
  }
  
  /**@return an array of all land, resource, and piece objects in this level*/
  public GameObject[] getObjects(){
	  return objects;
  }
  
  /**Sets the list of all land, resource, and piece objects in this level
   * @param o a GameObject[] containing land, piece, and resource objects*/
  public void setGameObjects(GameObject[] o){
	  objects = o;
  }
  
  
	@Override
	/**Draws all objects in the level to the Game Canvas
	 * @param canvas - the GameCanvas to draw to
	 * */
	public void draw(GameCanvas canvas) {
    // Tells all objects to render themselves with the given canvas.
		if (objects.length > 0){
		for (GameObject o : objects){
			o.draw(canvas);
		}
	}
	}
}

