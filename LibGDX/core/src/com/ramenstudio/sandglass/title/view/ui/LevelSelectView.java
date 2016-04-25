package com.ramenstudio.sandglass.title.view.ui;

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
  public final TextButton backButton;
  
  public final ScrollPane levelScrollPane;
  
  public final LevelScrollView levelScrollView;
  /**
   * Default constructor. Initializes all the UI components with the given skin.
   */
  public LevelSelectView(Skin skin) {
    super();
    
    setFillParent(true);
    
    // Create a button to go back.
    backButton = new TextButton("BACK", skin);
    
    add(backButton).bottom().left().pad(20);
    
    levelScrollView = new LevelScrollView(skin);
    
    levelScrollPane = new ScrollPane(levelScrollView, skin);
    add(levelScrollPane).expand();
    
    // Add appropriate number of levels. This SHOULD be done by TitleController.
    // Now we just add some static levels here.
    
  }
}
