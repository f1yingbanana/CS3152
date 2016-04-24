package com.ramenstudio.sandglass.game.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.ramenstudio.sandglass.game.controller.UIController.UIState;
import com.ramenstudio.sandglass.game.model.GameModel;
import com.ramenstudio.sandglass.game.model.GameState;
import com.ramenstudio.sandglass.game.model.Gate;
import com.ramenstudio.sandglass.game.model.Player;
import com.ramenstudio.sandglass.game.model.ShipPiece;
import com.ramenstudio.sandglass.game.util.LevelLoader;
import com.ramenstudio.sandglass.game.util.LevelLoader.LayerKey;
import com.ramenstudio.sandglass.game.view.GameCanvas;
import com.ramenstudio.sandglass.game.model.GameObject;
import com.ramenstudio.sandglass.game.model.Monster;
import com.ramenstudio.sandglass.game.model.Player;
import com.ramenstudio.sandglass.game.model.TurnTile;
import com.ramenstudio.sandglass.game.model.WallTile;

/**
 * This takes care of the game initialization, maintains update and drawing
 * order, and keeps the physics for the game.
 * 
 * @author Jiacong Xu
 */
public class GameController extends AbstractController implements ContactListener {
  
  /** If this flag is true, we need a new game controller. */
  public boolean needsReset = false;

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
  
  //Is the player collided with the gate?
  private boolean touchingGate = false;
  
  // The player controller for the game
  private PlayerController playerController;

  /** The Camera Controller for this game */
  private CameraController cameraController;

  // The UI controller for the game - special case. Do not draw UI inside game
  // controller.
  public UIController uiController = new UIController();
  
  private Array<MonsterController> monsterController = new Array<MonsterController>();
  
  public LevelLoader loader = new LevelLoader();
  
  private Map<LevelLoader.LayerKey, Array<GameObject>> mapObjects;
  
  
  public GameController() {
	mapObjects = loader.loadLevel("newLevel.tmx");
	Player player = (Player) mapObjects.get(LayerKey.PLAYER).get(0);
    playerController = new PlayerController(player);
    cameraController = new CameraController(new Vector2(5, 5));
    
    Array<GameObject> mArray = (Array<GameObject>) 
            mapObjects.get(LevelLoader.LayerKey.MONSTER);
    Gate gate = (Gate) ((Array<GameObject>) 
    		mapObjects.get(LevelLoader.LayerKey.GATE)).get(0);
    Array<GameObject> ship = (Array<GameObject>) 
    		mapObjects.get(LevelLoader.LayerKey.RESOURCE);
    List<ShipPiece> shipList = gameModel.getShipPieces();
    for (GameObject s: ship){
    	shipList.add((ShipPiece) s);
    }
    
    gameModel.setNumberOfPieces(ship.size);
    gameModel.setGate(gate);
      
    for (GameObject m: mArray){
        monsterController.add(new MonsterController((Monster) m));
    }
    
    // Set up the world!
    objectSetup(world);
    
    // Set up UI callbacks
    uiController.gameView.pauseButton.addListener(pauseButtonCallback);
    uiController.pauseView.resumeButton.addListener(resumeButtonCallback);
    uiController.pauseView.restartButton.addListener(restartButtonCallback);
    uiController.pauseView.mainMenuButton.addListener(mainMenuButtonCallback);
    uiController.levelCompleteView.restartButton.addListener(restartButtonCallback);
    uiController.levelCompleteView.mainMenuButton.addListener(mainMenuButtonCallback);
    uiController.levelFailedView.restartButton.addListener(restartButtonCallback);
    uiController.levelFailedView.mainMenuButton.addListener(mainMenuButtonCallback);
  }
  
  /**
   * UI Callbacks
   */
  
  /**
   * Called when pause button from the main playing screen is clicked.
   */
  private ClickListener pauseButtonCallback = new ClickListener() {
    @Override
    public void clicked(InputEvent event, float x, float y) {
      uiController.setGameState(UIController.UIState.PAUSED);
      gameModel.setGameState(GameState.PAUSED);
    }
  };
  
  /**
   * Called when resume button from the paused screen is clicked.
   */
  private ClickListener resumeButtonCallback = new ClickListener() {
    @Override
    public void clicked(InputEvent event, float x, float y) {
      uiController.setGameState(UIController.UIState.PLAYING);
      gameModel.setGameState(GameState.PLAYING);
    }
  };
  
  /**
   * Called when restart button from the paused screen is clicked.
   */
  private ClickListener restartButtonCallback = new ClickListener() {
    @Override
    public void clicked(InputEvent event, float x, float y) {
      reset();
      uiController.setGameState(UIController.UIState.PLAYING);
    }
  };
  
  /**
   * Called when main menu button from the paused screen is clicked.
   */
  private ClickListener mainMenuButtonCallback = new ClickListener() {
    public void clicked(InputEvent event, float x, float y) {
      // TODO go back to main menu.
    }
  };
  
