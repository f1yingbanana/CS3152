package com.ramenstudio.sandglass.game.controller;

import java.util.List;
import java.util.Map;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.ramenstudio.sandglass.game.model.GameModel;
import com.ramenstudio.sandglass.game.model.GameState;
import com.ramenstudio.sandglass.game.util.LevelLoader;
import com.ramenstudio.sandglass.game.view.GameCanvas;
import com.ramenstudio.sandglass.game.model.GameObject;

/**
 * This takes care of the game initialization, maintains update and drawing
 * order, and keeps the physics for the game.
 * 
 * @author Jiacong Xu
 */
public class GameController extends AbstractController {

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
  private PlayerController playerController;

  /** The Camera Controller for this game */
  private CameraController cameraController;

  // The UI controller for the game - special case. Do not draw UI inside game
  // controller.
  public UIController uiController = new UIController();
  
  public LevelLoader loader = new LevelLoader();
  
  private Map<LevelLoader.LayerKey, List<GameObject>> mapObjects;
  
  public GameController() {
    playerController = new PlayerController();
    cameraController = new CameraController(new Vector2(5, 5));
    
    mapObjects = loader.loadLevel("example.tmx");
    
    // Set up the world!
    objectSetup(world);
    
    // Set up UI callbacks
    uiController.gameView.pauseButton.addListener(pauseButtonCallback);
    uiController.pauseView.resumeButton.addListener(resumeButtonCallback);
    uiController.pauseView.restartButton.addListener(restartButtonCallback);
    uiController.pauseView.mainMenuButton.addListener(mainMenuButtonCallback);
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
    List<GameObject> mapTiles = mapObjects.get(LevelLoader.LayerKey.GROUND);
    
    for (GameObject o : mapTiles) {
      activatePhysics(world, o);
    }
    
    playerController.objectSetup(world);
    cameraController.setTarget(playerController.getPlayer());
    cameraController.objectSetup(world);
  }
  
  @Override
  public void update(float dt) {
    cameraController.update(dt);
    // Order matters. Must call update BEFORE rotate on cameraController.
    if (playerController.getRotateAngle() != 0f) {
        cameraController.rotate(playerController.getRotateAngle());
    }
    
    playerController.update(dt);
    uiController.update(dt);
    
    stepPhysics(dt);
    
    if (playerController.isReset()) {
      reset();
    }
  }
  
  private void reset() {
    world.dispose();
    world = new World(new Vector2(0, -9.8f), true);
    playerController = new PlayerController();
    gameModel = new GameModel();
    objectSetup(world);
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
    cameraController.draw(canvas);
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
}
