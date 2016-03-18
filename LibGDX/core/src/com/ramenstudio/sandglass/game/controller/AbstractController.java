package com.ramenstudio.sandglass.game.controller;

import com.ramenstudio.sandglass.game.view.GameCanvas;

/**
 * An abstract controller that provides some useful methods for controllers.
 * 
 * @author Jiacong Xu
 */
public abstract class AbstractController {
  
  /**
   * The update method for the controller. This should be called every drawing
   * frame.
   * 
   * @param dt is the delta time since last update is called.
   */
  public abstract void update(float dt);
  
  /**
   * Draws the content of this controller on the given canvas.
   * @param canvas provides API for rendering textures and such.
   */
  public abstract void draw(GameCanvas canvas);
  
  /**
   * This is called immediately after the constructor so the controller may
   * insert its objects into the world.
   * 
   * @param handler is a controller implementing necessary methods for object
   * insertion.
   */
  public abstract void objectSetup(PhysicsDelegate handler);
}
