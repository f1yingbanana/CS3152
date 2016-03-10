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
  
  // A bunch of math constants and stuff. Probably subject to change.
  private float damping = 0.1f;
  private float lookAheadFactor = 20f;
  private float lookAheadReturnSpeed = 0.5f;
  private float lookAheadMoveThreshold = 0.05f;

  private Vector2 m_LastTargetPosition;
  private Vector2 m_LookAheadPos = new Vector2();
  
  private GameCamera camera;
  
  /**
   * Creates a camera controller and initializes the game camera.
   */
  public CameraController() {
    float ratio = (float)Gdx.graphics.getWidth() / Gdx.graphics.getHeight();
    camera = new GameCamera(new Vector2(9 * ratio, 9));
  }
  
  /**
   * Creates a camera controller with the given target
   * @param target is the target we want to follow.
   */
  public CameraController(GameObject targetObject) {
    this();
    setTarget(targetObject);
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
    m_LastTargetPosition = target.getPosition();
  }
  
  @Override
  public void update(float dt) {
    // only update lookahead pos if accelerating or changed direction
    Vector2 dr = target.getPosition().sub(m_LastTargetPosition);
    boolean updateLookAheadTarget = dr.len() > lookAheadMoveThreshold;
    
    if (updateLookAheadTarget) {
      m_LookAheadPos = dr.scl(lookAheadFactor);
    } else {
      float amt = dt * lookAheadReturnSpeed;
      
      if (m_LookAheadPos.len() > amt) {
        m_LookAheadPos.sub(m_LookAheadPos.cpy().scl(amt));
      } else {
        m_LookAheadPos = Vector2.Zero;
      }
    }
    
    Vector2 aheadTargetPos = target.getPosition().add(m_LookAheadPos);
    Vector2 newPos = smoothDamp(camera.getPosition(), aheadTargetPos);
    
    camera.setPosition(newPos);
    m_LastTargetPosition = target.getPosition();
  }
  
  private Vector2 smoothDamp(Vector2 from, Vector2 to) {
    return to.sub(from).scl(damping).add(from);
  }

  @Override
  public void draw(GameCanvas canvas) {}
}
