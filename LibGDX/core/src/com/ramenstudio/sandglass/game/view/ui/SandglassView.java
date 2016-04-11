package com.ramenstudio.sandglass.game.view.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * Renders the sandglass UI element with two parameters specifying the fullness
 * of each part.
 * 
 * @author Jiacong Xu
 */
public class SandglassView extends Group {
  private Image backgroundImage = new Image(new Texture(Gdx.files.internal("UI/Sandglass/SandglassBG.png")));
  private Image foregroundImage = new Image(new Texture(Gdx.files.internal("UI/Sandglass/SandglassFG.png")));
  
  private Image topSandImage = new Image(new Texture(Gdx.files.internal("UI/Sandglass/SandglassSandTop.png")));
  private Image botSandImage = new Image(new Texture(Gdx.files.internal("UI/Sandglass/SandglassSandBot.png")));

  private Table topTable = new Table();
  private Table botTable = new Table();
  
  /**
   * The speed of this rotation.
   */
  public float rotationSpeed = 1.0f;
  
  private boolean isFlipped = false;
  
  /**
   * Initializes the sandglass view with 0 sand fill in both components.
   */
  public SandglassView() {
    topTable.setBounds(0, 0, 128, 128);
    botTable.setBounds(0, 0, 128, 128);
    topTable.setClip(true);
    botTable.setClip(true);
    
    topTable.addActor(topSandImage);
    botTable.addActor(botSandImage);
    
    addActor(backgroundImage);
    addActor(topTable);
    addActor(botTable);
    addActor(foregroundImage);

    backgroundImage.setPosition(-64, -64);
    foregroundImage.setPosition(-64, -64);
    topTable.setPosition(-64, -64);
    botTable.setPosition(-64, -64);
  }

  /**
   * Sets the sand in the hourglass.
   * 
   * @param top is the fill amount of the top part of the hourglass, [0,1].
   * @param bottom is the fill amount of the bottom part.
   */
  public void setSandAmount(float top, float bottom) {
    float yOffset = 32;
    float sandHeight = 28;
    
    topTable.setSize(topTable.getWidth(), topSandImage.getPrefHeight() - yOffset - (1 - top) * sandHeight);
    botTable.setSize(botTable.getWidth(), yOffset + sandHeight * bottom);
  }
  
  /**
   * Sets the goal orientation of the sandglass. It can either be flipped or
   * not flipped. Starts out not flipped.
   */
  public void rotateSandglass(boolean isFlipped) {
    this.isFlipped = isFlipped;
  }
  
  /**
   * Should be called to update orientation of the sandglass. If sandglass is
   * recently flipped, this will play an animation frame by frame.
   * @param dt
   */
  public void updateRotation(float dt) {
    float goal = isFlipped? 180 : 0;
    
    // Lerp to goal
    float curr = getRotation();
    float lerp = Math.min(goal, (goal - curr) * rotationSpeed * dt);
    
    setRotation(curr + lerp);
  }
}
