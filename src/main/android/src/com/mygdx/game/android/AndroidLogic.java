package com.mygdx.game.android;

import android.widget.Toast;

import com.mygdx.game.PlatformSpecificLogic;

import edu.udel.jatlas.google.GoogleAppsFacade;
import edu.udel.jatlas.google.GoogleAppsHttp;

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
