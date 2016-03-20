package com.ramenstudio.sandglass;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Texture;

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
  private float rotation;
  
  // The texture of this object
  private Texture texture;
  
  //The size of this object
  private Vector2 size;
  
  /**Initializer*/
  public GameObject(){
	  position = new Vector2();
	  rotation = 0;
	  texture = null;
	  size = new Vector2();
  }
  
  public GameObject(Vector2 pos, float rot, Texture tex, Vector2 s){
	  position = pos;
	  rotation = rot;
	  texture = tex;
	  size = s;
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
  
  /**@return the vector2 size of this object*/
  public Vector2 getSize(){
	  return size;
  }
  
  /**Sets the size of this object
   * @param s the Vector2 size (width, height)*/
  public void setSize(Vector2 s){
	  size = s;
  }
  
  /**@return the Texture associated with this object*/
  public Texture getTexture(){
	  return texture;
  }
  
  /**Sets the texture associated with this object
   * @param t a Texture object*/
  public void setTexture(Texture t){
	  texture = t;
  }
  
  @Override
  public void draw(GameCanvas canvas) {
    canvas.draw(texture, position, size);
  }
}
