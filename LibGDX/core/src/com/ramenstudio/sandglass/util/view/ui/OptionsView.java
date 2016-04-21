package com.ramenstudio.sandglass.util.view.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

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
    setBackground(skin.newDrawable("white", new Color(0, 0, 0, 1)));
    
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
