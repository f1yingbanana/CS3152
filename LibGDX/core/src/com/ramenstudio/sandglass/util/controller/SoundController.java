package com.ramenstudio.sandglass.util.controller;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IdentityMap;
import com.badlogic.gdx.utils.IdentityMap.Keys;

/**
 * Modified version of the SoundController provided in lab 4. 
 * 
 * Credits to Professor Walker White for the skeleton implementation
 * of this class.
 * 
 *
 * A singleton class for controlling sound effects in LibGDX
 * 
 * Sound sucks in LibGDX for three reasons.  (1) You have to keep track of
 * a mysterious number every time you play a sound.  (2) You have no idea
 * when a sound has finished playing.  (3) OpenAL bugs on OS X cause popping
 * and distortions if you have no idea what you are doing.  This class 
 * provides a (not so great) solution to all of these.
 * 
 * To get around (1), this sound engine uses a key management system.  
 * Instead of waiting for a number after playing the sound, you give it a
 * key ahead of time.  The key allows you to identify different instances
 * of the same sound.  See our example for collision sounds in the Rocket
 * demo for more.
 * 
 * To get around (2), we have an update() method.  By calling this method
 * you let the SoundController know that time has progressed by one animation
 * frame.  The cooldown prevents you from playing the same instance of a 
 * sound too close together.  In addition, the frame limit prevents you 
 * from playing too many sounds during the same animation frame (which can
 * lead to distortion).  This is not as good as being able to tell when a
 * sound is finished, but it works for most applications.
 * 
 * Finally, for (3), we never actually stop a Sound.  Instead we turn its
 * volume to 0 and allow it to be garbage collected when done.  This is why
 * we never allow you to access a sound object directly.
 */
public class SoundController {

	/**
	 * Inner class to track and active sound instance
	 * 
	 * A sound instance is a Sound object and a number.  That is because
	 * a single Sound object may have multiple instances.  We do not 
	 * know when a sound ends.  Therefore, we simply let the sound go
	 * and we garbage collect when the lifespace is greater than the
	 * sound limit.
	 */
	private class ActiveSound {
		/** Reference to the sound resource */
		public Sound sound;
		/** The id number representing the sound instance */
		public long  id;
		/** Is the sound looping (so no garbage collection) */
		public boolean loop;
		/** How long this sound has been running */
		public long lifespan;
		
		/**
		 * Creates a new active sound with the given values
		 * 
		 * @param s	Reference to the sound resource
		 * @param n The id number representing the sound instance
		 * @param b Is the sound looping (so no garbage collection)
		 */
		public ActiveSound(Sound s, long n, boolean b) {
			sound = s;
			id = n;
			loop = b;
			lifespan = 0;
		}
	}

	/** The default sound cooldown */
	private static final int DEFAULT_COOL = 20;
	/** The default sound length limit */
	private static final int DEFAULT_LIMIT = 120;
	/** The default limit on sounds per frame */
	private static final int DEFAULT_FRAME = 5;
	
	/** The singleton Sound controller instance */
	private static SoundController controller;
	
	/** Keeps track of all of the allocated sound resources */
	private IdentityMap<String,Sound> soundbank;
	/** Keeps track of all of the "active" sounds */
	private IdentityMap<String,ActiveSound> actives;
	/** Keeps track of BGM that should loop */
	private IdentityMap<String,Boolean> loopers;
	/** Support class for garbage collection */
	private Array<String> collection;
	
	
	/** The number of animation frames before a key can be reused */
	private long cooldown;
	/** The maximum amount of animation frames a sound can run */
	private long timeLimit;
	/** The maximum number of sounds we can play each animation frame */
	private int frameLimit;
	/** The number of sounds we have played this animation frame */
	private int current;
	
	private static final String BACKGROUND_01 = "Sounds/Odyssey.mp3";
	private static final String BACKGROUND_01_NAME = "Odyssey";
	
	private static final String MAIN_MENU_01 = "Sounds/MainMenu.mp3";
	private static final String MAIN_MENU_01_NAME = "MainMenu";
	
	private static final String DOOR_OPEN_01 = "Sounds/OPENGate.mp3";
	private static final String DOOR_OPEN_01_NAME = "DoorOpen";
	
