package com.ramenstudio.sandglass.game.controller;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.ramenstudio.sandglass.game.model.GameObject;
import com.ramenstudio.sandglass.game.model.Player;
import com.ramenstudio.sandglass.game.model.SandglassTile;
import com.ramenstudio.sandglass.game.model.TurnTile;
import com.ramenstudio.sandglass.game.view.GameCanvas;

/**
 * Handles player input and manages the player object.
 * 
 * @author Jiacong Xu
 */
public class PlayerController extends AbstractController {
	/** The camera controller we are controlling */
	private CameraController cameraController;

	/** The input controller for player. */
	private InputController inputController = new InputController();

	/** The player we are managing */
	private Player player;

	/** This is the offset from the center of the body to the foot. */
	private float footOffset = -0.7f;

	/** This is the distance from where we are raycasting */
	private float rayDist = 0.1f;

	/** Maximum move speed in horizontal movement */
	private float moveSpeed = 3.0f;
	
	/** Vertical jump velocity when jump is begun. */
	private float jumpVelocity = 5.0f;

	/** Saving an instance of the delegate */
	private PhysicsDelegate delegate;

	// Variables concerned with turning at corners.
	/** Whether we entered a TurnTile from the left. */
	private boolean enteredLeft;

	/** The active corner we are tracking whether we should turn or not. */
	private GameObject activeCorner;

	/** Whether this player is in the underworld. */
	private boolean isUnder = false;
	
	/** Whether this player is "sideways". */
	private boolean sideways = false;
	
	/** RayCastHandler that detects tiles in this one frame. Should always be set
	 * to null after every update loop. */
	private RayCastHandler oneFrameHandler;
	
	/** The direction the player's head is facing. */
	private AngleEnum heading = AngleEnum.NORTH;
	
	private enum AngleEnum {
		NORTH,
		EAST,
		SOUTH,
		WEST;
		
		/**
		 * Returns the new compass direction of the
		 * provided direction but rotated 180 degrees/flipped.
		 * 
		 * @param thisEnum is the direction to rotate 180 degrees/flip.
		 * @return the angle rotated 180 degrees/flipped.
		 */
		private static AngleEnum flipEnum(AngleEnum thisEnum) {
			if (thisEnum == NORTH) {
				return SOUTH;
			}
			else if (thisEnum == SOUTH) {
				return NORTH;
			}
			else if (thisEnum == WEST) {
				return EAST;
			}
			else {
				return WEST;
			}
		}
		
		/**
		 * Returns the new compass direction of the
		 * provided direction but rotated 90 degrees counterclockwise.
		 * 
		 * @param thisEnum is the direction to rotate 90 degrees counterclockwise.
		 * @return the angle rotated 90 degrees counterclockwise.
		 */
		private static AngleEnum flipCounterClockWise(AngleEnum thisEnum) {
			if (thisEnum == NORTH) {
				return WEST;
			}
			else if (thisEnum == EAST) {
				return NORTH;
			}
			else if (thisEnum == SOUTH) {
				return EAST;
			}
			else {
				return SOUTH;
			}
		}
		
		/**
		 * Returns the new compass direction of the 
		 * provided direction but rotated 90 degrees clockwise.
		 * 
		 * @param thisEnum is the direction to rotate 90 degrees clockwise.
		 * @return the angle rotated 90 degrees clockwise.
		 */
		private static AngleEnum flipClockWise(AngleEnum thisEnum) {
			if (thisEnum == NORTH) {
				return EAST;
			}
			else if (thisEnum == EAST) {
				return SOUTH;
			}
			else if (thisEnum == SOUTH) {
				return WEST;
			}
			else {
				return NORTH;
			}
		}
		
		/**
		 * Converts the compass direction to an actual angle in radians.
		 * 
		 * @param thisEnum to convert to an angle
		 * @return the angle in radians
		 */
		private static float convertToAngle(AngleEnum thisEnum) {
			if (thisEnum == NORTH) {
				return 0;
			}
			else if (thisEnum == EAST) {
				return (float) (Math.PI/2);
			}
			else if (thisEnum == SOUTH) {
				return (float) (Math.PI);
			}
			else {
				return (float) (3*Math.PI/2);
			}
		}
		
