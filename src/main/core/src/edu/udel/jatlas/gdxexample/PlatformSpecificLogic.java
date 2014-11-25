package edu.udel.jatlas.gdxexample;

import edu.udel.jatlas.gdxexample.google.GoogleAppsFacade;

public interface PlatformSpecificLogic {
	public void writeResult(int x);
	public GoogleAppsFacade getGoogleApps();
}
