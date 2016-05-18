package com.ramenstudio.sandglass.title.view.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.ramenstudio.sandglass.util.view.ui.KeyboardControlButton;

/**
 * The main menu view.
 * 
 * @author Jiacong Xu
 */
public class TitleView extends Table {
  /*
  public final TextButton gameStartButton;
  public final TextButton levelSelectButton;
  public final TextButton optionsButton;
  public final TextButton quitButton;
  */

  public final KeyboardControlButton gameStartButton;
  public final KeyboardControlButton levelSelectButton;
  public final KeyboardControlButton creditsButton;
  public final KeyboardControlButton quitButton;
  
  public TitleView(Skin skin) {
    super();
    
    setFillParent(true);
    
    // Create image
    Texture bgTexture = new Texture(Gdx.files.internal("Textures/bgmenu.png"));
    bgTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
    Sprite bgSprite = new Sprite(bgTexture);
    setBackground(new SpriteDrawable(bgSprite));
    
    
    // Create a play button
    
    float h = 60f;
    float w = 160f;
    
    //gameStartButton = new TextButton("START", skin, "New Font");
    gameStartButton = new KeyboardControlButton("START", skin, "purple");
    add(gameStartButton).padTop(220).minSize(w, h);
    
    // Create an image button
    row();
    //levelSelectButton = new TextButton("LEVELS", skin, "New Font");
    levelSelectButton = new KeyboardControlButton("LEVELS", skin, "purple");
    add(levelSelectButton).padTop(15).minSize(w, h);
    
    // Create an image button
    row();
    //optionsButton = new TextButton("CREDITS", skin, "New Font");
    creditsButton = new KeyboardControlButton("CREDITS", skin, "purple");
    add(creditsButton).padTop(15).minSize(w, h);
    
    row();
    //quitButton = new TextButton("QUIT", skin, "New Font");
    quitButton = new KeyboardControlButton("QUIT", skin, "purple");
    add(quitButton).padTop(15).minSize(w, h);

    gameStartButton.setArrowDown(levelSelectButton);
    gameStartButton.setArrowUp(quitButton);

    levelSelectButton.setArrowDown(creditsButton);
    levelSelectButton.setArrowUp(gameStartButton);

    creditsButton.setArrowDown(quitButton);
    creditsButton.setArrowUp(levelSelectButton);
    
    quitButton.setArrowDown(gameStartButton);
    quitButton.setArrowUp(creditsButton);
  }
}
