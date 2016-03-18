package com.ramenstudio.sandglass.game.controller;

import com.badlogic.gdx.math.Matrix4;
import com.ramenstudio.sandglass.game.model.GameModel;
import com.ramenstudio.sandglass.game.view.GameCanvas;

/**
 * This takes care of the game initialization, maintains update and drawing
 * order, and keeps the physics for the game.
 * 
 * @author Jiacong Xu
 */
public class GameController extends AbstractController {

  // The place to store all top-level game related data.
  private GameModel model = new GameModel();
  
  // The player controller for the game
  private PlayerController playerController = new PlayerController();
  
  
  @Override
  public void update(float dt) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void draw(GameCanvas canvas) {
    // TODO Auto-generated method stub
    
  }
  
  /**
   * @return the world to screen transformation matrix kept by the camera.
   */
  public Matrix4 world2ScreenMatrix() {
    return playerController.world2ScreenMatrix();
  }

}
