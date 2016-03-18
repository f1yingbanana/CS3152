package com.ramenstudio.sandglass.game.controller;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.ramenstudio.sandglass.game.model.GameModel;
import com.ramenstudio.sandglass.game.view.GameCanvas;

/**
 * This takes care of the game initialization, maintains update and drawing
 * order, and keeps the physics for the game.
 * 
 * @author Jiacong Xu
 */
public class GameController extends AbstractController implements PhysicsDelegate {

  // The physics world object
  public World world = new World(new Vector2(0, -1.5f), true);

  // The amount of time for a physics engine step.
  private static final float WORLD_STEP = 1/60.0f;
  
  // The maximum allowed time for each step in the simulation.
  private static final float WORLD_MAX_STEP = 1/4.0f;
  
  // Number of velocity iterations for the constrain solvers
  private static final int WORLD_VELOC = 6;
  
  // Number of position iterations for the constrain solvers
  private static final int WORLD_POSIT = 2;
  
  // An accumulator for last processed physics step time.
  private float accumulator;
  
  // The place to store all top-level game related data.
  private GameModel gameModel = new GameModel();
  
  // The player controller for the game
  private PlayerController playerController;
  
  public GameController() {
    playerController = new PlayerController();
    
    
    // Set up the world!
    objectSetup(this);
  }
  
  @Override
  public void objectSetup(PhysicsDelegate handler) {
    // TESTING AREA. CREATE SOME OBJECTS FOR FUN!
    BodyDef boxDef = new BodyDef();
    boxDef.position.set(new Vector2(-3, -3));
    Body box = world.createBody(boxDef);
    PolygonShape boxShape = new PolygonShape();
    boxShape.setAsBox(1, 1);
    box.createFixture(boxShape, 0);
    
    
    playerController.objectSetup(handler);
  }
  
  @Override
  public void update(float dt) {
    playerController.update(dt);
    stepPhysics(dt);
  }
  
  /**
   * Simulates the world by dt. If dt is too long, we break the update into
   * multiple steps.
   */
  private void stepPhysics(float dt) {
    float frameTime = Math.min(dt, WORLD_MAX_STEP);
    accumulator += frameTime;
    while (accumulator >= WORLD_STEP) {
        world.step(WORLD_STEP, WORLD_VELOC, WORLD_POSIT);
        accumulator -= WORLD_STEP;
    }
  }

  @Override
  public void draw(GameCanvas canvas) {
    playerController.draw(canvas);
  }
  
  /**
   * @return the world to screen transformation matrix kept by the camera.
   */
  public Matrix4 world2ScreenMatrix() {
    return playerController.world2ScreenMatrix();
  }
  
  
  @Override
  public Body addBody(BodyDef definition) {
    return world.createBody(definition);
  }

}
