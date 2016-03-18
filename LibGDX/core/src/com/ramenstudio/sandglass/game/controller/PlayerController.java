package com.ramenstudio.sandglass.game.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.ramenstudio.sandglass.game.model.Player;
import com.ramenstudio.sandglass.game.view.GameCanvas;

/**
 * Handles player input and manages the player object.
 * 
 * @author Jiacong Xu
 */
public class PlayerController extends AbstractController {
  // The camera controller we are controlling
  private CameraController cameraController;

  // The input controller for player.
  private InputController inputController = new InputController();
  
  // The player we are managing
  private Player player;
  
  // Maximum move speed in horizontal movement
  private float moveSpeed = 2.0f;
  
  /**
   * Default constructor for player object.
   */
  public PlayerController() {
    player = new Player(new Vector2(0, 0));
    cameraController = new CameraController(new Vector2(5, 5));
  }

  @Override
  public void objectSetup(PhysicsDelegate handler) {
    player.body = handler.addBody(player.bodyDef);
    player.body.createFixture(player.fixtureDef);
    player.body.setFixedRotation(true);
    
    cameraController.setTarget(player);
    cameraController.objectSetup(handler);
  }
  
  @Override
  public void update(float dt) {
    cameraController.update(dt);
    inputController.update(dt);
    
    // Realizes player input
    Vector2 p = player.body.getLinearVelocity();
    p.x = moveSpeed * inputController.getHorizontal();

    if (inputController.didPressJump()) {
      p.y = 5.0f;
    }
    
    player.body.setLinearVelocity(p);
    
    
  }
  
  /**
   * @return the main camera for rendering.
   */
  public OrthographicCamera getMainCamera() {
    return cameraController.getCamera();
  }

  @Override
  public void draw(GameCanvas canvas) {
    player.draw(canvas);
  }
}
