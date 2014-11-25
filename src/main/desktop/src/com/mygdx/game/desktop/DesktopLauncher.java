package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.MyGdxGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		launch();
	}
	
	// force only one launch
	private static boolean INIT = false;
	public static synchronized void launch() {
		if (!INIT) {
			INIT = true;
			LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
			config.width = 800;
			config.height = 480;
			new LwjglApplication(new MyGdxGame(new JavaLogic()), config);
		}
	}
}
