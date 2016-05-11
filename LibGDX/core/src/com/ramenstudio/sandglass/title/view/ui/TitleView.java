package com.ramenstudio.sandglass.title.view.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

/**
 * The main menu view.
 * 
 * @author Jiacong Xu
 */
public class TitleView extends Table {
  public final TextButton gameStartButton;
  public final TextButton levelSelectButton;
  public final TextButton optionsButton;
  public final TextButton quitButton;
  
  public TitleView(Skin skin) {
    super();
    
    setFillParent(true);
    
    // Create image
    Texture bgTexture = new Texture(Gdx.files.internal("Textures/bgmenu.png"));
    bgTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
    Sprite bgSprite = new Sprite(bgTexture);
    setBackground(new SpriteDrawable(bgSprite));
    
    // Create a play button
    gameStartButton = new TextButton("START", skin, "New Font");
    add(gameStartButton).padTop(220).minHeight(60);
    
    // Create an image button
    row();
    levelSelectButton = new TextButton("LEVELS", skin, "New Font");
    add(levelSelectButton).padTop(15).minHeight(60);
    
    // Create an image button
    row();
    optionsButton = new TextButton("OPTIONS", skin, "New Font");
    add(optionsButton).padTop(15).minHeight(60);
    
    row();
    quitButton = new TextButton("QUIT", skin, "New Font");
    add(quitButton).padTop(15).minHeight(60);
  }
}
