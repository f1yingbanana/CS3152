package com.ramenstudio.sandglass.title;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.ramenstudio.sandglass.title.controller.TitleController;
import com.ramenstudio.sandglass.util.AbstractMode;

/**
 * The mode when choosing a level to play. This includes score book-keeping,
 * UI displays for level selection, and so on.
 * 
 * @author flyingbanana
 */
public class TitleMode extends AbstractMode implements Screen {

  TitleController titleController = new TitleController();
  
  Color bgColor = Color.BLACK;
  
  @Override
  public void show() {
    // UI needs to be shown.
    titleController.uiController.acquireInputProcesser();
  }

  @Override
  public void render(float delta) {
    // Update controllers then run.
    titleController.update(delta);
    
    // Clear color
    Gdx.gl.glClearColor(bgColor.r, bgColor.b, bgColor.g, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    
    titleController.draw();
    
    // Flags
    if (titleController.levelSelected != null) {
      screenListener.transitionToMode(this, titleController.levelSelected);
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
}
