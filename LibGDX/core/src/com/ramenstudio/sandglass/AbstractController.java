package com.ramenstudio.sandglass;

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
   * @param dt  the delta time since last update is called.
   */
  public abstract void update(float dt);
}
