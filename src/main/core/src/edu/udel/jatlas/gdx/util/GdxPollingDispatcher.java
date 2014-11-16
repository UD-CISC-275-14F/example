package edu.udel.jatlas.gdx.util;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

/**
 * Singleton.  Not the best design, but convenient.
 * Also not thread safe, but code for libGDX should be single threaded anyway.
 * 
 * @author jatlas
 */
public final class GdxPollingDispatcher implements Runnable {
	private static GdxPollingDispatcher INSTANCE = new GdxPollingDispatcher();
	
	private Array<PollingRunnable> thingsToDo;
	private Array<PollingRunnable> thingsToRun;
	
	private boolean running;

	private GdxPollingDispatcher() {
		thingsToDo = new Array<PollingRunnable>(false, 16, PollingRunnable.class);
		thingsToRun = new Array<PollingRunnable>(false, 16, PollingRunnable.class);
	}
	
	public static GdxPollingDispatcher getInstance() {
		return INSTANCE;
	}
	public void runOnce(Runnable r, long waitTime) {
		thingsToDo.add(new PollingRunnable(r, System.currentTimeMillis() + waitTime));
		if (!running) {
			running = true;
			Gdx.app.postRunnable(this);
		}
	}
	public void run() {
		long time = System.currentTimeMillis();
		
		// pull out the things that we need to run right now
		Iterator<PollingRunnable> iter = thingsToDo.iterator();
		while (iter.hasNext()) {
			PollingRunnable pr = iter.next();
			if (time >= pr.runTime) {
				iter.remove();
				thingsToRun.add(pr);
			}
		}
		// now run them, this allows them to add themselves back to the thingsToDo
		for (PollingRunnable pr : thingsToRun) {
			pr.r.run();
		}
		thingsToRun.clear();
		if (thingsToDo.size > 0) {
			running = true;
			Gdx.app.postRunnable(this);
		}
		else {
			running = false;
		}
	}
	
	static final class PollingRunnable {
		Runnable r;
		long runTime;
		public PollingRunnable(Runnable r, long runTime) {
			this.r = r;
			this.runTime = runTime;
		}
	}
}