	private static final String SHIP_PIECE_COLLECT_01 = "Sounds/vintage_radio_button_006.mp3";
	private static final String SHIP_PIECE_COLLECT_01_NAME = "ShipPieceCollect";
	
	private static final String ITEM_COLLECT_01 = "Sounds/ItemPickUp.mp3";
	private static final String ITEM_COLLECT_01_NAME = "ItemCollect";
	
	private static final String LEVEL_COMPLETE_01 = "Sounds/LevelComplete.mp3";
	private static final String LEVEL_COMPLETE_01_NAME = "LevelComplete";
	
	private static final String LEVEL_FAILED_01 = "Sounds/Fail-Sound.mp3";
	private static final String LEVEL_FAILED_01_NAME = "LevelFail";
	
	private static final String MONSTER_HIT_01 = "Sounds/HitMonster.mp3";
	private static final String MONSTER_HIT_01_NAME = "MonsterCollision";
	
	private static final String SANDGLASS_COLLECT_01 = "Sounds/SandglassIcon.mp3";
	private static final String SANDGLASS_COLLECT_01_NAME = "SandglassIcon";
	
	private static final String ROTATION_01 = "Sounds/rotation.mp3";
	private static final String ROTATION_01_NAME = "Rotation";
	
	private static final String LEVEL_SELECT_01 = "Sounds/Kevin MacLeod-DarkFog.mp3";
	private static final String LEVEL_SELECT_01_NAME = "LevelSelect";

	/** 
	 * Creates a new SoundController with the default settings.
	 */
	private SoundController() {
		soundbank = new IdentityMap<String,Sound>();
		actives = new IdentityMap<String,ActiveSound>();
		loopers = new IdentityMap<String,Boolean>();
		collection = new Array<String>();
		cooldown = DEFAULT_COOL;
		timeLimit = DEFAULT_LIMIT;
		frameLimit = DEFAULT_FRAME;
		current = 0;
	}

	/**
	 * Returns the single instance for the SoundController
	 * 
	 * The first time this is called, it will construct the SoundController.
	 * 
	 * @return the single instance for the SoundController
	 */
	public static SoundController getInstance() {
		if (controller == null) {
			controller = new SoundController();
		}
		return controller;
	}
	
	public void preLoadSounds(AssetManager manager) {
		//System.out.println("Only loading sounds once");
		manager.load(BACKGROUND_01,Sound.class);
		manager.load(LEVEL_SELECT_01,Sound.class);
		manager.load(MAIN_MENU_01,Sound.class);
		manager.load(DOOR_OPEN_01,Sound.class);
		manager.load(SHIP_PIECE_COLLECT_01,Sound.class);
		manager.load(ITEM_COLLECT_01,Sound.class);
		manager.load(LEVEL_COMPLETE_01,Sound.class);
		manager.load(LEVEL_FAILED_01, Sound.class);
		manager.load(MONSTER_HIT_01, Sound.class);
		manager.load(SANDGLASS_COLLECT_01,Sound.class);
		manager.load(ROTATION_01,Sound.class);
		
		manager.finishLoading();
		
		Sound background1 = manager.get(BACKGROUND_01,Sound.class);
		soundbank.put(BACKGROUND_01_NAME,background1);
		loopers.put(BACKGROUND_01, true);
		
		Sound mainmenu1 = manager.get(MAIN_MENU_01,Sound.class);
		soundbank.put(MAIN_MENU_01_NAME,mainmenu1);
		loopers.put(MAIN_MENU_01, true);
		
		Sound levelSelect1 = manager.get(LEVEL_SELECT_01, Sound.class);
		soundbank.put(LEVEL_SELECT_01_NAME, levelSelect1);
		loopers.put(LEVEL_SELECT_01, true);
		
		Sound doorOpen1 = manager.get(DOOR_OPEN_01,Sound.class);
		soundbank.put(DOOR_OPEN_01_NAME, doorOpen1);
		loopers.put(DOOR_OPEN_01, false);
		
		Sound shipPieceCollect1 = manager.get(SHIP_PIECE_COLLECT_01,Sound.class);
		soundbank.put(SHIP_PIECE_COLLECT_01_NAME, shipPieceCollect1);
		loopers.put(SHIP_PIECE_COLLECT_01, false);
		
		Sound itemCollect1 = manager.get(ITEM_COLLECT_01,Sound.class);
		soundbank.put(ITEM_COLLECT_01_NAME, itemCollect1);
		loopers.put(ITEM_COLLECT_01, false);
		
		Sound levelComplete1 = manager.get(LEVEL_COMPLETE_01,Sound.class);
		soundbank.put(LEVEL_COMPLETE_01_NAME, levelComplete1);
		loopers.put(LEVEL_COMPLETE_01, false);
		
		Sound levelFailed1 = manager.get(LEVEL_FAILED_01,Sound.class);
		soundbank.put(LEVEL_FAILED_01_NAME, levelFailed1);
		loopers.put(LEVEL_FAILED_01, false);
		
		Sound monsterHit1 = manager.get(MONSTER_HIT_01,Sound.class);
		soundbank.put(MONSTER_HIT_01_NAME, monsterHit1);
		loopers.put(MONSTER_HIT_01,false);
		
		Sound sandglassCollect1 = manager.get(SANDGLASS_COLLECT_01,Sound.class);
		soundbank.put(SANDGLASS_COLLECT_01_NAME,sandglassCollect1);
		loopers.put(SANDGLASS_COLLECT_01, false);
		
		Sound rotation1 = manager.get(ROTATION_01,Sound.class);
		soundbank.put(ROTATION_01_NAME, rotation1);
		loopers.put(ROTATION_01, false);
	}
	
