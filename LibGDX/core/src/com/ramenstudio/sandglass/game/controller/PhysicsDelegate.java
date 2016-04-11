package com.ramenstudio.sandglass.game.controller;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.RayCastCallback;

/**
 * The delegate that satisfies all the required physics-related operations in
 * the game, such as adding an object in the simulated world.
 * 
 * @author Jiacong Xu
 */
public interface PhysicsDelegate {
  /**
   * Creates a physics body and inserts it immediately into the world.
   * 
   * @param definition is the creating body's properties.
   * @return the created body's reference.
   */
  public Body addBody(BodyDef definition);
  
  /**
   * @return a copy of the current gravity.
   */
  public Vector2 getGravity();
  
  /**
   * @param the vector of the new gravity direction.
   */
  public void setGravity(Vector2 gravity);
  
  /**
   * See rayCast method in World class.
   */
  public void rayCast(RayCastCallback callback, Vector2 point1, Vector2 point2);
  
  /**
   * See QueryAABB method in World class.
   */
  public void QueryAABB(QueryCallback callback, float lowerX, float lowerY,
    float upperX, float upperY);

}