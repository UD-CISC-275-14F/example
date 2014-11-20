package com.mygdx.game.desktop.test;

import com.mygdx.game.desktop.DesktopLauncher;

import edu.udel.jatlas.google.GoogleAppsFacade.OnObtainAccessToken;
import edu.udel.jatlas.google.GoogleAppsHttp;
import edu.udel.jatlas.google.GoogleAppsFacade.AccessToken;
import junit.framework.TestCase;

public class TestGoogleApps extends TestCase {
	protected void setUp() throws Exception {
		// force libGDX to start
		DesktopLauncher.main(new String[] {});

	}

	public void test_getCode() {
		GoogleAppsHttp http = new GoogleAppsHttp();
		final Object tokenwait = new Object();
		http.obtainAccessToken(new OnObtainAccessToken() {

			public void success(AccessToken token) {
				// this is good
				synchronized (tokenwait) {
					tokenwait.notify();
				}
			}

			public void failure(String error) {
				synchronized (tokenwait) {
					tokenwait.notify();
					fail("AccessToken not obtained: " + error);
				}
			}

			public void displayToUser(String message) {
				System.out.println(message);
			}
		});

		
		synchronized (tokenwait) {
			try {
				tokenwait.wait();
			} catch (InterruptedException e) {
				fail(e.getMessage());
			}
		}
		
		
	}
}
