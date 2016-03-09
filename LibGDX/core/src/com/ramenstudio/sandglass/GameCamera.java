package com.ramenstudio.sandglass;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

/**
 * The game rener camera object that is maintained inside a game object.
 * 
 * @author Jiacong Xu
 */
public class GameCamera extends GameObject {
  private OrthographicCamera camera = new OrthographicCamera();
  
  public OrthographicCamera getCamera() {
    return camera;
  }
  
  @Override
  public void setPosition(Vector2 position) {
    // Also moves the camera
    Vector2 oldPos = getPosition();
    super.setPosition(position);
    camera.translate(position.sub(oldPos));
  }
  
  @Override
  public void setRotation(float rotation) {
    // Also rotates the camera
    float oldRot = getRotation();
    super.setRotation(rotation);
    camera.rotate(rotation - oldRot);
  }
}
