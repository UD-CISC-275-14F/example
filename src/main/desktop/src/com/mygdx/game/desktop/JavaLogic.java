package com.mygdx.game.desktop;

import com.mygdx.game.PlatformSpecificLogic;

import edu.udel.jatlas.google.GoogleAppsFacade;
import edu.udel.jatlas.google.GoogleAppsHttp;

public class JavaLogic implements PlatformSpecificLogic {
	private GoogleAppsHttp gap;
	public JavaLogic() {
		gap = new GoogleAppsHttp();
	}
	
	public void writeResult(int x) {
		System.out.println("result = " + x);
	}
	
	public GoogleAppsFacade getGoogleApps() {
		return gap;
	}
}
