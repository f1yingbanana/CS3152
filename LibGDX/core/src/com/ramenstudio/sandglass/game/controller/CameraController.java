package com.ramenstudio.sandglass.game.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.ramenstudio.sandglass.game.model.GameCamera;
import com.ramenstudio.sandglass.game.model.GameObject;
import com.ramenstudio.sandglass.game.view.GameCanvas;

/**
 * A camera controller that supports complex behavior of the camera, like
 * smooth follow, panning to important objects temporarily, position weighting,
 * etc.
 * 
 * @author flyingbanana
 */
public class CameraController extends AbstractController {
  // The game object to follow
  private GameObject target;
  
  // The underlying math-doing camera.
  private GameCamera camera;
  
  // The position used to store the position to move the camera to. Due to
  // orthogonal camera only offers translate instead of set position, we can't
  // initialize box2d body with initial position, but set it afterwards.
  private Vector2 initPos;
  
  /**
   * Creates a camera controller and initializes the game camera.
   */
  public CameraController() {
    float ratio = (float)Gdx.graphics.getWidth() / Gdx.graphics.getHeight();
    camera = new GameCamera(new Vector2(9 * ratio, 9));
  }
  
  /**
   * Creates a camera controller with the given starting position.
   * 
   * @param initialPosition is the position where the camera is created.
   */
  public CameraController(Vector2 initialPosition) {
    this();
    initPos = initialPosition.cpy();
  }
  
  /**
   * @return the current matrix used to translate world space position to screen
   */
  public Matrix4 world2ScreenMatrix() {
    return camera.getCamera().combined;
  }
  
  /**
   * Sets the target object for the camera to follow.
   * 
   * @param targetObject is the game object that the camera will smoothly pan to
   */
  public void setTarget(GameObject targetObject) {
    target = targetObject;
  }
  
  @Override
  public void update(float dt) {
    camera.setPosition(target.getPosition());
  }
  
  @Override
  public void draw(GameCanvas canvas) {}

  @Override
  public void objectSetup(PhysicsDelegate handler) {
    // Creates the camera object.
    camera.body = handler.addBody(camera.bodyDef);
    camera.setPosition(initPos);
    initPos = null;
  }
}
