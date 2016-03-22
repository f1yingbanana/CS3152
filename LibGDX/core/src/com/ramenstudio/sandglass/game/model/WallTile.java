package com.ramenstudio.sandglass.game.model;

/**
 * A wall tile that has a specific collision fixture set up.
 * 
 * @author Jiacong Xu
 */
public class WallTile extends GameObject {
  public enum WallType {
    TOPLEFT, TOP, TOPRIGHT, LEFT, CENTER, RIGHT, BOTLEFT, BOT, BOTRIGHT
  }
  
  /**
   * Default constructor. Creates a tile with collision corresponding to the 
   * given wall type.
   * 
   * @param type specifies fixture geometry.
   */
  public WallTile(WallType type) {
    
  }
}
