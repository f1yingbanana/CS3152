package com.ramenstudio.sandglass.game.controller;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.ramenstudio.sandglass.game.model.GameObject;
import com.ramenstudio.sandglass.game.model.NormalTile;
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
    	cameraController.rotate(180, false);
    	// TODO: Rotate player
    }
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
    
    return handler.isGrounded;
  }
  
  /**
   * Returns true if the player can flip and false if not
   * 
   * @return whether player can flip
   */
  public boolean canFlip() {
	  // TODO: Check if a player can flip if isGrounded and tile standing on also is flippable.
	    Vector2 g = delegate.getGravity().nor();
	    Vector2 footPos = player.getPosition().add(g.cpy().scl(-footOffset));
	    Vector2 endPos = footPos.cpy().add(g.cpy().scl(rayDist));
	    
	    RayCastHandler handler = new RayCastHandler();
	    delegate.rayCast(handler, footPos, endPos);
	    
	    return handler.isGrounded && handler.isFlip;
  }
  
  private class RayCastHandler implements RayCastCallback {
    boolean isGrounded = false;
    boolean isFlip = false;
    
    @Override
    public float reportRayFixture(Fixture fixture, Vector2 point, 
        Vector2 normal, float fraction) {
      /**
       * Later we need to check whether this is actually tagged as ground.
       * For now, we ignore and return true for any objects!
       */
      Object obj = fixture.getUserData();
      
      if (obj != null && obj instanceof SandglassTile) {
    	  System.out.println("Got here");
        SandglassTile tempGameObject = (SandglassTile)obj;
        isGrounded = tempGameObject.isGround() || isGrounded;
        isFlip = tempGameObject.isFlippable() || isFlip;
        return 0;
      }
      return 0;
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
    // if (activeCorner == null)
    
    // activeCorner = handler.corner;
    
    // Now we check 
  }
  
  private class OverlapHandler implements QueryCallback {
    GameObject corner;
    
    @Override
    public boolean reportFixture(Fixture fixture) {
      Object obj = fixture.getUserData();
      
      if (obj != null && obj.getClass().equals(TurnTile.class)) {
        corner = (GameObject)obj;
        return false;
      }
      
      return true;
    }
  }
}
