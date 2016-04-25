package com.ramenstudio.sandglass.title.view.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

/**
 * The main menu view.
 * 
 * @author Jiacong Xu
 */
public class TitleView extends Table {
  public final TextButton gameStartButton;
  public final TextButton optionsButton;
  public final TextButton creditsButton;
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
    gameStartButton = new TextButton("Start", skin);
    add(gameStartButton).padTop(250).padLeft(420);
    
    gameStartButton.addListener(new ClickListener() {
      @Override
      public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        gameStartButton.setText("-    START    -");
      }
      
      @Override
      public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        gameStartButton.setText("START");
      }
    });
    
    // Create an image button
    row();
    optionsButton = new TextButton("Options", skin);
    add(optionsButton).padTop(50).padLeft(420);
    
    optionsButton.addListener(new ClickListener() {
      @Override
      public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        optionsButton.setText("-   OPTIONS   -");
      }
      
      @Override
      public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        optionsButton.setText("OPTIONS");
      }
    }); 

    row();
    creditsButton = new TextButton("CREDITS", skin);
    add(creditsButton).padTop(50).padLeft(420);
    
    creditsButton.addListener(new ClickListener() {
      @Override
      public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        creditsButton.setText("-   CREDITS   -");
      }
      
      @Override
      public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        creditsButton.setText("CREDITS");
      }
    });
    
    row();
    quitButton = new TextButton("QUIT", skin);
    add(quitButton).padTop(50).padLeft(420);
    
    quitButton.addListener(new ClickListener() {
      @Override
      public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        quitButton.setText("-     QUIT     -");
      }
      
      @Override
      public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        quitButton.setText("QUIT");
      }
    });
  }
}