		/**
		 * Whether the given compass direction is vertical.
		 * 
		 * @param 	thisEnum to evaluate
		 * @return	true if thisEnum points North or South, false otherwise 
		 */
		private static boolean isVertical (AngleEnum thisEnum) {
			if (thisEnum == NORTH || thisEnum == SOUTH) {
				return true;
			} else {
				return false;
			}
		}
	}

	/**
	 * Default constructor for player object.
	 */
	public PlayerController() {
		player = new Player(new Vector2(-5, 5));
		cameraController = new CameraController(new Vector2(5, 5));
	}

	@Override
	public void objectSetup(PhysicsDelegate handler) {
		delegate = handler;
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
		Vector2 pos = player.getPosition();
		Vector2 vel = player.body.getLinearVelocity();
		float ang = player.getRotation();
		Vector2 grav = delegate.getGravity();
		int underFactor = (isUnder)? -1 : 1;
		
		// Handle movement
		boolean jump = false;
		float x = underFactor * moveSpeed * inputController.getHorizontal();
		float y = AngleEnum.isVertical(heading) ? vel.y: vel.x;
		if (inputController.didPressJump() && isGrounded()) {
			y = underFactor * jumpVelocity;
			jump = true;
		}
		if (AngleEnum.isVertical(heading)) {
			vel.x = x;
			vel.y = y;
		} else {
			vel.x = y;
			vel.y = x;
		}
		player.body.setLinearVelocity(vel);

		// Handle rotating
		// TODO
		
		checkCorner();
		// TODO: add isUnder condition
		if (activeCorner != null && isGrounded() && !jump) {
			Vector2 cornerPos = activeCorner.getPosition();
			float diff = (AngleEnum.isVertical(heading))?
					pos.x - cornerPos.x : pos.y - cornerPos.y;
			if (diff > 0) {
				cameraController.rotate(-90);
				delegate.setGravity(delegate.getGravity().rotate(90));
				heading = AngleEnum.flipClockWise(heading);
				player.setRotation(AngleEnum.convertToAngle(heading));

			} else {
				cameraController.rotate(90);
				delegate.setGravity(delegate.getGravity().rotate(-90));
				heading = AngleEnum.flipCounterClockWise(heading);
				player.setRotation(AngleEnum.convertToAngle(heading));
			}
			activeCorner = null;
		}
		// Handle flipping
		else if (inputController.didPressFlip() && canFlip() && !jump) {
			SandglassTile under = oneFrameHandler.tileUnderneath;
			
			if (under.isFlippable()) {
				cameraController.rotate(180);
				Vector2 size = player.getSize();
				float flipDist = (AngleEnum.isVertical(heading)) ? 
						under.getHeight() + size.y : under.getWidth() + size.x;
				
				grav.setLength(1.0f);
				grav.scl(flipDist);
				pos.x += grav.x;
				pos.y += grav.y;
				heading = AngleEnum.flipEnum(heading);

				player.setPosition(pos);
				player.setRotation(AngleEnum.convertToAngle(heading));
				delegate.setGravity(delegate.getGravity().scl(-1));
				isUnder ^= true;
			}
		}
		oneFrameHandler = null;
	}

	/**
	 * @return the matrix transformation from world to screen. Used in drawing.
	 */
	public Matrix4 world2ScreenMatrix() {
		return cameraController.world2ScreenMatrix();
	}

	@Override
	public void draw(GameCanvas canvas) {
		player.draw(canvas);
	}

	/**
	 * @return whether player is touching the ground.
	 */
	public boolean isGrounded() {
		Vector2 g = delegate.getGravity().nor();
		Vector2 footPos = player.getPosition().add(g.cpy().scl(-footOffset));
		Vector2 endPos = footPos.cpy().add(g.cpy().scl(rayDist));

		RayCastHandler handler = new RayCastHandler();
		delegate.rayCast(handler, footPos, endPos);

		oneFrameHandler = handler;
		
		return handler.isGrounded;
	}

	/**
	 * Returns true if the player can flip and false if not
	 * 
	 * @return whether player can flip
	 */
	public boolean canFlip() {
		if (oneFrameHandler == null) {
			Vector2 g = delegate.getGravity().nor();
			Vector2 footPos = player.getPosition().add(g.cpy().scl(-footOffset));
			Vector2 endPos = footPos.cpy().add(g.cpy().scl(rayDist));

			RayCastHandler handler = new RayCastHandler();
			delegate.rayCast(handler, footPos, endPos);

			oneFrameHandler = handler;
			
			return handler.isGrounded && handler.isFlip;
		}
		else {
			return oneFrameHandler.isGrounded && oneFrameHandler.isFlip;
		}
	}

