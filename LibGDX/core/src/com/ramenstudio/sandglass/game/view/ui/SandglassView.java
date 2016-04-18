package com.ramenstudio.sandglass.game.view.ui;

import javax.swing.GroupLayout.Alignment;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

/**
 * Renders the sandglass UI element with two parameters specifying the fullness
 * of each part.
 * 
 * @author Jiacong Xu
 */
public class SandglassView extends Group {
  private Image backgroundImage = new Image(new Texture(Gdx.files.internal("UI/Sandglass/SandglassBG.png")));
  private Image foregroundImage = new Image(new Texture(Gdx.files.internal("UI/Sandglass/SandglassFG.png")));
  
  private Image topSandImage;
  private Image botSandImage;
  
  /**
   * The speed of this rotation.
   */
  public float rotationSpeed = 5.0f;
  
  private boolean isFlipped = false;
  
  /**
   * Initializes the sandglass view with 0 sand fill in both components.
   */
  public SandglassView() {
    setTransform(false);
    
    Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
    pixmap.setColor(new Color(1, 216/255f, 92/255f, 1));
    pixmap.fill();
    Texture sandTexture = new Texture(pixmap);
    topSandImage = new Image(sandTexture);
    botSandImage = new Image(sandTexture);
    topSandImage.setOrigin(29, 0);
    botSandImage.setOrigin(29, 31);
    topSandImage.setBounds((128 - 58) / 2f, 64, 58, 31);
    botSandImage.setBounds((128 - 58) / 2f, 64 - 31, 58, 31);
    
    backgroundImage.setOrigin(Align.center);
    foregroundImage.setOrigin(Align.center);
    
    addActor(backgroundImage);
    addActor(topSandImage);
    addActor(botSandImage);
    addActor(foregroundImage);
    
  }

  /**
   * Sets the sand in the hourglass.
   * 
   * @param top is the fill amount of the top part of the hourglass, [0,1].
   * @param bottom is the fill amount of the bottom part.
   */
  public void setSandAmount(float top, float bottom) {
    float sandHeight = 31;
    if (isFlipped) {
      //botSandImage.setHeight(sandHeight * bottom);
      //botSandImage.setY(64 - botSandImage.getHeight());
      //topSandImage.setHeight(sandHeight * top);
      //topSandImage.setY(64 + sandHeight - topSandImage.getHeight());

      botSandImage.setHeight(sandHeight * top);
      topSandImage.setHeight(sandHeight * bottom);
    } else {
      topSandImage.setHeight(sandHeight * top);
      //topSandImage.setY(64);
      botSandImage.setHeight(sandHeight * bottom);
      //botSandImage.setY(64 - sandHeight);
      //topSandImage.setRotation(0);
      //botSandImage.setRotation(0);
    }
  }
  
  /**
   * Sets the goal orientation of the sandglass. It can either be flipped or
   * not flipped. Starts out not flipped.
   */
  public void rotateSandglass(boolean isFlipped) {
    if (this.isFlipped != isFlipped) {
      this.isFlipped = isFlipped;
    }
  }
  
  /**
   * Should be called to update orientation of the sandglass. If sandglass is
   * recently flipped, this will play an animation frame by frame.
   * @param dt
   */
  public void updateRotation(float dt) {
    float goal = isFlipped? 180 : 0;
    
    // Lerp to goal
    float curr = foregroundImage.getRotation();
    float lerp = Math.min(goal, (goal - curr) * rotationSpeed * dt);
    foregroundImage.setRotation(curr + lerp);
    //topSandImage.setRotation(curr + lerp);
    //botSandImage.setRotation(curr + lerp);
  }
}
