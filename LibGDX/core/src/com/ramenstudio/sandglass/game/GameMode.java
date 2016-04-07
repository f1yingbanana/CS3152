package com.ramenstudio.sandglass.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.ramenstudio.sandglass.game.controller.GameController;
import com.ramenstudio.sandglass.game.view.GameCanvas;
import com.ramenstudio.sandglass.util.AbstractMode;

/**
 * This is the controller for any game level. It is the root for each level. It
 * implements an update-draw loop and handles application-based events.
 * 
 * @author Jiacong Xu
 */
public class GameMode extends AbstractMode implements Screen {
  // The game play controller that handles the basic logic of the game
  private GameController gameplayController = new GameController();
  
  // The game canvas.
  private GameCanvas canvas = new GameCanvas();

  // BG renderer
  private ShapeRenderer shapeRenderer = new ShapeRenderer();
  
  // A debug renderer
  Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
  
  // Toggle debug
  private boolean debug = false;
  // Debug timer
  private int count = 0;
  
  TiledMapRenderer tiledMapRenderer = new OrthogonalTiledMapRenderer(gameplayController.loader.tiledMap, 1/450f);
  
  /**
   * Initializes an instance of the game with all the controllers, model and
   * view canvas.
   */
  public GameMode() {
  }

  @Override
  public void show() {
  }

  @Override
  public void render(float delta) {
    // Implements an update-draw loop
    gameplayController.update(delta);

    
    // Now we render all objects that we can render
    canvas.clear();
    
    // Render background.
    shapeRenderer.begin(ShapeType.Filled);
    Color botColor = new Color(242/255f, 250/255f, 172/255f, 1);
    Color topColor = new Color(242/255f, 144/255f, 132/255f, 1);
    
    shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), topColor, topColor, botColor, botColor);
    shapeRenderer.end();

    // MAP RENDER
//    OrthographicCamera tempCamera = gameplayController.getMainCamera();
    tiledMapRenderer.setView(gameplayController.getMainCamera());
    
    if (gameplayController.playerController.cameraController.okeydokey) {
        float viewportWidth = gameplayController.getMainCamera().viewportWidth;
        float viewportHeight = gameplayController.getMainCamera().viewportHeight;
        gameplayController.getMainCamera().viewportHeight = viewportWidth;
        gameplayController.getMainCamera().viewportWidth = viewportHeight;
    }
    
    tiledMapRenderer.render();
    
    // Draw Map
    canvas.begin(gameplayController.world2ScreenMatrix());
    gameplayController.draw(canvas);
    canvas.end();

    // DEBUG RENDERS. We can have more render passes later implemented here.
    
    if (Gdx.input.isKeyPressed(Input.Keys.D))
      count++;
      if (count > 5) {
    	  debug ^= true;
    	  count = 0;
      }
      if (debug) {
          debugRenderer.render(gameplayController.world, gameplayController.world2ScreenMatrix());
      }
    
  }

  @Override
  public void resize(int width, int height) {
    // We should not need to resize, ever.
  }

  @Override
  public void pause() {
    // Called when the game is paused (loss of focus)
  }

  @Override
  public void resume() {
    // Called when the game regains focus.
  }

  @Override
  public void hide() {
    // When the game disposes this mode.
  }

  @Override
  public void dispose() {
    // When we should release all resources for this screen.
  }

  @Override
  public String[] getResourcePaths() {
    return null;
  }
  
}
