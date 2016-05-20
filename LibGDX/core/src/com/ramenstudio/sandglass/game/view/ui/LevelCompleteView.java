package com.ramenstudio.sandglass.game.view.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.ramenstudio.sandglass.util.view.ui.KeyboardControlButton;

/**
 * Displays a completed level screen, showing stats, buttons for restart, next
 * and back to menu.
 */
public class LevelCompleteView extends Table {
  public final KeyboardControlButton restartButton;
  
  public final KeyboardControlButton nextLevelButton;
  
  public final KeyboardControlButton mainMenuButton;
  
  private Label scoreLabel;
  
  private String yourTime;
  
  private String bestTime;
  
  private final Label titleLabel;
  
  // is there a new high score?
  public boolean newHighScore = false;

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
    
    float w = 250f;
    float h = 60f;

    nextLevelButton = new KeyboardControlButton("NEXT LEVEL", skin, "white");
    restartButton = new KeyboardControlButton("RESTART", skin, "white");
    mainMenuButton = new KeyboardControlButton("MAIN MENU", skin, "white");
    
    if (didWin) {
      row();
      
      if(newHighScore){
    	  scoreLabel = new Label("New Best Time - " + bestTime, skin, "font_gotham-_light_24pt", Color.WHITE);
      } else {
    	  scoreLabel = new Label("Your Time - " + yourTime + "  ||  Best Time - " + bestTime, skin, "font_gotham-_light_24pt", Color.WHITE);
      }
      
      add(scoreLabel).pad(20);
      
      row();
      
      add(nextLevelButton).prefSize(w, h).pad(20);

      restartButton.setArrowUp(nextLevelButton);
      mainMenuButton.setArrowDown(nextLevelButton);
    } else {
      restartButton.setArrowUp(mainMenuButton);
      mainMenuButton.setArrowDown(restartButton);
    }
    
    nextLevelButton.setArrowDown(restartButton);
    nextLevelButton.setArrowUp(mainMenuButton);
    restartButton.setArrowDown(mainMenuButton);
    mainMenuButton.setArrowUp(restartButton);
    
    row();
    
    add(restartButton).prefSize(w, h).pad(20);

    row();
    
    add(mainMenuButton).prefSize(w, h).pad(20);
  }
  
  public void setTime(int yTime, int bTime){
	  if (newHighScore){
		  scoreLabel.setText("New Best Time - " + addZero(yTime/60) + ":" + addZero(yTime%60));
	  } else {
		  scoreLabel.setText("Your Time - " + addZero(yTime/60) + ":" + addZero(yTime%60) + 
				  "  ||  Best Time - " + addZero(bTime/60) + ":" + addZero(bTime%60));
	  }
  }
  
  public String addZero(int time) {
	  if (time < 10){
		  return "0" + time;
	  }
	  else {
		  return ""+time;
	  }
  }
}
