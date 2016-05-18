package com.ramenstudio.sandglass.util.view.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

/**
 * Defines the UI used for the option screen.
 * 
 * @author Jiacong Xu
 */
public class OptionsView extends Table {
  /** The restart button of the paused screen. */
  public final TextButton backButton;
  
  /**
   * Default constructor. Uses the given skin to set up the pause screen UI.
   * @param skin is the style-sheet for the widgets.
   */
  public OptionsView(Skin skin) {
    super();
    
    setFillParent(true);
    
    Texture bgTexture = new Texture(Gdx.files.internal("Textures/Controls.png"));
    bgTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
    Sprite bgSprite = new Sprite(bgTexture);
    setBackground(new SpriteDrawable(bgSprite));
    
    backButton = new TextButton("BACK", skin);
    add(backButton).prefSize(160, 50).expand().bottom().pad(20);
    backButton.addListener(new ClickListener() {
      @Override
      public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        backButton.setText("-    BACK    -");
      }
      
      @Override
      public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        backButton.setText("BACK");
      }
    });
  }
}
