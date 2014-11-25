package com.mygdx.game.android;

import android.widget.Toast;
import edu.udel.jatlas.gdxexample.PlatformSpecificLogic;
import edu.udel.jatlas.gdxexample.google.GoogleAppsFacade;
import edu.udel.jatlas.gdxexample.google.GoogleAppsHttp;

public class AndroidLogic implements PlatformSpecificLogic {
	private AndroidLauncher context;
	private GoogleAppsHttp gap;

	public AndroidLogic(AndroidLauncher context) {
		super();
		this.context = context;
		this.gap = new GoogleAppsHttp();
	}

	public void writeResult(int x) {
		Toast.makeText(context, "result=" + x, Toast.LENGTH_SHORT).show();
	}

	public GoogleAppsFacade getGoogleApps() {
		return gap;
	}
}
