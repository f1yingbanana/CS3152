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
   * @return a copy of this object's world position.
   */
  public Vector2 getPosition() {
    return position.cpy();
  }
  
  /**
   * Sets the position of this game object to the given position.
   * 
   * @param position  the position we want to set to.
   */
  public void setPosition(Vector2 position) {
    this.position.set(position);
  }

  /**
   * @return the rotation of this object in degrees, capped from 0 to 360.
   */
  public float getRotation() {
    return rotation;
  }
  
  /**
   * Sets the rotation of this game object to the given rotation.
   * 
   * @param rotation  the rotation we want to set to in degrees. If out of range
   * we will modulo it to between 0 and 360.
   */
  public void setRotation(float rotation) {
    float r = rotation % 360;
    
    if (rotation < 0) {
      r += 360;
    }
    
    this.rotation = r;
  }
}
