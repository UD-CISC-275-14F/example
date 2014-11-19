package com.mygdx.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import edu.udel.jatlas.google.GoogleAppsFacade.AccessToken;
import edu.udel.jatlas.google.GoogleAppsFacade.OnObtainAccessToken;
import edu.udel.jatlas.google.GoogleAppsFacade.OnQueryResult;

public class MyGdxGame extends ApplicationAdapter {
	private ArrayList<String> WISDOM = new ArrayList<String>(Arrays.asList(
		"Commit early and often.",
		"For every bug you fix, two more are unknown.",
		"Poor estimation indicates poor requirements.",
		"Adding team members increases productivity sub-linearly"));

	SpriteBatch batch;
	Texture img;
	BitmapFont font;
	PlatformSpecificLogic platform;
	boolean result;
	AccessToken authenticated;
	String authMessage;

	public MyGdxGame(PlatformSpecificLogic logic) {
		platform = logic;
	}
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		
		font = new BitmapFont();
        font.setColor(Color.BLACK);

        FileHandle file = Gdx.files.internal("wisdom.txt");
        WISDOM.addAll(Arrays.asList(file.readString().split("[\\n\\r]+")));
        
        Gdx.input.setInputProcessor(new MyInputProcessor());
	}

	
	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, 0, 0);
        font.draw(batch, WISDOM.get((int)((System.currentTimeMillis()/3000)%WISDOM.size())), 100, 300);
        if (authMessage != null && authenticated == null) {
        	font.draw(batch, authMessage, 50, 400);
        }
        
        if (authenticated != null && !result && (int)((System.currentTimeMillis()/3000)%WISDOM.size()) == 0) {
        	platform.writeResult((int)((System.currentTimeMillis()/3000)));
        	
        	String sql = "INSERT INTO " + platform.getGoogleApps().getTableId("AtlasClassDatabaseReqs")
        			+ " ('Student Id', 'First Date Attempted', '# Attempts') VALUES ('jatlas@udel.edu', '" + 
        			new Date().toString() + "', 1)";
        	platform.getGoogleApps().queryFusionTable(sql, new GoogleAppsOnQueryResult(), authenticated);
        	result = true;
        }
		batch.end();
	}
	

	
	class MyInputProcessor extends InputAdapter {
		private long downTime;
		
		public boolean touchDown(int screenX, int screenY, int pointer,
				int button) {
			downTime = System.currentTimeMillis();
			return super.touchDown(screenX, screenY, pointer, button);
		}
		
		public boolean touchUp(int screenX, int screenY, int pointer, int button) {
			if (System.currentTimeMillis() - downTime > 2000) {
				platform.getGoogleApps().obtainAccessToken(new GoogleAppsOnObtainAccessToken());
			}
			return super.touchUp(screenX, screenY, pointer, button);
		}
	}
	
	class GoogleAppsOnObtainAccessToken implements OnObtainAccessToken {
		public void displayToUser(String message) {
			authMessage = message;
		}
		public void failure(String error) {
			System.err.println(error);
		}
		public void success(AccessToken token) {
			System.out.println("got token: " + token);
			authenticated = token;
		}
	}
	
	class GoogleAppsOnQueryResult implements OnQueryResult {

		public void success(String json) {
			System.out.println("got result success: " + json);
		}

		public void failure(String error) {
		}
		
	}
}
