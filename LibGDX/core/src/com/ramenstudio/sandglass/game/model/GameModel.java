package com.ramenstudio.sandglass.game.model;

import java.util.*;

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
  //flag for whether or not we are in the overworld
  private boolean in_overworld;

  //the exit gate in this level
  private Gate gate;
  //array of objects in this level (land, resources, etc)
  private List<ShipPiece> shipPieces = new ArrayList<ShipPiece>();
  //array of resources in this level
  private List<Resource> resources = new ArrayList<Resource>();
  
  // Game state
  private GameState gameState = GameState.PLAYING;
  
  // Current level
  private int gameLevel;
  
  // Max game levels. Minimum level is 1.
  private static final int MAX_LEVEL = 16;
  
  /**
   * Initializer.
   */
  public GameModel() {
    pieces = 1;
    collected_pieces = 0;
    in_overworld = true;
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
  public boolean allPiecesCollected(){
    return pieces == collected_pieces;
  }
  
  /**Increments the number of pieces the player has collected*/
  public void collectPiece(){
    collected_pieces ++;
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
  
  /**@return the Gate object for this level*/
  public Gate getGate(){
	  return gate;
  }
  
  /**@param the Gate object for this level*/
  public void setGate(Gate g){
	  gate = g;
	  g.setTextureLevel(gameLevel);
  }
  
  
  @Override
  /**Draws all objects in the level to the Game Canvas
   * @param canvas - the GameCanvas to draw to
   * */
  public void draw(GameCanvas canvas) {
    // Tells all objects to render themselves with the given canvas.
      for (ShipPiece s : getShipPieces()) {
        s.draw(canvas);
      }
      
      for (Resource r: getResources()){
    	  r.draw(canvas);
      }
    
    gate.draw(canvas);
    //gate.draw(canvas);
  }

  /**
   * @return the gameState
   */
  public GameState getGameState() {
    return gameState;
  }

  /**
   * @param gameState the gameState to set
   */
  public void setGameState(GameState gameState) {
    this.gameState = gameState;
  }

  public List<ShipPiece> getShipPieces() {
	return shipPieces;
  }

  /**
   * @return the gameLevel
   */
  public int getGameLevel() {
    return gameLevel;
  }

  /**
   * Sets the game level to the given level. If the game level is out of range,
   * then we do nothing and return false. Otherwise return true.
   * @param gameLevel the gameLevel to set
   */
  public boolean setGameLevel(int gameLevel) {
    if (gameLevel > MAX_LEVEL) {
      return false;
    }
    
    this.gameLevel = gameLevel;
    
    return true;
  }
  
  /**get list of resources*/
  public List<Resource> getResources() {
	  return resources;
  }
  
  /**set the list of resources*/
  public void setResources(List<Resource> r){
	  resources = r;
  }
}

