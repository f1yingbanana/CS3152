package com.ramenstudio.sandglass.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ramenstudio.sandglass.GDXRoot;

/**
 * The desktop launcher for the game. This class handles player preferences for
 * screen sizes.
 * 
 * @author Jiacong Xu
 */
public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.fullscreen = true;
		
		new LwjglApplication(new GDXRoot(), config);
	}
}
