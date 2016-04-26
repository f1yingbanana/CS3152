package com.ramenstudio.sandglass.title.view.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
/**
 * Displays a list of levels, each can be displayed as locked or unlocked.
 * 
 * @author Jiacong Xu
 */
public class LevelSelectView extends Table {
  public final Label titleLabel;
  
  public final TextButton backButton;
  
  public final ScrollPane levelScrollPane;
  
  public final LevelScrollView levelScrollView;
  /**
   * Default constructor. Initializes all the UI components with the given skin.
   */
  public LevelSelectView(Skin skin) {
    super();
    
    setFillParent(true);
    
    Table tbTable = new Table();
    
    titleLabel = new Label("SELECT LEVEL", skin, "title");
    tbTable.add(titleLabel).top().left().pad(20);
    
    tbTable.row();
    
    backButton = new TextButton("BACK", skin);
    
    tbTable.add(backButton).bottom().left().pad(20).fill();
    
    add(tbTable).fill();
    
    levelScrollView = new LevelScrollView(skin);
    
    levelScrollPane = new ScrollPane(levelScrollView, skin);
    add(levelScrollPane).expand();
  }
}
