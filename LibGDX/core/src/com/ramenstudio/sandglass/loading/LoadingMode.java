package com.ramenstudio.sandglass.loading;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.*;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.ramenstudio.sandglass.game.GameMode;
import com.ramenstudio.sandglass.game.view.GameCanvas;
import com.ramenstudio.sandglass.util.ScreenListener;
import com.ramenstudio.sandglass.util.controller.SoundController;
import com.sun.j3d.utils.scenegraph.io.retained.Controller;

/**
 * The overall container for a loading mode. This displays pretty graphics while
 * the game loads.
 * It is called only once when any of the screen should be displayed.
 * 
 * @author gnsrom
 */
public class LoadingMode implements Screen, InputProcessor {

	// Textures necessary to support the loading screen
	private static String PROGRESS_FILE;
	
	private static String BACKGROUND_FILE;
	
	private static String PLAYBUTTON;
	
	private GameCanvas canvas = new GameCanvas();
	
	private SpriteBatch spritebatch = new SpriteBatch();
	
	private Array<String> assets = new Array<String>();

	/** Background texture for start-up */
	private Texture background;
	/** Play button to display when done */
	private Texture playButton;
	/** Texture atlas to support a progress bar */
	private Texture statusBar;

	/** Default budget for asset loader (do nothing but load 60 fps) */
	private static int DEFAULT_BUDGET = 15;
	/** Standard window size (for scaling) */
	private static int STANDARD_WIDTH  = 800;
	/** Standard window height (for scaling) */
	private static int STANDARD_HEIGHT = 700;
	/** Ratio of the bar width to the screen */
	private static float BAR_WIDTH_RATIO  = 0.66f;
	/** Ration of the bar height to the screen */
	private static float BAR_HEIGHT_RATIO = 0.25f;	
	/** Height of the progress bar */
	private static int PROGRESS_HEIGHT = 30;
	/** Width of the rounded cap on left or right */
	private static int PROGRESS_CAP    = 15;
	/** Width of the middle portion in texture atlas */
	private static int PROGRESS_MIDDLE = 200;
	/** Amount to scale the play button */
	private static float BUTTON_SCALE  = 0.75f;
	
	/** Start button for XBox controller on Windows */
	private static int WINDOWS_START = 7;
	/** Start button for XBox controller on Mac OS X */
	private static int MAC_OS_X_START = 4;

	// statusbar
	/** Left cap to the status background (grey region) */
	private TextureRegion statusBkgLeft;
	/** Middle portion of the status background (grey region) */
	private TextureRegion statusBkgMiddle;
	/** Right cap to the status background (grey region) */
	private TextureRegion statusBkgRight;
	/** Left cap to the status forground (colored region) */
	private TextureRegion statusFrgLeft;
	/** Middle portion of the status forground (colored region) */
	private TextureRegion statusFrgMiddle;
	/** Right cap to the status forground (colored region) */
	private TextureRegion statusFrgRight;

	/** AssetManager to be loading in the background */
	private AssetManager manager;
	/** Listener that will update the player mode when we are done */
	private ScreenListener listener;
	/** Sound controller*/
	private SoundController soundcontroller;
	/** Game State that contains this loading mode.*/

	/** The width of the progress bar */
	private int width;
	/** The y-coordinate of the center of the progress bar */
	private int centerY;
	/** The x-coordinate of the center of the progress bar */
	private int centerX;
	/**
	 * The height of the canvas window (necessary since sprite origin != screen
	 * origin)
	 */
	private int heightY;
	/** Scaling factor for when the student changes the resolution. */
	private float scale;

	/** Current progress (0 to 1) of the asset manager */
	private float progress;
	/** The current state of the play button */
	private int   pressState;
	/** The amount of time to devote to loading assets */
	private int   budget;
	/** Whether or not this player mode is still active */
	private boolean active;
	/** playButton directory*/
	private static String playButtondir;
	/** animation directory*/
	private static String animationdir;
	/** background directory*/
	private static String backgroundir;

	/**
	* returns the budget for the asset loader.
	* The budget is the number of milliseconds to spend loading assets each animation
	* frame. 
	*/
	public int getBudget() {
		return budget;
	}

	public void setBudget(int millis) {
		budget = millis;
	}

	/**
	 * Returns true if all assets are loaded and the player is ready to go.
	 *
	 * @return true if the player is ready to go
	 */
	public boolean isReady() {
		return pressState == 2;
	}

