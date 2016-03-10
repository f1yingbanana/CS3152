package com.ramenstudio.sandglass;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Screen;

/**
 * This is the controller for any game level. It is the root for each level.
 * 
 * @author Jiacong Xu
 */
public class GameMode implements Screen {
  // The root model used for the game
  private GameModel model = new GameModel();
  
  // The input controller for player.
  private InputController inputController = new InputController();
  
  // The player controller
  private PlayerController playerController = new PlayerController();
  
  // The list of controllers - used to simplify update loop processing.
  private List<AbstractController> controllers = new ArrayList<AbstractController>();
  
  // The game canvas.
  private GameCanvas canvas;
  
  /**
   * Initializes an instance of the game with all the controllers, model and
   * view canvas.
   */
  public GameMode() {
    controllers.add(inputController);
    controllers.add(playerController);
    
    canvas = new GameCanvas(model.getMainCamera().getCamera());
  }

  @Override
  public void show() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void render(float delta) {
    // Implements an update-draw loop
    for (AbstractController c : controllers) {
      c.update(delta);
    }
    
    // Now we render all objects that we can render
    canvas.clear();
    canvas.begin();
    model.draw(canvas);
    canvas.end();
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
  
}
