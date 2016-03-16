package com.ramenstudio.sandglass.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.ramenstudio.sandglass.GameCanvas;
import com.ramenstudio.sandglass.Player;

/**
 * Handles player input and manages the player object.
 * 
 * @author Jiacong Xu
 */
public class PlayerController extends AbstractController {
  // The camera controller we are controlling
  private CameraController cameraController;
  
  // The player we are managing
  private Player player = new Player();
  
  /**
   * Default constructor for player object.
   */
  public PlayerController() {
    cameraController = new CameraController(player);
  }
  
  @Override
  public void update(float dt) {
    cameraController.update(dt);
    
    // Realizes player input
    // PLACEHOLDER!
    int h = Gdx.input.isKeyPressed(Input.Keys.A)? -1 : 0 + (Gdx.input.isKeyPressed(Input.Keys.D)? 1:0);
    int v = Gdx.input.isKeyPressed(Input.Keys.S)? -1 : 0 + (Gdx.input.isKeyPressed(Input.Keys.W)? 1:0);
    
    Vector2 p = player.getPosition();
    p.x += h * dt;
    p.y += v * dt;
    player.setPosition(p);
  }
  
  /**
   * @return the matrix transformation from world to screen. Used in drawing.
   */
  public Matrix4 getCameraTransform() {
    return cameraController.getCamera().getCamera().combined;
  }

  @Override
  public void draw(GameCanvas canvas) {
    player.draw(canvas);
  }
}
