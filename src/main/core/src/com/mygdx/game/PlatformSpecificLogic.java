package com.mygdx.game;

import edu.udel.jatlas.google.GoogleAppsFacade;

public interface PlatformSpecificLogic {
	public void writeResult(int x);
	public GoogleAppsFacade getGoogleApps();
}
