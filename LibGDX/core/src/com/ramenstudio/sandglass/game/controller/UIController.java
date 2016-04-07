package com.ramenstudio.sandglass.game.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.ramenstudio.sandglass.game.model.GameState;
import com.ramenstudio.sandglass.game.view.GameCanvas;

/**
 * The root controller for all UI-related functionalities in-game. This keeps
 * the stage object.
 * 
 * @author Jiacong Xu
 */
public class UIController extends AbstractController {
  private Stage stage;
  private Skin skin = new Skin(Gdx.files.internal("UI/Skin/uiskin.json"));
  
  private GameState gameState;
  
  private Table playingTable;
  private Table pausedTable;

  // Outlets for playing table
  public final TextButton pauseButton = new TextButton("PAUSE", skin);
  
  // Outlets for paused table
  public final TextButton restartButton = new TextButton("RESTART", skin);
  public final TextButton resumeButton = new TextButton("RESUME", skin);
  public final TextButton optionButton = new TextButton("OPTIONS", skin);
  public final TextButton mainMenuButton = new TextButton("MAIN MENU", skin);
  
  public UIController() {
    stage = new Stage(new ScreenViewport());
    Gdx.input.setInputProcessor(stage);
    
    // Create a table that fills the screen. Everything will go inside
    playingTable = new Table();
    playingTable.setFillParent(true);
    stage.addActor(playingTable);
    
    // Add an image actor. Have to set the size, else it would be the size of the drawable (which is the 1x1 texture).
    // table.add(new Image(skin.newDrawable("white", Color.GRAY))).size(64);
    playingTable.add(pauseButton).prefSize(120, 50).expand().top().right().pad(20);
    
    pauseButton.addListener(new ChangeListener() {
      public void changed (ChangeEvent event, Actor actor) {
        setGameState(GameState.PAUSED);
      }
    });
    
    // Set up PAUSED SCREEN
    pausedTable = new Table();
    pausedTable.setFillParent(true);
    pausedTable.setBackground(skin.newDrawable("white", new Color(0, 0, 0, 0.8f)));
    stage.addActor(pausedTable);
    
    pausedTable.add(restartButton).prefSize(160, 50).pad(20);
    
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
    
    pausedTable.row();
    
    pausedTable.add(resumeButton).prefSize(160, 50).pad(20);

    resumeButton.addListener(new ClickListener() {
      public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        resumeButton.setText("-     RESUME     -");
      }
      public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        resumeButton.setText("RESUME");
      }
    });

    pausedTable.row();
    
    //optionButton.setDisabled(true);
    //optionButton.setTouchable(Touchable.disabled);
    pausedTable.add(optionButton).prefSize(160, 50).pad(20);
    
    optionButton.addListener(new ClickListener() {
      public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        optionButton.setText("-     OPTIONS     -");
      }
      public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        optionButton.setText("OPTIONS");
      }
    });
    
    pausedTable.row();
    
    //mainMenuButton.setDisabled(true);
    //mainMenuButton.setTouchable(Touchable.disabled);
    pausedTable.add(mainMenuButton).prefSize(160, 50).pad(20);
    
    mainMenuButton.addListener(new ClickListener() {
      public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        mainMenuButton.setText("-   MAIN MENU   -");
      }
      public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        mainMenuButton.setText("MAIN MENU");
      }
    });
    
    setGameState(GameState.PLAYING);
  }
  
  /**
   * Sets which game state we are in. Uses this to switch visibility of
   * different UIs.
   * 
   * @param state is the current play status of the game.
   */
  public void setGameState(GameState state) {
    if (state == gameState) {
      return;
    }
    
    // Hide all tables
    playingTable.setVisible(false);
    pausedTable.setVisible(false);
    
    switch (state) {
    case LOST:
      break;
    case PAUSED:
      pausedTable.setVisible(true);
      break;
    case PLAYING:
      playingTable.setVisible(true);
      break;
    case WON:
      break;
    }
  }
  
  @Override
  public void update(float dt) {
    stage.act(dt);
  }
  
  @Override
  public void draw(GameCanvas canvas) {
    stage.draw();
  }

}