	/// Properties
	/**
	 * Returns the number of frames before a key can be reused
	 * 
	 * If a key was used very recently, then an attempt to use the key
	 * again means that the sound will be stopped and restarted. This
	 * can cause undesirable artifacts.  So we limit how fast a key
	 * can be reused.
	 * 
	 * @return the number of frames before a key can be reused
	 */
	public long getCoolDown() {
		return cooldown;
	}

	/**
	 * Sets the number of frames before a key can be reused
	 * 
	 * If a key was used very recently, then an attempt to use the key
	 * again means that the sound will be stopped and restarted. This
	 * can cause undesirable artifacts.  So we limit how fast a key
	 * can be reused.
	 * 
	 * param value	the number of frames before a key can be reused
	 */
	public void setCoolDown(long value) {
		cooldown = value;
	}
	
	/**
	 * Returns the maximum amount of animation frames a sound can run
	 * 
	 * Eventually we want to garbage collect sound instances.  Since we cannot
	 * do this, we set an upper bound on all sound effects (default is 2
	 * seconds) and garbage collect when time is up.
	 * 
	 * Sounds on a loop with NEVER be garbage collected.  They must be stopped
	 * manually via stop().
	 * 
	 * @return the maximum amount of animation frames a sound can run
	 */
	public long getTimeLimit() {
		return timeLimit;
	}

	/**
	 * Sets the maximum amount of animation frames a sound can run
	 * 
	 * Eventually we want to garbage collect sound instances.  Since we cannot
	 * do this, we set an upper bound on all sound effects (default is 2
	 * seconds) and garbage collect when time is up.
	 * 
	 * Sounds on a loop with NEVER be garbage collected.  They must be stopped
	 * manually via stop().
	 * 
	 * @param value the maximum amount of animation frames a sound can run
	 */
	public void setTimeLimit(long value) {
		timeLimit = value;
	}
	
	/**
	 * Returns the maximum amount of sounds per animation frame
	 * 
	 * Because of Box2d limitations in LibGDX, you might get a lot of simultaneous
	 * sounds if you try to play sounds on collision.  This in turn can cause
	 * distortions.  We fix that by putting an upper bound on the number of 
	 * simultaneous sounds per animation frame.  If you exceed the number, then
	 * you should wait another frame before playing a sound.
	 * 
	 * @return the maximum amount of sounds per animation frame
	 */
	public int getFrameLimit() {
		return frameLimit;
	}
	
