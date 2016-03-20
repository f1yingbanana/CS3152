package com.ramenstudio.sandglass.game.controller;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
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
   * See rayCast function in World class.
   */
  public void rayCast(RayCastCallback callback, Vector2 point1, Vector2 point2);
}
