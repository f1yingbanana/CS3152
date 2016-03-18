package com.ramenstudio.sandglass.game.controller;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;

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
}