	/**
	 * Sets the maximum amount of sounds per animation frame
	 * 
	 * Because of Box2d limitations in LibGDX, you might get a lot of simultaneous
	 * sounds if you try to play sounds on collision.  This in turn can cause
	 * distortions.  We fix that by putting an upper bound on the number of 
	 * simultaneous sounds per animation frame.  If you exceed the number, then
	 * you should wait another frame before playing a sound.
	 * 
	 * @param value the maximum amount of sounds per animation frame
	 */
	public void setFrameLimit(int value) {
		frameLimit = value;
	}

	/// Sound Management
	/**
	 * Uses the asset manager to allocate a sound
	 * 
	 * All sound assets are managed internally by the controller.  Do not try 
	 * to access the sound directly.  Use the play and stop methods instead.
	 * 
	 * @param manager  A reference to the asset manager loading the sound
	 * @param filename The filename for the sound asset
	 */
	public void allocate(AssetManager manager, String filename) {
		Sound sound = manager.get(filename,Sound.class);
		soundbank.put(filename,sound);
	}

	/**
	 * Plays the an instance of the given sound
	 * 
	 * A sound is identified by its filename.  You can have multiple instances of the
	 * same sound playing.  You use the key to identify a sound instance.  You can only
	 * have one key playing at a time.  If a key is in use, the existing sound may
	 * be garbage collected to allow you to reuse it, depending on the settings.
	 * 
	 * However, it is also possible that the key use may fail.  In the latter case,
	 * this method returns false.  In addition, if the sound is currently looping,
	 * then this method will return true but will not stop and restart the sound.
	 * 
	 * 
	 * @param key		The identifier for this sound instance
	 * @param filename	The filename of the sound asset
	 * @param loop		Whether to loop the sound
	 * 
	 * @return True if the sound was successfully played
	 */
	public boolean play(String key, String filename, boolean loop, boolean isBackground) {
		return play(key,filename,loop,1.0f,isBackground);
	}

	/**
	 * Plays the an instance of the given sound
	 * 
	 * A sound is identified by its filename.  You can have multiple instances of the
	 * same sound playing.  You use the key to identify a sound instance.  You can only
	 * have one key playing at a time.  If a key is in use, the existing sound may
	 * be garbage collected to allow you to reuse it, depending on the settings.
	 * 
	 * However, it is also possible that the key use may fail.  In the latter case,
	 * this method returns false.  In addition, if the sound is currently looping,
	 * then this method will return true but will not stop and restart the sound.
	 * 
	 * 
	 * @param key		The identifier for this sound instance
	 * @param filename	The filename of the sound asset
	 * @param loop		Whether to loop the sound
	 * @param volume	The sound volume in the range [0,1]
	 * @param isBackground Whether or not to stop the sound if we want to play it again.
	 * 
	 * @return True if the sound was successfully played
	 */
	public boolean play(String key, String filename, boolean loop, float volume, boolean isBackground) {
		// Get the sound for the file
		// Original check. Removed frameLimit
//		if (!soundbank.containsKey(filename) || current >= frameLimit) {
//			return false;
//		}

		if (!isBackground) {
			if (!soundbank.containsKey(filename) || current >= frameLimit) {
				return false;
			}
			// If there is a sound for this key, stop it
			Sound sound = soundbank.get(filename);
			if (actives.containsKey(key)) {
				ActiveSound snd = actives.get(key);
				if (!snd.loop && snd.lifespan > cooldown) {
					// This is a workaround for the OS X sound bug
					//snd.sound.stop(snd.id);
					snd.sound.setVolume(snd.id, 0.0f); 
				} else {
					return true;
				}
			}
			
			// Play the new sound and add it
			long id = sound.play(volume);
			if (id == -1) {
				return false;
			} else if (loop) {
				sound.setLooping(id, true);
			}
			
			actives.put(key,new ActiveSound(sound,id,loop));
			current++;
			return true;
		}
		
		else {
			if (!soundbank.containsKey(filename)) {
				System.out.println("No key called " + filename);
				return false;
			}
			// If there is a sound for this key, stop it
			Sound sound = soundbank.get(filename);
			if (actives.containsKey(key)) {
				return true;
			}
			
			// Play the new sound and add it
			long id = sound.play(volume);
			if (id == -1) {
				return false;
			} else if (loop) {
				sound.setLooping(id, true);
			}
			
			actives.put(key,new ActiveSound(sound,id,loop));
			current++;
			return true;
		}
	}
	
