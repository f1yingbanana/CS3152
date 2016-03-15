package com.ramenstudio.sandglass;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

/**
 * The most generic object in a game. It represents an object (whether rendered
 * or not) in the game.
 * 
 * @author Jiacong Xu
 * @author Nathaniel Hunter
 */
public class GameObject implements Drawable{
  // The world position of this object.
  private Vector2 position;
  // The world rotation of this object.
  private float rotation = 0;
  // The size of this object.
  private Vector2 size;
  // The texture of this object
  private Texture texture;
  
  /**INITIALIZERS*/
  public GameObject(){
	  this.position = new Vector2();
	  this.size = new Vector2();
	  this.texture = null;
  }
  /**Creates a GameObject with rotation 0 and:
   * @param p - the position of the object
   * @param s - the size of the object
   * @param t - the texture the object will use to draw itself*/
  public GameObject(Vector2 p, Vector2 s, Texture t){
	  this.position = p;
	  this.size = s;
	  this.texture = t;
  }
  
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
  
  /**Returns the Vector2 size of this object*/
  public Vector2 getSize(){
	  return size;
  }
  
  /**Sets the size of this object to s
   * @param s - a Vector2 object*/
  public void setSize(Vector2 s){
	  size = s;
  }
  
  /**Returns the texture of this object*/
  public Texture getTexture(){
	  return texture;
  }
  
  /**Sets the texture of this object to the given texture
   * @param t - the texture we wish to apply
   * */
  public void setTexture(Texture t){
	  texture = t;
  }
  
  public void draw(GameCanvas canvas){
	  canvas.draw(getTexture(), getPosition(), getSize());
  }
}