	private class RayCastHandler implements RayCastCallback {
		boolean isGrounded = false;
		boolean isFlip = false;
		private SandglassTile tileUnderneath;

		@Override
		public float reportRayFixture(Fixture fixture, Vector2 point, 
				Vector2 normal, float fraction) {
			/**
			 * Later we need to check whether this is actually tagged as ground.
			 * For now, we ignore and return true for any objects!
			 */
			Object obj = fixture.getUserData();

			if (obj != null && obj instanceof SandglassTile) {
				SandglassTile tempGameObject = (SandglassTile)obj;
				isGrounded = tempGameObject.isGround() || isGrounded;
				isFlip = tempGameObject.isFlippable() || isFlip;
				tileUnderneath = tempGameObject;
				return 0;
			}
			return -1;
		}
	}

	/**
	 * If we are not currently at a turning corner, we try to find one. Otherwise
	 * we decide whether we want to flip or not.
	 */
	private void checkCorner() {
		OverlapHandler handler = new OverlapHandler();

		// At start this is 0 degrees
		float rot = delegate.getGravity().angle() - 270;
		Vector2 upper = getUpperRight();
		Vector2 lower = getLowerLeft();
//		System.out.println("This is upper " + upper + " and this is lower " + lower);
//		System.out.println("This is player position " + player.getPosition());
		
		
		delegate.QueryAABB(handler, lower.x, lower.y, upper.x, upper.y);

		// We only set active corner if we WALKED into the corner. We can land on
		// the corner too.
		
		GameObject corner = handler.corner;
		if (corner != null) {
			float coord;
			float pos;
			float vel;
			if (AngleEnum.isVertical(heading)) {
				coord = corner.getPosition().x;
				pos = player.getPosition().x;
				vel = player.body.getLinearVelocity().x;
			} else {
				coord = corner.getPosition().y;
				pos = player.getPosition().y;
				vel = player.body.getLinearVelocity().y;
			}
			if (coord < pos && vel < 0) {
				activeCorner = corner;
			} else if (coord > pos && vel > 0) {
				activeCorner = corner;
			} else {
				activeCorner = null;
			}
		}
	}
	
	/**
	 * This method gets the lower left point of the hit box. In this case,
	 * lower left refers to the absolute lower left, regardless of the player's
	 * current orientation.
	 * 
	 * @return the absolute lower left point of the player's bounding box.
	 */
	private Vector2 getLowerLeft() {
		Vector2 playerPos = player.getPosition();
		Vector2 playerSize = player.getSize().scl(.5f);
		Vector2 theLowerLeft;
		if (heading == AngleEnum.NORTH || heading == AngleEnum.SOUTH) {
			theLowerLeft = new Vector2(playerPos.x - playerSize.x, playerPos.y - playerSize.y);
		}
		else {
			theLowerLeft = new Vector2(playerPos.x - playerSize.y, playerPos.y - playerSize.x);
		}
		return theLowerLeft;
	}
	
	/**
	 * This method gets the upper right point of the hit box. In this case,
	 * upper right refers to the absolute upper right, regarldess of the player's
	 * current orientation.
	 * @return the absolute upper right of the player's bounding box.
	 */
	private Vector2 getUpperRight() {
		Vector2 playerPos = player.getPosition();
		Vector2 playerSize = player.getSize().scl(.5f);
		Vector2 theUpperRight;
		if (heading == AngleEnum.NORTH || heading == AngleEnum.SOUTH) {
			theUpperRight = new Vector2(playerPos.x + playerSize.x, playerPos.y + playerSize.y);
		}
		else {
			theUpperRight = new Vector2(playerPos.x + playerSize.y, playerPos.y + playerSize.x);
		}
		return theUpperRight;
	}

	private class OverlapHandler implements QueryCallback {
		GameObject corner;

		@Override
		public boolean reportFixture(Fixture fixture) {
			Object obj = fixture.getUserData();
			if (obj != null && obj instanceof TurnTile) {
				System.out.println(obj);	
				corner = (TurnTile)obj;
				return false;
			}
			corner = null;
			return true;
		}
	}
}
