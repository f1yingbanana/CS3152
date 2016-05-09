package com.ramenstudio.sandglass.game.view.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Defines the UI used for paused state of the game.
 * 
 * @author Jiacong Xu
 */
public class PauseView extends Table {
  /** The restart button of the paused screen. */
  public final TextButton restartButton;

  /** The resume button of the paused screen. */
  public final TextButton resumeButton;

  /** The option button of the paused screen. */
  public final TextButton optionsButton;

  /** The go back to main menu button of the paused screen. */
  public final TextButton mainMenuButton;
  
  /**
   * Default constructor. Uses the given skin to set up the pause screen UI.
   * @param skin is the style-sheet for the widgets.
   */
  public PauseView(Skin skin) {
    super();
    
    setFillParent(true);
    setBackground(skin.newDrawable("white", new Color(0, 0, 0, 0.8f)));
    
    restartButton = new TextButton("RESTART", skin);
    add(restartButton).prefSize(160, 50).pad(20);
    restartButton.addListener(new ClickListener() {
      @Override
      public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        restartButton.setText("-    RESTART    -");
      }
      
      @Override
      public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        restartButton.setText("RESTART");
      }
    });
    
    row();
    
    resumeButton = new TextButton("RESUME", skin);
    add(resumeButton).prefSize(160, 50).pad(20);
    resumeButton.addListener(new ClickListener() {
      public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        resumeButton.setText("-     RESUME     -");
      }
      public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        resumeButton.setText("RESUME");
      }
    });
    
    row();
    
    optionsButton = new TextButton("OPTIONS", skin);
    //optionButton.setDisabled(true);
    //optionButton.setTouchable(Touchable.disabled);
    add(optionsButton).prefSize(160, 50).pad(20);
    optionsButton.addListener(new ClickListener() {
      public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        optionsButton.setText("-     OPTIONS     -");
      }
      public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        optionsButton.setText("OPTIONS");
      }
    });
    
    row();
    
    mainMenuButton = new TextButton("SELECT LEVEL", skin);
    //mainMenuButton.setDisabled(true);
    //mainMenuButton.setTouchable(Touchable.disabled);
    add(mainMenuButton).prefSize(160, 50).pad(20);
    mainMenuButton.addListener(new ClickListener() {
      public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        mainMenuButton.setText("-   SELECT LEVEL   -");
      }
      public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        mainMenuButton.setText("SELECT LEVEL");
      }
    });
  }
}
