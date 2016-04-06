package com.ramenstudio.sandglass.game.controller;

import java.util.List;
import java.util.Map;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.ramenstudio.sandglass.game.model.GameModel;
import com.ramenstudio.sandglass.game.util.LevelLoader;
import com.ramenstudio.sandglass.game.view.GameCanvas;
import com.ramenstudio.sandglass.game.model.GameObject;
import com.ramenstudio.sandglass.game.model.TurnTile;
import com.ramenstudio.sandglass.game.model.WallTile;

/**
 * This takes care of the game initialization, maintains update and drawing
 * order, and keeps the physics for the game.
 * 
 * @author Jiacong Xu
 */
public class GameController extends AbstractController implements PhysicsDelegate {

  // The physics world object
  public World world = new World(new Vector2(0, -9.8f), true);

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
  public PlayerController playerController;
  
  public LevelLoader loader = new LevelLoader();
  
  private Map<LevelLoader.LayerKey, List<GameObject>> mapObjects;
  
  public GameController() {
    playerController = new PlayerController();
    
    mapObjects = loader.loadLevel("example.tmx");
    // Set up the world!
    objectSetup(this);
  }
  
  @Override
  public void objectSetup(PhysicsDelegate handler) {
    // TESTING AREA. CREATE SOME OBJECTS FOR FUN!
    List<GameObject> mapTiles = mapObjects.get(LevelLoader.LayerKey.GROUND);
    
    for (GameObject o : mapTiles) {
      activatePhysics(handler, o);
//      if (o instanceof TurnTile) {
//    	  TurnTile temp = (TurnTile) o;
//    	  System.out.println(temp.getPosition());
//      }
    }
    
    playerController.objectSetup(handler);
  }
  
  @Override
  public void update(float dt) {
    playerController.update(dt);
    stepPhysics(dt);
    if (playerController.isReset()) {
    	reset();
    }
    
    // Call updates
  }
  
  private void reset() {
	  	world.dispose();
		world = new World(new Vector2(0, -9.8f), true);
		playerController = new PlayerController();
		objectSetup(this);
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
    gameModel.draw(canvas);
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

  @Override
  public Vector2 getGravity() {
    return world.getGravity().cpy();
  }
  
  /**
   * @return a copy of the current gravity.
   */
  @Override
  public void setGravity(Vector2 gravity) {
	  world.setGravity(gravity);
  }

  @Override
  public void rayCast(RayCastCallback callback, Vector2 point1, 
    Vector2 point2) {
    world.rayCast(callback, point1, point2);
  }

  @Override
  public void QueryAABB(QueryCallback callback, float lowerX, float lowerY,
    float upperX, float upperY) {
    world.QueryAABB(callback, lowerX, lowerY, upperX, upperY);
  }

  /**
   * @return the main camera used by the game
   */
  public OrthographicCamera getMainCamera() {
    return playerController.getMainCamera();
  }

}
