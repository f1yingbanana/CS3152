package com.ramenstudio.sandglass.game.view.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

/**
 * Defines the UI used for playing state of the game.
 * 
 * @author Jiacong Xu
 */
public class GameView extends Table {
  /** The pause button for the game. */
  public final TextButton pauseButton;
  
  public final Label flipCountLabel;
  
  public final Label shipPieceCountLabel;
  
  /**
   * Default constructor. Uses the given skin to set up the pause screen UI.
   * @param skin is the style-sheet for the widgets.
   */
  public GameView(Skin skin, int level) {
    super();
    
    this.setFillParent(true);
    
    if (level < 5){
    	this.setBackground(new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("UI/UI_LEVEL_Type1.png")))));
    } else if (level < 9) {
    	this.setBackground(new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("UI/UI_LEVEL_Type2.png")))));
    } else {
    	this.setBackground(new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("UI/UI_LEVEL_Type3.png")))));
    }

    flipCountLabel = new Label("10", skin);
    add(flipCountLabel).top().left().padLeft(65).padTop(83);

    shipPieceCountLabel = new Label("10", skin);
    add(shipPieceCountLabel).top().right().padTop(262).padRight(5).width(50);
    
    
    row();
    
    pauseButton = new TextButton("         ", skin);
    add(pauseButton).expand().bottom().right().padTop(60).padLeft(40);
    
  }
  
  public void setFlipCount(int flipsLeft) {
    flipCountLabel.setText("" + flipsLeft);
  }
  
  public void setShipPieceCount(int shipPiecesLeft, int maxShipPieces) {
    shipPieceCountLabel.setText("" + (maxShipPieces - shipPiecesLeft));
  }
}

