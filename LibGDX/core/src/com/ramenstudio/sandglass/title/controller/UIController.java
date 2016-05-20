package com.ramenstudio.sandglass.title.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.ramenstudio.sandglass.title.controller.UIController.UIState;
import com.ramenstudio.sandglass.title.view.ui.*;
import com.ramenstudio.sandglass.util.controller.KeyboardUIController;
import com.ramenstudio.sandglass.util.controller.SoundController;
import com.ramenstudio.sandglass.util.view.ui.CreditsView;
import com.ramenstudio.sandglass.util.view.ui.KeyboardUIListener;

/**
 * This is the root controller for the title screen, which consists of mainly
 * UI stuff.
 * 
 * @author Jiacong Xu
 */
public class UIController {

	public enum UIState {
		TITLE, OPTIONS, LEVEL_SELECT
	}

	private UIState state;

	private Stage stage;
	private Skin skin = new Skin(Gdx.files.internal("UI/Skin/uiskin.json"));

	public TitleView titleView = new TitleView(skin);

	public CreditsView optionsView = new CreditsView(skin);

	public LevelSelectView levelSelectView = new LevelSelectView(skin);

	private KeyboardUIController keyboardControl = new KeyboardUIController();

	public UIController() {
		stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);
		stage.addActor(titleView);
		stage.addActor(optionsView);
		stage.addActor(levelSelectView);

		// Set up callbacks
		titleView.levelSelectButton.addListener(levelSelectListener);
		titleView.creditsButton.addListener(optionsListener);
		optionsView.backButton.addListener(backListener);
		levelSelectView.backButton.addListener(backListener);

		setUIState(UIState.TITLE);


	}

	// Button listeners
	private KeyboardUIListener levelSelectListener = new KeyboardUIListener() {
		@Override
		public void interacted() {
			setUIState(UIState.LEVEL_SELECT);
		}
	};

	private KeyboardUIListener backListener = new KeyboardUIListener() {
		@Override
		public void interacted() {
			setUIState(UIState.TITLE);
		}
	};

	private KeyboardUIListener optionsListener = new KeyboardUIListener() {
		@Override
		public void interacted() {
			setUIState(UIState.OPTIONS);
		}
	};

	public UIState getUIState(){
		return state;
	}

	public void setUIState(UIState uiState) {
		if (uiState == state) {
			return;
		}

		state = uiState;

		titleView.setVisible(false);
		optionsView.setVisible(false);
		levelSelectView.setVisible(false);

		switch (state) {
			case LEVEL_SELECT:
				SoundController.getInstance().stopAll();
				SoundController.getInstance().playLevelSelect();
				levelSelectView.setVisible(true);
				keyboardControl.setFocusedUI(levelSelectView.levelScrollView.levelPreviewViews.get(0).levelSelectButton);
				break;
			case OPTIONS:
				optionsView.setVisible(true);
				keyboardControl.setFocusedUI(optionsView.backButton);
				break;
			case TITLE:
				SoundController.getInstance().stopAll();
				SoundController.getInstance().playMainMenuBGM();
				titleView.setVisible(true);
				keyboardControl.setFocusedUI(titleView.gameStartButton);
				break;
		}
	}


	public void update(float dt) {
		keyboardControl.update(dt);

		// Tells the level scroll view to scroll the selection
		if (state == UIState.LEVEL_SELECT) {
			int tag = keyboardControl.getFocusedUI().tag();

			if (tag != 0) {
				levelSelectView.backButton.setArrowRight(levelSelectView.levelScrollView.levelPreviewViews.get(tag - 1).levelSelectButton);
				levelSelectView.scrollTo = tag;
			}
		}

		stage.act(dt);
	}

	public void draw() {
		stage.draw();
	}

	/**
	 * Acquires the input processor for UI in this mode. Must be called after
	 * switching modes.
	 */
	public void acquireInputProcesser() {
		Gdx.input.setInputProcessor(stage);
	}

	public static void playBGM(UIState uiState) {
		switch (uiState){
			case LEVEL_SELECT :
				SoundController.getInstance().stopAll();
				SoundController.getInstance().playLevelSelect();
				break;
			case OPTIONS :
				break;
			case TITLE :
				SoundController.getInstance().stopAll();
				SoundController.getInstance().playMainMenuBGM();
				break;
			default :
				break;
			
		}
	}
}
