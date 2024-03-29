package com.ramenstudio.sandglass;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.ramenstudio.sandglass.game.GameMode;
import com.ramenstudio.sandglass.title.LoadingMode;
import com.ramenstudio.sandglass.title.TitleMode;
import com.ramenstudio.sandglass.title.controller.UIController;
import com.ramenstudio.sandglass.util.ScreenListener;
import com.ramenstudio.sandglass.util.controller.SoundController;

/**
 * The platform-independent root class that initializes the base MVC for the
 * game.
 * 
 * @author Jiacong Xu
 */
public class GDXRoot extends Game implements ScreenListener {
	/** Loads and manages assets. */
	private AssetManager manager;

	/** The three game modes */
	private LoadingMode loadingMode;
	private TitleMode titleMode;
	private GameMode gameMode;

	public enum ApplicationMode {
		TITLE, GAME, LOADING
	}

	/**
	 * Default initializer that initializes the asset manager.
	 */
	public GDXRoot() {
		// Initializes manager.
		manager = new AssetManager();
	}

	/**
	 * We dispose a screen and set the new screen given the mode. We assume the
	 * screen we are setting to is correctly configured before this function is
	 * called.
	 * 
	 * @param mode represents which of the three modes of the application we want
	 * to switch to.
	 */
	public void setApplicationMode(ApplicationMode mode) {
		switch (mode) {
			case LOADING:
				setScreen(loadingMode);
				break;
			case TITLE:
				
				setScreen(titleMode);
				SoundController.getInstance().stopAll();
				UIController.playBGM(titleMode.getTitleController().uiController.getUIState());
				break;
			case GAME:
				setScreen(gameMode);
				break;
		}
	}

	/** 
	 * Called when the Application is first created.
	 * 
	 * This method immediately loads assets for the loading screen, and prepares
	 * the asynchronous loader for all other assets.
	 */
	@Override
	public void create() {
		// We create all the modes we need. A title mode, a game mode, and a loading
		// mode. We don't need to load anything yet, though.
		loadingMode = new LoadingMode();
		titleMode = new TitleMode();
		titleMode.screenListener = this;
		loadingMode.manager = manager;
		SoundController.getInstance().preLoadSounds(manager);
		loadingMode.screenListener = this;
		setApplicationMode(ApplicationMode.LOADING);
	}

	/** 
	 * Called when the Application is destroyed.
	 *
	 * pause() is called before this. We should recursively unload all assets.
	 */
	@Override
	public void dispose() {
		// Call dispose on our children

		// Unload all of the resources
		manager.clear();
		manager.dispose();
		super.dispose();
	}

	/**
	 * Gives up control on the current mode and transition to a new mode instead.
	 * The mode codes are follows:
	 * 0    go to menu
	 * 1+   go to that level
	 */
	@Override
	public void transitionToMode(Screen screen, int modeCode) {
		// When the given mode wants to exit the mode. This happens when title chose
		// a level to play on, or when game mode is done, or when loading is done.
		if (screen == loadingMode){
			SoundController.getInstance().loadSounds(manager);
			setApplicationMode(ApplicationMode.TITLE);
			loadingMode.dispose();
		}
		else if (modeCode == 0) {
			setApplicationMode(ApplicationMode.TITLE);

		} else {
			SoundController.getInstance().stopAll();
			gameMode = new GameMode(modeCode);
			gameMode.screenListener = this;
			setApplicationMode(ApplicationMode.GAME);
		}
	}
}
