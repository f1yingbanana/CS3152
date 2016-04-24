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
  //amount of time remaining in overworld
  private int overtime;
  //amount of time remaining in underworld
  private int undertime;
  //max amount of time that can be in the hourglass
  private int maxtime = 4000;
  //flag for whether or not we are in the overworld
  private boolean in_overworld;
  //flag for whether this level has been completed or not
  private boolean completed;
  //the exit gate in this level
  private Gate gate;
  //array of objects in this level (land, resources, etc)
  private List<ShipPiece> shipPieces = new ArrayList<ShipPiece>();
  
  // Game state
  private GameState gameState = GameState.PLAYING;
  
  // Current level
  private int gameLevel;
  
  // Max game levels. Minimum level is 1.
  private static final int MAX_LEVEL = 10;
  
  /**
   * Initializer.
   */
  public GameModel() {
    pieces = 1;
    overtime = 0;
    undertime = 3600;
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
  public boolean allPiecesCollected(){
    return pieces == collected_pieces;
  }
  
  /**Increments the number of pieces the player has collected*/
  public void collectPiece(){
    collected_pieces ++;
  }
  
  public int getMaxTime(){
	  return maxtime;
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
  
  /**Decreases the sand in the sandglass by amount specified. Takes from the top
   * of the sandglass. For when a monster hits the player
   * @param amount the integer amount of sand to be removed
   */
  public void takeTime(int amount){
	  if (overtime + undertime == 0){
		  return;
	  }
	  if (in_overworld){
		  overtime = overtime - amount;
	  } else {
		  undertime = undertime - amount;
	  }
  }
  
  /**Increases the sand in the sandglass by amount specified. Adds to the top of
   * the sandglass. For collecting a sand resource
   * @param amount the integer amount of sand to be added
   */
  public void giveTime(int amount){
	  if (overtime + undertime + amount >= maxtime){
		  return;
	  }
	  if (in_overworld){
		  overtime = overtime + amount;
	  } else {
		  undertime = undertime + amount;
	  }
  }
  
  /**Returns true if we've run out of time*/
  public boolean outOfTime(){
	  return !in_overworld && undertime <= 0;
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
  
}

