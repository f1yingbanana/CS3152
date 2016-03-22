package com.ramenstudio.sandglass.game.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.ramenstudio.sandglass.game.view.GameCanvas;
import com.ramenstudio.sandglass.util.Drawable;

/**
 * The most generic object in a game. It represents an object (whether rendered
 * or not) in the game. It has a box2d body field which keeps its physical
 * presence in box2d world. It is up to the controller to add this object into
 * the world, however.
 * 
 * @author Jiacong Xu
 */
public class GameObject implements Drawable{
  /** 
   * The physical object of this game object.
   */
  public Body body;
  
  /**
   * The definition for this body for this object. Every game object has a
   * position at least.
   */
  public BodyDef bodyDef = new BodyDef();
  
  /** 
   * Stores the fixture information for this object. Make sure userData for
   * fixture points back to the game object it belongs to! 
   */
  public FixtureDef fixtureDef;
  
  /**
   * Stores the custom mass information for this object. Leave null for default.
   */
  public MassData massdata;
  
  // Stores the size of this object for purposes of drawing
  private Vector2 size = new Vector2();
  
  // Stores the texture associated with this object, can be null if game camera
  private Texture texture = null;
  
  private TextureRegion textureRegion;
  
  /**
   * Default initializer.
   */
  public GameObject() {}
  
  /**
   * Creates a GameObject with given size and texture.
   * 
   * @param s - the size of the object
   * @param t - the texture the object will use to draw itself
   * */
  public GameObject(Vector2 s, Texture t){
	  this.texture = t;
	  this.size = s;
	  textureRegion = new TextureRegion(t);
  }
  
  /**
   * @return a copy of this object's world position.
   */
  public Vector2 getPosition() {
    return body.getPosition().cpy();
  }
  
  /**
   * Sets the position of this game object to the given position.
   * 
   * @param position  the position we want to set to.
   */
  public void setPosition(Vector2 position) {
    body.setTransform(position, body.getAngle());
  }

  /**
   * @return the rotation of this object in radians.
   */
  public float getRotation() {
    return body.getAngle();
  }
  
  /**
   * Sets the rotation of this game object to the given rotation.
   * 
   * @param rotation is the rotation we want to set to in radians.
   */
  public void setRotation(float rotation) {
    body.setTransform(body.getPosition(), rotation);
  }
  
  /**@return the size of this object as a Vector2*/
  public Vector2 getSize(){
	  return size.cpy();
  }
  
  /**Sets the size of this object (for purposes of drawing)
   * @param s the new vector2 size*/
  public void setSize(Vector2 s){
	  size = size.set(s);
  }
  
  /**@return the texture of this game object*/
  public Texture getTexture(){
	  return texture;
  }
  
  /**Sets the texture of this object
   * @param t the Texture to set*/
  public void setTexture(Texture t) {
	  texture = t;
	  textureRegion = new TextureRegion(t);
  }
  
  public TextureRegion getTextureRegion() {
	  return textureRegion;
  }
  
  public void setTextureRegion(TextureRegion tr) {
	  textureRegion = tr;
  }
  
  @Override
  public void draw(GameCanvas canvas){
	  canvas.draw(texture, body.getPosition(), size);
  }
}
