package com.ramenstudio.sandglass.game.view.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.ramenstudio.sandglass.util.view.ui.KeyboardControlButton;

/**
 * Defines the UI used for paused state of the game.
 * 
 * @author Jiacong Xu
 */
public class PauseView extends Table {
  /** The restart button of the paused screen. */
  public final KeyboardControlButton restartButton;

  /** The resume button of the paused screen. */
  public final KeyboardControlButton resumeButton;

  /** The option button of the paused screen. */
  public final KeyboardControlButton creditsButton;

  /** The go back to main menu button of the paused screen. */
  public final KeyboardControlButton mainMenuButton;
  
  /**
   * Default constructor. Uses the given skin to set up the pause screen UI.
   * @param skin is the style-sheet for the widgets.
   */
  public PauseView(Skin skin) {
    super();
    
    setFillParent(true);
    setBackground(skin.newDrawable("white", new Color(0, 0, 0, 0.8f)));

    resumeButton = new KeyboardControlButton("RESUME", skin, "white");
    add(resumeButton).prefSize(160, 50).pad(20);
    
    row();
    
    restartButton = new KeyboardControlButton("RESTART", skin, "white");
    add(restartButton).prefSize(160, 50).pad(20);
    
    row();
    
    creditsButton = new KeyboardControlButton("HELP", skin, "white");
    add(creditsButton).prefSize(160, 50).pad(20);
    
    row();
    
    mainMenuButton = new KeyboardControlButton("MENU", skin, "white");
    add(mainMenuButton).prefSize(160, 50).pad(20);

    resumeButton.setArrowUp(mainMenuButton);
    restartButton.setArrowUp(resumeButton);
    creditsButton.setArrowUp(restartButton);
    mainMenuButton.setArrowUp(creditsButton);
    
    resumeButton.setArrowDown(restartButton);
    restartButton.setArrowDown(creditsButton);
    creditsButton.setArrowDown(mainMenuButton);
    mainMenuButton.setArrowDown(resumeButton);
  }
}
