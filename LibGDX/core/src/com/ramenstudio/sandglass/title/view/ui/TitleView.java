package com.ramenstudio.sandglass.title.view.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
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
  public final ImageButton optionsButton;
  
  public TitleView(Skin skin) {
    super();
    
    setFillParent(true);
    
    // Create image
    Sprite bgSprite = new Sprite(new Texture(Gdx.files.internal("Textures/bgmenu.png")));
    setBackground(new SpriteDrawable(bgSprite));
    
    // Create a play button
    gameStartButton = new TextButton("Start", skin);
    add(gameStartButton).expand().bottom().padBottom(100).padLeft(350);
    
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
    optionsButton = new ImageButton(skin, "OptionButton");
    add(optionsButton).bottom().right().pad(20);
    
  }
}
