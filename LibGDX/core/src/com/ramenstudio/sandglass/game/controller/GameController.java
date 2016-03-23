package com.ramenstudio.sandglass.game.controller;

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
import com.badlogic.gdx.utils.Array;
import com.ramenstudio.sandglass.game.model.GameModel;
import com.ramenstudio.sandglass.game.util.LevelLoader;
import com.ramenstudio.sandglass.game.view.GameCanvas;
<<<<<<< HEAD
=======
import com.ramenstudio.sandglass.game.model.GameObject;
import com.ramenstudio.sandglass.game.model.NormalTile;
>>>>>>> origin/westaalger
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
  
  private static final String BACKGROUND_FILE = "assets/background.png";
  
  private Texture background;
  
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
  
  public LevelLoader loader = new LevelLoader();
  
  public GameController() {
    playerController = new PlayerController();
<<<<<<< HEAD
    background = new Texture(BACKGROUND_FILE);
    
=======
    
    loader.loadLevel("example.tmx");
>>>>>>> jiacong/new/tilemap
    // Set up the world!
    objectSetup(this);
  }
  
  @Override
  public void objectSetup(PhysicsDelegate handler) {
    // TESTING AREA. CREATE SOME OBJECTS FOR FUN!

    //create box 1
//    BodyDef box1Def = new BodyDef();
//    box1Def.position.set(new Vector2(-3, -3));
//    Body box1 = world.createBody(box1Def);
//    PolygonShape boxShape1 = new PolygonShape();
//    boxShape1.setAsBox(8, 4);
//    box1.createFixture(boxShape1, 0);

	// This does everything above except in the constructor
    new NormalTile(handler, world, -3, -3, 8, 4, true);
    
    //create box 2
//    BodyDef box2Def = new BodyDef();
//    box2Def.position.set(new Vector2(1, 5));
//    Body box2 = world.createBody(box2Def);
//    PolygonShape boxShape2 = new PolygonShape();
//    boxShape2.setAsBox(4, 4);
//    box2.createFixture(boxShape2, 0);
    
    // This does everything above except in the constructor	
    new NormalTile(handler, world, 1, 5, 4, 4, true);
    
    //set up turn tiles at corners
    TurnTile tt1 = new TurnTile();
    tt1.getBodyDef().position.set(new Vector2(-11.5f, -7.5f));
    activatePhysics(handler, tt1);
    
    TurnTile tt2 = new TurnTile();
    tt2.getBodyDef().position.set(new Vector2(-11.5f, 1.5f));
    activatePhysics(handler, tt2);
    
    WallTile wt = new WallTile(WallTile.WallType.TOPLEFT);
    wt.getBodyDef().position.set(new Vector2(-14, 0));
    activatePhysics(handler, wt);
    
    wt = new WallTile(WallTile.WallType.BOTLEFT);
    wt.getBodyDef().position.set(new Vector2(-14, -2));
    activatePhysics(handler, wt);
    wt = new WallTile(WallTile.WallType.BOTRIGHT);
    wt.getBodyDef().position.set(new Vector2(-12, -2));
    activatePhysics(handler, wt);
    wt = new WallTile(WallTile.WallType.TOPRIGHT);
    wt.getBodyDef().position.set(new Vector2(-12, 0));
    activatePhysics(handler, wt);
    wt = new WallTile(WallTile.WallType.TOP);
    wt.getBodyDef().position.set(new Vector2(-13, 0));
    activatePhysics(handler, wt);
    wt = new WallTile(WallTile.WallType.BOT);
    wt.getBodyDef().position.set(new Vector2(-13, -2));
    activatePhysics(handler, wt);
    wt = new WallTile(WallTile.WallType.LEFT);
    wt.getBodyDef().position.set(new Vector2(-14, -1));
    activatePhysics(handler, wt);
    wt = new WallTile(WallTile.WallType.RIGHT);
    wt.getBodyDef().position.set(new Vector2(-12, -1));
    activatePhysics(handler, wt);
    
    
    //GameObject boxobj = new GameObject();
    //boxobj.setTexture(new Texture(Gdx.files.internal("paper.png")));
    //boxobj.setSize(new Vector2(8,8));
    //GameObject[] a = {boxobj};
    
    //gameModel.setGameObjects(a);
    
<<<<<<< HEAD
    
    

	  for (int i = 0; i < 20; i++){
		  BodyDef boxDef = new BodyDef();
		    boxDef.position.set(new Vector2(i, -3));
		    Body box = world.createBody(boxDef);
		    PolygonShape boxShape = new PolygonShape();
		    boxShape.setAsBox(1, 1);
		    box.createFixture(boxShape, 0);
	  }

=======
>>>>>>> jiacong/new/tilemap
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
	canvas.draw(background, new Vector2(0,0), new Vector2(10,10));
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