	public LoadingMode(GameMode gamestate, AssetManager manager, SoundController soundctrl) {
		this(manager, soundctrl, DEFAULT_BUDGET);
	}


	/** 
	* Creates a LoadingMode with default size and position
	*
	* Because it is called by the GameController, it needs to have the
	* String array of textures and sound directories of the level to be loaded.
	* That array is kept in the GameModel class, from which we can get
	* Sound and Texture by getSound or getImage method.
	*/
	public LoadingMode(AssetManager manager, SoundController soundctrl, int millis) {
		playButton = null;
		this.manager = manager;
		soundcontroller = soundctrl;
		budget = millis;

		// Compute the dimensions from the canvas

		// Load the next two images immediately.
		statusBar = new Texture(PROGRESS_FILE);

		// No progress so far.
		progress = 0;
		active = false;

		// Break up the status bar texture into regions
		statusBkgLeft = new TextureRegion(statusBar, 0, 0, PROGRESS_CAP, PROGRESS_HEIGHT);
		statusBkgRight = new TextureRegion(statusBar, statusBar.getWidth() - PROGRESS_CAP, 0, PROGRESS_CAP,
			PROGRESS_HEIGHT);
		statusBkgMiddle = new TextureRegion(statusBar, PROGRESS_CAP, 0, PROGRESS_MIDDLE, PROGRESS_HEIGHT);

		int offset = statusBar.getHeight() - PROGRESS_HEIGHT;

		statusFrgLeft = new TextureRegion(statusBar, 0, offset, PROGRESS_CAP, PROGRESS_HEIGHT);
		statusFrgRight = new TextureRegion(statusBar, statusBar.getWidth() - PROGRESS_CAP, offset, PROGRESS_CAP,
			PROGRESS_HEIGHT);
		statusFrgMiddle = new TextureRegion(statusBar, PROGRESS_CAP, offset, PROGRESS_MIDDLE, PROGRESS_HEIGHT);

		Gdx.input.setInputProcessor(this);

		active = true;
	}

	public void setPlayButton(String playButton){
		this.playButton = new Texture(playButton);
	}
	
	// public void setAnimation(String animation){
	// 	this.animation = new 
	// }

	public void setBackground(String background){
		this.background = new Texture(background);
	}



	public TextureRegion[] loadTexture(String[] textures) {
		TextureRegion[] tex = new TextureRegion[textures.length];

		for (int i = 0; i < textures.length; i++) {
			manager.load(textures[i], Texture.class);
			assets.add(textures[i]);
			tex[i]=createTexture(manager, textures[i], true);
		}
		return tex;

		// should use gameModel. setAssets(tex)
	}

	public Sound[] loadSound(String[] sounds) {
		Sound[] snd = new Sound[sounds.length];
		for (int i = 0; i < sounds.length; i++) {
			manager.load(sounds[i], Sound.class);
			Sound sound = manager.get(sounds[i], Sound.class);
			assets.add(sounds[i]);
			soundcontroller.allocate(manager,sounds[i]);
			snd[i] = sound;
		}
		return snd;
	}

	protected TextureRegion createTexture(AssetManager manager, String file, boolean repeat) {
		if (manager.isLoaded(file)) {
			TextureRegion region = new TextureRegion(manager.get(file, Texture.class));
			region.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
			if (repeat) {
				region.getTexture().setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
			}
			return region;
		}
		return null;
	}


	public void unloadContent(Array<String> assets){
	  
		for (String s: assets){
			if (manager.isLoaded(s)){
				manager.unload(s);
			}	
		}
	}

	private void update(float delta) {
		if (playButton == null) {
			manager.update(budget);
			this.progress = manager.getProgress();
			if (progress >= 1.0f) {
				progress = 1.0f;
				playButton = new Texture(PLAYBUTTON);
				playButton.setFilter(TextureFilter.Linear, TextureFilter.Linear);
				
			}
		}
	}

	
	private void draw() {
		canvas.begin(new Matrix4());
		spritebatch.draw(background, 0,0);
		if (playButton == null) {
			drawProgress(canvas);
		} else {
			Color tint = (pressState == 1 ? Color.GRAY: Color.WHITE);
			spritebatch.setColor(tint);
			spritebatch.draw(playButton, centerX-playButton.getWidth()/2, centerY- (float) playButton.getHeight()/2,
					 playButton.getWidth()*BUTTON_SCALE*scale, playButton.getHeight()*BUTTON_SCALE*scale);
		}
		canvas.end();
	}

	
	/**
	 * Called when the Screen should render itself.
	 *
	 * We defer to the other methods update() and draw().  However, it is VERY important
	 * that we only quit AFTER a draw.
	 *
	 * @param delta Number of seconds since last animation frame
	 */
	@Override
	public void render(float delta) {
		if (active) {
			update(delta);
			draw();

			// We are are ready, notify our listener
			if (isReady() && listener != null) {
				listener.transitionToMode(this, 0);
			}
		}
	}

