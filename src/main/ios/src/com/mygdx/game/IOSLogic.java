package com.mygdx.game;

import edu.udel.jatlas.google.GoogleAppsFacade;
import edu.udel.jatlas.google.GoogleAppsHttp;

public class IOSLogic implements PlatformSpecificLogic {
	private GoogleAppsFacade gap;
	public IOSLogic() {
		gap = new GoogleAppsHttp();
	}
	public void writeResult(int x) {
	}

	public GoogleAppsFacade getGoogleApps() {
		return gap;
	}

}
