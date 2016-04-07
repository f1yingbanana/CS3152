package com.ramenstudio.sandglass.game.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.ramenstudio.sandglass.game.view.GameCanvas;

/**
 * An input controller that handles player input.
 * 
 * @author Sung Won An
 */
public class InputController extends AbstractController {
	// (Do not implement as singleton!)
	
	/** How much did we move horizontally? */
	private float horizontal;
	/** How much did we move vertically? */
	private float vertical;
	/** Did we press the jump button? */
	private boolean pressedJump;
	/** Did we previously press the jump button? */
	private boolean prevJump;
	/** Did we press the flip button? */
	private boolean pressedFlip;
	/** Did we previously press the jump button? */
	private boolean prevFlip;
	/** Did we press the mouse button? */
	private boolean pressedMouse;
	/** Did we press the reset button? DUU DUU DUUUUUU DU */
	private boolean pressedReset;
	/** The mouse position on screen. */
	private Vector2 mousePos;
	
	private static InputController singletonInstance = new InputController();

	private InputController() {
		// TODO: Constructor
		prevJump = prevFlip = false;
	}

	public static InputController getInstance() {
	    return singletonInstance;
	}
	
	/**
	 * Returns the amount of sideways movement. 
	 *
	 * -1 = left, 1 = right, 0 = still
	 *
	 * @return the amount of sideways movement. 
	 */
	public float getHorizontal() {
		return horizontal;
	}

	/**
	 * Returns the amount of vertical movement. 
	 *
	 * -1 = down, 1 = up, 0 = still
	 *
	 * @return the amount of vertical movement. 
	 */
	public float getVertical() {
		return vertical;
	}

	/**
	 * Returns whether the jump button was pressed.
	 *
	 * @return whether the jump button was pressed.
	 */
	public boolean didPressJump() {
		return pressedJump;
	}

	/**
	 * Returns whether the flip button was pressed.
	 *
	 * @return whether the flip button was pressed.
	 */
	public boolean didPressFlip() {
		return pressedFlip;
	}

	/**
	 * Returns whether the mouse button was pressed.
	 *
	 * @return whether the mouse button was pressed.
	 */
	public boolean didPressMouse() {
		return pressedMouse;
	}
	
	/**
	 * Returns whether the reset button was pressed.
	 * 
	 * @return whether the mouse button was pressed.
	 */
	public boolean didPressReset() {
		return pressedReset;
	}

	/**
	 * Returns the last known mouse position on the screen.
	 * Doesn't handle the inconsistency in coordinates and returns the mouse position
	 * with the origin in the upper left corner.
	 *
	 * @return the last known mouse position on screen.
	 */
	public Vector2 getMousePos() {
		return mousePos;
	}
	
	/**
	 * Reads the input for the player and converts the result into game logic.
	 *
	 * Sets the private fields that can be accessed using getters.
	 */
	public void readKeyboard() {
	    int up, left, right, down, jump, flip, reset;
	    up    = Input.Keys.UP; 
	    down  = Input.Keys.DOWN;
	    left  = Input.Keys.LEFT; 
	    right = Input.Keys.RIGHT;
	    jump  = Input.Keys.SPACE;
	    flip  = Input.Keys.F;
	    reset = Input.Keys.L;
			
	    // Convert keyboard state into game commands
	    horizontal = vertical = 0;
	    pressedJump = pressedFlip = false;
	
	    // Movement up/down
		if (Gdx.input.isKeyPressed(up) && !Gdx.input.isKeyPressed(down)) {
			vertical = 1;
		} else if (Gdx.input.isKeyPressed(down) && !Gdx.input.isKeyPressed(up)) {
			vertical = -1;
		}
		
		// Movement left/right
		if (Gdx.input.isKeyPressed(left) && !Gdx.input.isKeyPressed(right)) {
			horizontal = -1;
		} else if (Gdx.input.isKeyPressed(right) && !Gdx.input.isKeyPressed(left)) {
			horizontal = 1;
		}

		// Jumping
		pressedJump = Gdx.input.isKeyPressed(jump) && !prevJump;	// Might be able to use isKeyJustPressed()
		prevJump = Gdx.input.isKeyPressed(jump);
    
		// Flipping
		pressedFlip = Gdx.input.isKeyPressed(flip) && ! prevFlip;	// Might be able to use isKeyJustPressed()
		prevFlip = Gdx.input.isKeyPressed(flip);
		
		pressedReset = Gdx.input.isKeyPressed(reset);
	}

	/**
	 * Reads the input from the mouse.
	 *
	 * Sets the private fields for mouse logic that can be accessed using getters.
	 */
	public void readMouse() {
		int mouseClick = Input.Buttons.LEFT;
		pressedMouse = Gdx.input.isButtonPressed(mouseClick);
		int x = Gdx.input.getX();
		int y = Gdx.input.getY();
		mousePos = new Vector2(x,y);
	}

	/**
	 * Read input and process.
	 *
	 * Must be called before PlayerController update method.
	 */
	@Override
	public void update(float dt) {
		readKeyboard();
		readMouse();
	}

	@Override
	public void draw(GameCanvas canvas) {
		// TODO Auto-generated method stub
	}

  @Override
  public void objectSetup(PhysicsDelegate handler) {}

}
