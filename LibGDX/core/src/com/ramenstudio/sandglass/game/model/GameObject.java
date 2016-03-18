package com.ramenstudio.sandglass.game.model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;

/**
 * The most generic object in a game. It represents an object (whether rendered
 * or not) in the game. It has a box2d body field which keeps its physical
 * presence in box2d world. It is up to the controller to add this object into
 * the world, however.
 * 
 * @author Jiacong Xu
 */
public class GameObject {
  // The physical object of this game object.
  public Body body;
  
  // The definition for this body for this object. Every game object has a
  // position at least.
  public BodyDef bodyDef = new BodyDef();
  
  // Stores the fixture information for this object.
  public FixtureDef fixtureDef;
  
  // Stores the custom mass information for this object. Leave null for default.
  public MassData massdata;
  
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
}
