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

	/** Saving an instance of the delegate */
	private PhysicsDelegate delegate;

	// Variables concerned with turning at corners.
	/** Whether we entered a TurnTile from the left. */
	private boolean enteredLeft;

	/** The active corner we are tracking whether we should turn or not. */
	private GameObject activeCorner;

	/** Whether this player is in the underworld. */
	private boolean isUnder = false;
	
	/** RayCastHandler that detects tiles in this one frame. Should always be set
	 * to null after every update loop. */
	private RayCastHandler oneFrameHandler;

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
		Vector2 p = player.body.getLinearVelocity();
		int underFactor = (isUnder)? -1 : 1;
		p.x = underFactor* moveSpeed * inputController.getHorizontal();

		if (inputController.didPressJump() && isGrounded()) {
			p.y = underFactor * 5.0f;
		}

		player.body.setLinearVelocity(p);

		// Handle rotating
		// TODO
		checkCorner();

		// Handle flipping
		if (inputController.didPressFlip() && canFlip()) {
			SandglassTile under = oneFrameHandler.tileUnderneath;
			if (under.isFlippable())
			cameraController.rotate(180, false);
			// Rotate player box
//			player.setRotation((float) Math.PI);
			// TODO: Rotate player image
			// TODO: Move player based on tile below
			Vector2 pos = player.getPosition();
			float dist = player.getRotation()%Math.PI == 0? 
					under.getHeight() : under.getWidth();
			pos.y -= dist * underFactor;
			player.setPosition(pos);
			Vector2 grav = delegate.getGravity();
			grav.y *= -1;
			delegate.setGravity(grav);
			isUnder ^= true;
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
		Vector2 upper = player.getPosition().add(player.getSize().scl(0.5f).rotate(rot));
		Vector2 lower = player.getPosition().add(player.getSize().scl(-0.5f).rotate(rot));

		delegate.QueryAABB(handler, lower.x, lower.y, upper.x, upper.y);

		// We only set active corner if we WALKED into the corner. We can land on
		// the corner too.
		if (handler.rotate) {
			
		}
		
		
		
		// if (activeCorner == null)

		// activeCorner = handler.corner;

		// Now we check 
	}

	private class OverlapHandler implements QueryCallback {
		GameObject corner;
		boolean rotate;

		@Override
		public boolean reportFixture(Fixture fixture) {
			Object obj = fixture.getUserData();

			if (obj != null && obj.getClass().equals(TurnTile.class)) {
				corner = (GameObject)obj;
//				float 
//				if (corner.getPosition())
				rotate = true;
				return false;
			}
			rotate = false;
			return true;
		}
	}
}
