package com.ramenstudio.sandglass.game.controller;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
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
  public void update(float dt) {}
  
  /**
   * Draws the content of this controller on the given canvas.
   * @param canvas provides API for rendering textures and such.
   */
  public void draw(GameCanvas canvas) {}
  
  /**
   * This is called immediately after the constructor so the controller may
   * insert its objects into the world.
   * 
   * @param world is a reference to the physical world object. Keep a reference
   * if the controller needs it later.
   */
  public void objectSetup(World handler) {}
  
  /**
   * Uses the physics delegate to insert the game object into the world, and
   * create all fixtures.
   * 
   * @param world is the box2D object.
   * @param gameObject is the object to insert into the world.
   */
  public void activatePhysics(World world, GameObject gameObject) {
    gameObject.setBody(world.createBody(gameObject.getBodyDef()));
    gameObject.getBody().setUserData(gameObject);
    
    if (gameObject.getFixtureDefs() != null) {
      for (FixtureDef def : gameObject.getFixtureDefs()) {
        Fixture tempFixture = gameObject.getBody().createFixture(def);
        tempFixture.setUserData(gameObject);
      }
    }
  }
  
  /**
   * Uses the physics delegate to remove the game object from the world, and
   * remove all fixtures.
   * 
   * @param world is the box2D object.
   * @param gameObject is the object to remove from the world.
   */
  public void deactivatePhysics(World world, GameObject gameObject) {
    // TODO
  }
  
  public abstract void dispose();
}