	@Override
	public void resize(int width, int height) {
		float sx = ((float) width) / STANDARD_WIDTH;
		float sy = ((float) height) / STANDARD_HEIGHT;
		scale = (sx < sy ? sx : sy);

		this.width = (int) (BAR_WIDTH_RATIO * width);
		centerY = (int) (BAR_HEIGHT_RATIO * height);
		centerX = width / 2;
		heightY = height;
	}

	
	private void drawProgress(GameCanvas canvas) {

		// draw status bar
		spritebatch.setColor(Color.WHITE);
		spritebatch.draw(statusBkgLeft, centerX - width / 2, centerY, scale * PROGRESS_CAP,
			scale * PROGRESS_HEIGHT);
		spritebatch.draw(statusBkgRight, centerX + width / 2 - scale * PROGRESS_CAP, centerY,
			scale * PROGRESS_CAP, scale * PROGRESS_HEIGHT);
		spritebatch.draw(statusBkgMiddle, centerX - width / 2 + scale * PROGRESS_CAP, centerY,
			width - 2 * scale * PROGRESS_CAP, scale * PROGRESS_HEIGHT);

		spritebatch.draw(statusFrgLeft, centerX - width / 2, centerY, scale * PROGRESS_CAP,
				scale * PROGRESS_HEIGHT);
		
		if (progress > 0) {
			float span = progress * (width - 2 * scale * PROGRESS_CAP) / 2.0f;
			spritebatch.draw(statusFrgRight, centerX - width / 2 + scale * PROGRESS_CAP + span, centerY,
				scale * PROGRESS_CAP, scale * PROGRESS_HEIGHT);
			spritebatch.draw(statusFrgMiddle, centerX - width / 2 + scale * PROGRESS_CAP, centerY, span,
				scale * PROGRESS_HEIGHT);
		} else {
			spritebatch.draw(statusFrgRight, centerX - width / 2 + scale * PROGRESS_CAP, centerY,
				scale * PROGRESS_CAP, scale * PROGRESS_HEIGHT);
		}
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
	public void show() {
		active = true;
	}

	@Override
	public void hide() {
		active = false;
	}

	/**
	 * Sets the ScreenListener for this mode
	 *
	 * The ScreenListener will respond to requests to quit.
	 */
	public void setScreenListener(ScreenListener listener) {
		this.listener = listener;
	}

	@Override
	public void dispose() {
		statusBkgLeft = null;
		statusBkgRight = null;
		statusBkgMiddle = null;

		statusFrgLeft = null;
		statusFrgRight = null;
		statusFrgMiddle = null;

		background.dispose();
		statusBar.dispose();
		background = null;
		statusBar  = null;
	}
	
	public boolean keyDown(int keycode){
		return true;
	}

	/** 
	 * Called when a key is released.
	 * 
	 * We allow key commands to start the game this time.
	 *
	 * @param keycode the key released
	 * @return whether to hand the event to other listeners. 
	 */	
	public boolean keyUp(int keycode) { 
		if (keycode == Input.Keys.SPACE || keycode == Input.Keys.ENTER) {
			pressState = 2;
			return false;			
		}
		return true; 
	}
	
	/** 
	 * Called when the mouse was moved without any buttons being pressed. (UNSUPPORTED)
	 *
	 * @param screenX the x-coordinate of the mouse on the screen
	 * @param screenY the y-coordinate of the mouse on the screen
	 * @return whether to hand the event to other listeners. 
	 */	
	public boolean mouseMoved(int screenX, int screenY) { 
		return true; 
	}

	/** 
	 * Called when the mouse wheel was scrolled. (UNSUPPORTED)
	 *
	 * @param amount the amount of scroll from the wheel
	 * @return whether to hand the event to other listeners. 
	 */	
	public boolean scrolled(int amount) { 
		return true; 
	}

	/** 
	 * Called when the mouse or finger was dragged. (UNSUPPORTED)
	 *
	 * @param screenX the x-coordinate of the mouse on the screen
	 * @param screenY the y-coordinate of the mouse on the screen
	 * @param pointer the button or touch finger number
	 * @return whether to hand the event to other listeners. 
	 */		
	public boolean touchDragged(int screenX, int screenY, int pointer) { 
		return true; 
	}
	
	// UNSUPPORTED METHODS FROM ControllerListener
	
	/**
	 * Called when a controller is connected. (UNSUPPORTED)
	 *
	 * @param controller The game controller
	 */
	public void connected (Controller controller) {}

	/**
	 * Called when a controller is disconnected. (UNSUPPORTED)
	 *
	 * @param controller The game controller
	 */
	public void disconnected (Controller controller) {}

	/** 
	 * Called when an axis on the Controller moved. (UNSUPPORTED) 
	 *
	 * The axisCode is controller specific. The axis value is in the range [-1, 1]. 
	 *
	 * @param controller The game controller
	 * @param axisCode 	The axis moved
	 * @param value 	The axis value, -1 to 1
	 * @return whether to hand the event to other listeners. 
	 */
	public boolean axisMoved (Controller controller, int axisCode, float value) {
		return true;
	}

	/** 
	 * Called when a POV on the Controller moved. (UNSUPPORTED) 
	 *
	 * The povCode is controller specific. The value is a cardinal direction. 
	 *
	 * @param controller The game controller
	 * @param povCode 	The POV controller moved
	 * @param value 	The direction of the POV
	 * @return whether to hand the event to other listeners. 
	 */
	public boolean povMoved (Controller controller, int povCode, boolean value) {
		return true;
	}

	/** 
	 * Called when an x-slider on the Controller moved. (UNSUPPORTED) 
	 *
	 * The x-slider is controller specific. 
	 *
	 * @param controller The game controller
	 * @param sliderCode The slider controller moved
	 * @param value 	 The direction of the slider
	 * @return whether to hand the event to other listeners. 
	 */
	public boolean xSliderMoved (Controller controller, int sliderCode, boolean value) {
		return true;
	}

	/** 
	 * Called when a y-slider on the Controller moved. (UNSUPPORTED) 
	 *
	 * The y-slider is controller specific. 
	 *
	 * @param controller The game controller
	 * @param sliderCode The slider controller moved
	 * @param value 	 The direction of the slider
	 * @return whether to hand the event to other listeners. 
	 */
	public boolean ySliderMoved (Controller controller, int sliderCode, boolean value) {
		return true;
	}


	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (playButton == null || pressState == 2) {
			return true;
		}
		
		// Flip to match graphics coordinates
		screenY = heightY-screenY;
		
		// Play button is a circle.
		float radius = BUTTON_SCALE*scale*playButton.getWidth()/2.0f;
		float dist = (screenX-centerX)*(screenX-centerX)+(screenY-centerY)*(screenY-centerY);
		if (dist < radius*radius) {
			pressState = 1;
		}
		return false;
	}