  @Override
  public void objectSetup(World world) {
    Array<GameObject> mapTiles = mapObjects.get(LevelLoader.LayerKey.GROUND);
    
    for (GameObject o : mapTiles) {
      activatePhysics(world, o);
    }
    
    playerController.objectSetup(world);
    cameraController.setTarget(playerController.getPlayer());
    cameraController.objectSetup(world);
    
    for (MonsterController m: monsterController){
    	m.objectSetup(world);
    }

    for (ShipPiece c : gameModel.getShipPieces()) {
    	activatePhysics(world, c);
    }
    
    activatePhysics(world, gameModel.getGate());
    
    world.setContactListener(this);
  }
  
  @Override
  public void update(float dt) {
    uiController.update(dt);
    
    switch (gameModel.getGameState()) {
    case LOST:
      uiController.setGameState(UIState.LOST);
      return;
    case PAUSED:
      return;
    case PLAYING:
      break;
    case WON:
      uiController.setGameState(UIState.WON);
      return;
    }
    
    cameraController.update(dt);
    // Order matters. Must call update BEFORE rotate on cameraController.
    if (playerController.getRotateAngle() != 0f) {
        cameraController.rotate(playerController.getRotateAngle());
    }
    
    playerController.update(dt);
//    System.out.println(playerController.getPlayer().getPosition().toString);
    
  //update game model
    gameModel.setWorldPosition(!playerController.isUnder());
    gameModel.incrementTime();
    
    //if(gameModel.allPiecesCollected()){
    //	gameModel.getGate().setOpen();
    //}
    
    for (MonsterController m: monsterController){
        m.update(dt);
    }
    
    
    stepPhysics(dt);
    
    if (/**gameModel.getGate().isOpen() && touchingGate ||*/
    		gameModel.outOfTime() ||
    		playerController.isReset()){
    	reset();
    }
  }
  
  private void reset() {
    needsReset = true;
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
    // Draw a background image
    playerController.draw(canvas);
    cameraController.draw(canvas);
    
    for (MonsterController m: monsterController){
        m.draw(canvas);
    }
    
    gameModel.draw(canvas);
  }
  
  /**
   * @return the world to screen transformation matrix kept by the camera.
   */
  public Matrix4 world2ScreenMatrix() {
    return cameraController.world2ScreenMatrix();
  }

  /**
   * @return the main camera used by the game
   */
  public OrthographicCamera getViewCamera() {
    return cameraController.getViewCamera();
  }

  public PlayerController getPlayerController() {
    return playerController;
  }

  public CameraController getCameraController() {
    return cameraController;
  }

@Override
public void beginContact(Contact contact) {
    if(contact.getFixtureA().getBody().getUserData() == Player.class  &&
            contact.getFixtureB().getBody().getUserData() == Gate.class){
          touchingGate = true;
    }
    
    GameObject firstOne = (GameObject) contact.getFixtureA().getBody().getUserData();
    GameObject secondOne = (GameObject) contact.getFixtureB().getBody().getUserData();
    
    if ((firstOne instanceof Player && secondOne instanceof Monster) ||
        (secondOne instanceof Player && firstOne instanceof Monster)) {
      gameModel.setGameState(GameState.LOST);
    }
    
    if (firstOne instanceof Player &&
    		secondOne instanceof ShipPiece) {
    	ShipPiece secondShipPiece = (ShipPiece) secondOne;
    	if (!secondShipPiece.getIsCollected()) {
    		secondShipPiece.setCollected();
    		gameModel.collectPiece();
    	}
    } else if (firstOne instanceof ShipPiece &&
    		secondOne instanceof Player) {
    	ShipPiece firstShipPiece = (ShipPiece) firstOne;
    	if (!firstShipPiece.getIsCollected()) {
    		firstShipPiece.setCollected();
    		gameModel.collectPiece();
    	}
    }
    
    if (gameModel.allPiecesCollected()) {
    	gameModel.getGate().setAllPiecesCollected(true);
    }
    
    if ((firstOne instanceof Player && secondOne instanceof Gate) ||
        (firstOne instanceof Gate && secondOne instanceof Player)) {
      if (gameModel.allPiecesCollected()) {
        // We won!
        gameModel.setGameState(GameState.WON);
    	}
    }
    
}



@Override
public void endContact(Contact contact) {
    // TODO Auto-generated method stub
    if (contact.getFixtureB().getUserData().getClass()==Player.class &&
            contact.getFixtureA().getUserData().getClass()==Monster.class){
    }
}

@Override
public void preSolve(Contact contact, Manifold oldManifold) {
    // TODO Auto-generated method stub
    
}

@Override
public void postSolve(Contact contact, ContactImpulse impulse) {
	    
}

}
