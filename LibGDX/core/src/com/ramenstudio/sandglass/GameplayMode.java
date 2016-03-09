package com.ramenstudio.sandglass;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Screen;

/**
 * This is the controller for any game level. It is the root for each level.
 * 
 * @author Jiacong Xu
 */
public class GameplayMode implements Screen {
  // The root model used for the game
  private GameplayModel model;
  
  // The input controller for player.
  private InputController inputController;
  
  // The player controller
  private PlayerController playerController;
  
  private List<AbstractController> controllers = new ArrayList<AbstractController>();
  
  /**
   * Initializes an instance of the game with all the controllers, model and
   * view canvas.
   */
  public GameplayMode() {
    inputController = new InputController();
    playerController = new PlayerController();
    
    controllers.add(inputController);
    controllers.add(playerController);
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
