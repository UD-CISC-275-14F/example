package com.mygdx.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MyGdxGame extends ApplicationAdapter {
	private ArrayList<String> WISDOM = new ArrayList<String>(Arrays.asList(
		"Commit early and often.",
		"For every bug you fix, two more are unknown.",
		"Poor estimation indicates poor requirements.",
		"Adding team members increases productivity sub-linearly"));

	SpriteBatch batch;
	Texture img;
	BitmapFont font;

	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		
		font = new BitmapFont();
        font.setColor(Color.BLACK);

        FileHandle file = Gdx.files.internal("wisdom.txt");
        Scanner scanner = new Scanner(file.read());
        while (scanner.hasNextLine()) {
        	WISDOM.add(scanner.nextLine());
        }
        scanner.close();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, 0, 0);
        font.draw(batch, WISDOM.get((int)((System.currentTimeMillis()/3000)%WISDOM.size())), 100, 300);
		batch.end();
	}
}