	/**
	 * Stops the sound, allowing its key to be reused.
	 * 
	 * This is the only way to stop a sound on a loop.  Otherwise it will
	 * play forever.
	 * 
	 * If there is no sound instance for the key, this method does nothing.
	 * 
	 * @param key	The sound instance to stop.
	 */
	public void stop(String key) {
		// Get the active sound for the key
		if (!actives.containsKey(key)) {
			return;
		}
		
		ActiveSound snd = actives.get(key);
		
		// This is a workaround for the OS X sound bug
		//snd.sound.stop(snd.id);
		snd.sound.setLooping(snd.id,false); // Will eventually garbage collect
		snd.sound.setVolume(snd.id, 0.0f); 
		actives.remove(key);
	}
	
	/**
	 * Returns true if the sound instance is currently active
	 * 
	 * @param key	The sound instance identifier
	 * 
	 * @return true if the sound instance is currently active
	 */
	public boolean isActive(String key) {
		return actives.containsKey(key);
	}
	
	/**
	 * Updates the current frame of the sound controller.
	 * 
	 * This method serves two purposes.  First, it allows us to limit the number
	 * of sounds per animation frame.  In addition it allows us some primitive
	 * garbage collection.
	 */
	public void update() {
		for(String key : actives.keys()) {
			if (loopers.get(key)) {
				continue;
			}
			ActiveSound snd = actives.get(key);
			snd.lifespan++;
			if (snd.lifespan > timeLimit) {
				collection.add(key);
				snd.sound.setLooping(snd.id,false); // Will eventually garbage collect
				snd.sound.setVolume(snd.id, 0.0f); 
			}
		}
		for(String key : collection) {
			actives.remove(key);
		}
		collection.clear();
		current = 0;
	}

	public void playBGMForLevel(int gameLevel) {
		// Have logic based on gameLevel to decide which sound to play, currently playing the same.
//		System.out.println(play(BACKGROUND_01, BACKGROUND_01_NAME,true,true));
		play(BACKGROUND_01, BACKGROUND_01_NAME,true,1.5f, true);
	}

	public void stopAll() {
		Keys<String> activeStringSounds = actives.keys();
		for (String s : activeStringSounds) {
			stop(s);
		}
	}

	public void playDoorOpen() {
//		System.out.println(play(DOOR_OPEN_01, DOOR_OPEN_01_NAME, false, false));
		play(DOOR_OPEN_01, DOOR_OPEN_01_NAME, false, 0.2f, false);
	}
	
	/**
	 * Outdated, not used
	 */
	public void playShipCollect() {
		play(SHIP_PIECE_COLLECT_01, SHIP_PIECE_COLLECT_01_NAME,false, 0.2f, false);
	}

	public void playItemCollect() {
		play(ITEM_COLLECT_01, ITEM_COLLECT_01_NAME, false, .15f, false);
	}
	
	public void playSandglassCollect() {
		play(SANDGLASS_COLLECT_01, SANDGLASS_COLLECT_01_NAME,false, false);
	}
	
	public void playLevelComplete() {
		play(LEVEL_COMPLETE_01, LEVEL_COMPLETE_01_NAME, false, 0.2f, false);
	}
	
	public void playMonsterHit() {
		play(MONSTER_HIT_01, MONSTER_HIT_01_NAME, false, 0.8f, false);
	}
	
	public void playRotation() {
		play(ROTATION_01, ROTATION_01_NAME, false, false);
	}
	
	public void playMainMenuBGM() {
		play(MAIN_MENU_01, MAIN_MENU_01_NAME, true, 0.8f, true);
	}
	
	public void playLevelSelect() {
		play(LEVEL_SELECT_01, LEVEL_SELECT_01_NAME, true, 1.0f, true);
	}
	
	public void dispose() {
		for (String s: collection){
			if (!s.equals("BACKGROUND_01_NAME")){
				Sound snd = soundbank.get(s);
				snd.dispose();
			}
		}
	}
	public void playLost() {
		play(LEVEL_FAILED_01, LEVEL_FAILED_01_NAME, false, false);
	}




}
