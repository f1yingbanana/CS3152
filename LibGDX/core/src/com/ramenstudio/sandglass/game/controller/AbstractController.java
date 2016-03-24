package com.ramenstudio.sandglass.game.controller;

import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.ramenstudio.sandglass.game.model.GameObject;
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
  
  /**
   * Uses the physics delegate to insert the game object into the world, and
   * create all fixtures.
   * 
   * @param handler is the physics delegate that maintains the World object.
   * @param gameObject is the object to insert into the world.
   */
  public void activatePhysics(PhysicsDelegate handler, GameObject gameObject) {
    gameObject.setBody(handler.addBody(gameObject.getBodyDef()));
    gameObject.getBody().setUserData(gameObject);
    
    if (gameObject.getFixtureDefs() != null) {
      for (FixtureDef def : gameObject.getFixtureDefs()) {
        gameObject.getBody().createFixture(def);
      }
    }
  }
  
  /**
   * Uses the physics delegate to remove the game object from the world, and
   * remove all fixtures.
   * 
   * @param handler is the physics delegate that maintains the World object.
   * @param gameObject is the object to remove from the world.
   */
  public void deactivatePhysics(PhysicsDelegate handler, GameObject gameObject) {
    // TODO
  }
}
