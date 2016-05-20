package com.ramenstudio.sandglass.game.view.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.ramenstudio.sandglass.util.view.ui.KeyboardControlButton;

public class HelpView extends Table {
  /** The restart button of the paused screen. */
  public final KeyboardControlButton backButton;
  
  /**
   * Default constructor. Uses the given skin to set up the pause screen UI.
   * @param skin is the style-sheet for the widgets.
   */
  public HelpView(Skin skin) {
    super();
    
    setFillParent(true);
    
    Texture bgTexture = new Texture(Gdx.files.internal("Textures/Help.png"));
    bgTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
    Sprite bgSprite = new Sprite(bgTexture);
    setBackground(new SpriteDrawable(bgSprite));
    
    backButton = new KeyboardControlButton("BACK", skin, "white");
    add(backButton).prefSize(160, 50).expand().bottom().pad(20);
  }
}
