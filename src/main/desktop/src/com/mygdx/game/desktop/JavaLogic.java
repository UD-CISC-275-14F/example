package com.mygdx.game.desktop;

import edu.udel.jatlas.gdxexample.PlatformSpecificLogic;
import edu.udel.jatlas.gdxexample.google.GoogleAppsFacade;
import edu.udel.jatlas.gdxexample.google.GoogleAppsHttp;

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