	/** 
	 * Called when a finger was lifted or a mouse button was released.
	 *
	 * This method checks to see if the play button is currently pressed down. If so, 
	 * it signals the that the player is ready to go.
	 *
	 * @param screenX the x-coordinate of the mouse on the screen
	 * @param screenY the y-coordinate of the mouse on the screen
	 * @param pointer the button or touch finger number
	 * @return whether to hand the event to other listeners. 
	 */	
	public boolean touchUp(int screenX, int screenY, int pointer, int button) { 
		if (pressState == 1) {
			pressState = 2;
			return false;
		}
		return true;
	}

	/** 
	 * Called when a button on the Controller was pressed. 
	 *
	 * The buttonCode is controller specific. This listener only supports the start
	 * button on an X-Box controller.  This outcome of this method is identical to 
	 * pressing (but not releasing) the play button.
	 *
	 * @param controller The game controller
	 * @param buttonCode The button pressed
	 * @return whether to hand the event to other listeners. 
	 */
	public boolean buttonDown (Controller controller) {
		if (pressState == 0) {
			pressState = 1;
			return false;
		}
		return true;
	}

	/** 
	 * Called when a button on the Controller was released. 
	 *
	 * The buttonCode is controller specific. This listener only supports the start
	 * button on an X-Box controller.  This outcome of this method is identical to 
	 * releasing the the play button after pressing it.
	 *
	 * @param controller The game controller
	 * @param buttonCode The button pressed
	 * @return whether to hand the event to other listeners. 
	 */
	public boolean buttonUp (Controller controller) {
		if (pressState == 1) {
			pressState = 2;
			return false;
		}
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}
}
