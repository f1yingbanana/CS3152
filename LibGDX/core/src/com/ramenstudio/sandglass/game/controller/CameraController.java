package com.ramenstudio.sandglass.game.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
  
  /** The current rotation angle goal. */
  private float goal;
  
  /** Whether to use smoothing. */
  private boolean instant;
  
  /** The smoothing factors between 0 and 1. 
   * 	0 means no movement
   * 	1 means instant movement.
   */
  private static final float TRANSLATING_FACTOR = 0.05f;
  private static final float ROTATING_FACTOR = 0.07f;
  
  /** The standard for camera speed. */
  private static final float FRAME_TIME = 1f/60f;
  
  // For debugging purposes
  private int count = 0;
  
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
  
//  /**
//   * Sets the smoothing factor for the camera.
//   * 
//   * @param factor	the smoothing factor
//   * 				Must be between 0 and 1 inclusive
//   */
//  public void setSmoothing(float f) {
//    factor = f;
//  }
	  
  /**
   * Rotates the camera view given the amount, with an option to animate.
   * 
   * @param angle is the amount to rotate in degrees.
   * @param isInstant is the flag for whether rotation should be instant.
   */
  public void rotate(float angle, boolean isInstant) {
    goal = goal + angle;
    instant = isInstant;
  }
  
  /**
   * Rotates the camera view given the amount with animation.
   * 
   * @param angle is the amount to rotate in degrees.
   */
  public void rotate(float angle) {
	    goal = goal + angle;
	    instant = false;
	  }
  
  @Override
  public void update(float dt) {
	float translateTime = (dt/FRAME_TIME) * TRANSLATING_FACTOR;
	float rotateTime = (dt/FRAME_TIME) * ROTATING_FACTOR;

	float camAngle = camera.getRotation();
	if (goal != camAngle) {
		if (instant) {
		    camera.setRotation(goal);
		} else {
			float newAngle = (goal - camAngle)*rotateTime + camAngle;
		    camera.setRotation(newAngle);
		}
	}
	Vector2 targPos = target.getPosition();
    Vector2 camPos = camera.getPosition();
	if (targPos != camPos) {
		float x = (targPos.x - camPos.x)*translateTime + camPos.x;
	    float y = (targPos.y - camPos.y)*translateTime + camPos.y;
	    Vector2 deltaPos = new Vector2(x,y);
	    camera.setPosition(deltaPos);
	}
	
	// TESTING
    count++;
	if (Gdx.input.isKeyPressed(Input.Keys.R) && count > 10) {
		count = 0;
		rotate(90,false);
	}
  }
  
  @Override
  public void draw(GameCanvas canvas) {}

  @Override
  public void objectSetup(PhysicsDelegate handler) {
    // Creates the camera object.
    camera.setBody(handler.addBody(camera.getBodyDef()));
    camera.setPosition(initPos);
    initPos = null;
  }

  /**
   * @return the camera managed by this controller
   */
  public OrthographicCamera getCamera() {
    return camera.getCamera();
  }
}
