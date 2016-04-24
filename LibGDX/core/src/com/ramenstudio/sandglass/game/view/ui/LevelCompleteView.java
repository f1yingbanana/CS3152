package com.ramenstudio.sandglass.game.view.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Displays a completed level screen, showing stats, buttons for restart, next
 * and back to menu.
 */
public class LevelCompleteView extends Table {
  public final TextButton restartButton;
  
  public final TextButton nextLevelButton;
  
  public final TextButton mainMenuButton;
  
  // private final Label scoreLabel;
  
  private final Label titleLabel;
  

  /**
   * Default constructor. Uses the given skin to set up the pause screen UI.
   * @param skin is the style-sheet for the widgets.
   */
  public LevelCompleteView(Skin skin, boolean didWin) {
    super();

    setFillParent(true);
    setBackground(skin.newDrawable("white", new Color(0, 0, 0, 0.8f)));
    
    titleLabel = new Label("Level Complete", skin);
    add(titleLabel).padBottom(80);
    
    if (!didWin) {
      titleLabel.setText("Level Failed");
    }

    nextLevelButton = new TextButton("NEXT LEVEL", skin);
    
    if (didWin) {
      row();
      
      add(nextLevelButton).prefSize(160, 50).pad(20);
      nextLevelButton.addListener(new ClickListener() {
        @Override
        public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
          nextLevelButton.setText("-   NEXT LEVEL   -");
        }
        
        @Override
        public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
          nextLevelButton.setText("NEXT LEVEL");
        }
      });
    }
    
    row();
    
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
    
    mainMenuButton = new TextButton("MAIN MENU", skin);
    //mainMenuButton.setDisabled(true);
    //mainMenuButton.setTouchable(Touchable.disabled);
    add(mainMenuButton).prefSize(160, 50).pad(20);
    mainMenuButton.addListener(new ClickListener() {
      public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        mainMenuButton.setText("-   MAIN MENU   -");
      }
      public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        mainMenuButton.setText("MAIN MENU");
      }
    });
  }
}
