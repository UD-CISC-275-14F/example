package com.mygdx.game.desktop.test;

import java.util.Date;

import junit.framework.TestCase;

import com.mygdx.game.desktop.DesktopLauncher;

import edu.udel.jatlas.google.GoogleAppsFacade.AccessToken;
import edu.udel.jatlas.google.GoogleAppsFacade.OnObtainAccessToken;
import edu.udel.jatlas.google.GoogleAppsFacade.OnQueryResult;
import edu.udel.jatlas.google.GoogleAppsHttp;

public class TestGoogleApps extends TestCase {
	private GoogleAppsHttp http;
	private static AccessToken accessToken;
	
	protected void setUp() throws Exception {
		// force libGDX to start
		DesktopLauncher.launch();
		http = new GoogleAppsHttp();
	}
	
	public AccessToken getAccessToken() {
		if (accessToken != null) {
			return accessToken;
		}
		final Object tokenwait = new Object();
		http.obtainAccessToken(new OnObtainAccessToken() {

			public void success(AccessToken token) {
				accessToken = token;
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
		
		return accessToken;
	}

	public void test_getCode() {
		getAccessToken();
	}
	
	public void test_insert() {
		String sql = "INSERT INTO " + http.getTableId("AtlasClassDatabaseReqs")
    			+ " ('Student Id', 'First Date Attempted', '# Attempts') VALUES ('jatlas@udel.edu', '" + 
    			new Date().toString() + "', 1)";
		final Object tokenwait = new Object();
		
		http.queryFusionTable(sql, new OnQueryResult() {
			
			public void success(String json) {
				synchronized (tokenwait) {
					tokenwait.notify();
				}
			}
			
			public void failure(String error) {
				synchronized (tokenwait) {
					tokenwait.notify();
					fail("Insert failed: " + error);
				}
			}
		}, getAccessToken());
		
		synchronized (tokenwait) {
			try {
				tokenwait.wait();
			} catch (InterruptedException e) {
				fail(e.getMessage());
			}
		}
	}
}
