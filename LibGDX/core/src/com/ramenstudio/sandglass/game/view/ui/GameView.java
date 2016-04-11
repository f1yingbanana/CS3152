package com.ramenstudio.sandglass.game.view.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * Defines the UI used for playing state of the game.
 * 
 * @author Jiacong Xu
 */
public class GameView extends Table {
  /** The pause button for the game. */
  public final TextButton pauseButton;
  
  /**
   * Default constructor. Uses the given skin to set up the pause screen UI.
   * @param skin is the style-sheet for the widgets.
   */
  public GameView(Skin skin) {
    super();
    
    this.setFillParent(true);
    
    pauseButton = new TextButton("PAUSE", skin);
    add(pauseButton).prefSize(120, 50).expand().top().right().pad(20);
  }
  
  /**
   * Sets the sand in the hourglass element in the UI.
   * 
   * @param top is the fill amount of the top part of the hourglass, [0,1].
   * @param bottom is the fill amount of the bottom part.
   */
  public void setSandAmount(float top, float bottom) {
    
  }
}
