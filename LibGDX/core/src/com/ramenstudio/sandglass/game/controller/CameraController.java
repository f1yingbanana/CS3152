package com.ramenstudio.sandglass.game.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.ramenstudio.sandglass.game.model.GameCamera;
import com.ramenstudio.sandglass.game.model.GameObject;
import com.ramenstudio.sandglass.game.view.GameCanvas;

/**
 * A viewCamera controller that supports complex behavior of the viewCamera, like
 * smooth follow, panning to important objects temporarily, position weighting,
 * etc.
 * 
 * @author flyingbanana
 */
public class CameraController extends AbstractController {

	// The game object to follow
	private GameObject target;

	// The underlying math-doing viewCamera.
	private GameCamera viewCamera;


	// The position used to store the position to move the viewCamera to. Due to
	// orthogonal viewCamera only offers translate instead of set position, we can't
	// initialize box2d body with initial position, but set it afterwards.
	private Vector2 initPos;

	// Rotation stuff
	
	/** The current rotation angle goal. */
	private float goal;

	/** Whether to use smoothing. */
	private boolean instant;

	/** The smoothing factors between 0 and 1. 
	 * 	0 means no movement
	 * 	1 means instant movement.
	 */
	private static final float TRANSLATING_FACTOR = 0.05f;
	private static final float ROTATING_FACTOR = 0.05f;

	/** The standard for viewCamera speed. */
	private static final float FRAME_TIME = 1f/60f;

	// For debugging purposes
	private int count = 0;
	
	/** Used to detect if the view is sideways or not */
	private boolean okeydokey = false;
	
	// Zoom stuff
	
	/** The original size of the Camera */
	private Vector2 originalSize;
	
	/** The current zoom scale factor of the camera */
	private float zoomScale = 1f;
	
	/** Used to provide drag to the camera as it is zooming in/out */
	private final float slowFactor = .05f;
	
	/** The maximum zoom of the camera */
	private final float maxZoom = 1.5f;
	
	/** Used to store whether the camera should be currently zooming in or zooming out */
	private boolean zoomingOut = false;
	

	/**
	 * Creates a viewCamera controller and initializes the game viewCamera.
	 */
	public CameraController() {
		float ratio = (float)Gdx.graphics.getWidth() / Gdx.graphics.getHeight();
		originalSize = new Vector2(9 * ratio, 9);
		viewCamera = new GameCamera(originalSize);
	}

	/**
	 * Creates a viewCamera controller with the given starting position.
	 * 
	 * @param initialPosition is the position where the viewCamera is created.
	 */
	public CameraController(Vector2 initialPosition) {
		this();
		initPos = initialPosition.cpy();
	}

	/**
	 * @return the current matrix used to translate world space position to screen
	 */
	public Matrix4 world2ScreenMatrix() {
		return viewCamera.getCamera().combined;
	}

	/**
	 * Sets the target object for the viewCamera to follow.
	 * 
	 * @param targetObject is the game object that the viewCamera will smoothly pan to
	 */
	public void setTarget(GameObject targetObject) {
		target = targetObject;
	}

	/**
	 * Rotates the viewCamera view given the amount, with an option to animate.
	 * 
	 * @param angle is the amount to rotate in degrees.
	 * @param isInstant is the flag for whether rotation should be instant.
	 */
	public void rotate(float angle, boolean isInstant) {
		goal = goal + angle;
		instant = isInstant;
	}

	/**
	 * Rotates the viewCamera view given the amount with animation.
	 * 
	 * @param angle is the amount to rotate in degrees.
	 */
	public void rotate(float angle) {
		goal = goal + angle;
		instant = false;
		if (Math.abs(Math.abs(angle) - 90) < 1f) {
			OrthographicCamera realCamera = viewCamera.getCamera();
			float viewportWidth = realCamera.viewportWidth;
			float viewportHeight = realCamera.viewportHeight;
			realCamera.viewportHeight = viewportWidth;
			realCamera.viewportWidth = viewportHeight;
			okeydokey = !okeydokey;
		}
	}

	/**
	 * Helper method that's also used in GameController to
	 * swap the width and height of the camera. This has to be done 
	 * every frame because, when the camera is rotated, the render
	 * and actual view of the screen differ.
	 */
	public void swapCameraDimensions() {
		if (okeydokey) {
			OrthographicCamera realCamera = viewCamera.getCamera();
			float viewportWidth = realCamera.viewportWidth;
			float viewportHeight = realCamera.viewportHeight;
			realCamera.viewportHeight = viewportWidth;
			realCamera.viewportWidth = viewportHeight;
		}
	}
	
	public void doZoomInOrOut() {
		if (zoomingOut) {
			zoomScale += slowFactor*(maxZoom - zoomScale)/maxZoom;
		}
		else {
			zoomScale -= slowFactor*(zoomScale - 1f)/1f;
		}
		// Apply the zoomScale
		viewCamera.getCamera().zoom = zoomScale;
	}

	@Override
	public void update(float dt) {
		float translateTime = (dt/FRAME_TIME) * TRANSLATING_FACTOR;
		float rotateTime = (dt/FRAME_TIME) * ROTATING_FACTOR;

		float camAngle = viewCamera.getRotation();
		if (goal != camAngle) {
			if (instant) {
				viewCamera.setRotation(goal);
			} else {
				float newAngle = (goal - camAngle)*rotateTime + camAngle;
				viewCamera.setRotation(newAngle);
			}
		}
		Vector2 targPos = target.getPosition();
		Vector2 camPos = viewCamera.getPosition();
		if (targPos != camPos) {
			float x = (targPos.x - camPos.x)*translateTime + camPos.x;
			float y = (targPos.y - camPos.y)*translateTime + camPos.y;
			Vector2 deltaPos = new Vector2(x,y);
			viewCamera.setPosition(deltaPos);
		}

		swapCameraDimensions();

		// TESTING
		count++;
		if (Gdx.input.isKeyPressed(Input.Keys.R) && count > 10) {
			count = 0;
			rotate(90,false);
		}
		
		if (InputController.getInstance().didJustPressedZoom()) {
			zoomingOut = !zoomingOut;
		}
		
		doZoomInOrOut();
	}

	@Override
	public void draw(GameCanvas canvas) {}

	@Override
	public void objectSetup(World world) {
		// Creates the viewCamera object.
		viewCamera.setBody(world.createBody(viewCamera.getBodyDef()));
		viewCamera.setPosition(initPos);
		goal = 0;
	}

	/**
	 * @return the viewCamera managed by this controller
	 */
	public OrthographicCamera getViewCamera() {
		return viewCamera.getCamera();
	}
}
