package com.ramenstudio.sandglass.title;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.ramenstudio.sandglass.title.controller.TitleController;
import com.ramenstudio.sandglass.title.controller.UIController;
import com.ramenstudio.sandglass.util.AbstractMode;

/**
 * The mode when choosing a level to play. This includes score book-keeping,
 * UI displays for level selection, and so on.
 * 
 * @author flyingbanana
 */
public class TitleMode extends AbstractMode implements Screen {

  private TitleController titleController = new TitleController();
  
  Color bgColor = Color.BLACK;
  
  @Override
  public void show() {
    // UI needs to be shown.
    getTitleController().uiController.acquireInputProcesser();
    Gdx.input.setCursorCatched(true);
  }

  @Override
  public void render(float delta) {
    // Update controllers then run.
    getTitleController().update(delta);
    
    // Clear color
    Gdx.gl.glClearColor(bgColor.r, bgColor.b, bgColor.g, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    
    getTitleController().draw();
    
    // Flags
    if (getTitleController().levelSelected != null) {
      int level = getTitleController().levelSelected;
      getTitleController().levelSelected = null;
      screenListener.transitionToMode(this, level);
    }
  }

  @Override
  public void resize(int width, int height) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void pause() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void resume() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void hide() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void dispose() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public String[] getResourcePaths() {
    // TODO Auto-generated method stub
    return null;
  }

public TitleController getTitleController() {
	return titleController;
}

public void setTitleController(TitleController titleController) {
	this.titleController = titleController;
}
}
