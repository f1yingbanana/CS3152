package com.ramenstudio.sandglass;

import com.badlogic.gdx.math.Vector2;

/**
 * The most generic object in a game. It represents an object (whether rendered
 * or not) in the game.
 * 
 * @author Jiacong Xu
 */
public class GameObject {
  // The world position of this object.
  private Vector2 position = new Vector2();
  
  // The world rotation of this object.
  private float rotation = 0;
  /**
   * Draw this object on the canvas.
   * 
   * @param canvas  the current active game canvas.
   */
  public void draw(GameCanvas canvas) {
    
  }
}
