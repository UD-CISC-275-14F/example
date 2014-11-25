package edu.udel.jatlas.gdxexample.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

import edu.udel.jatlas.gdxexample.MyGdxGame;
import edu.udel.jatlas.gdxexample.PlatformSpecificLogic;
import edu.udel.jatlas.gdxexample.google.GoogleAppsFacade;
import edu.udel.jatlas.gdxexample.google.GoogleAppsHttp;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(480, 320);
        }

        @Override
        public ApplicationListener getApplicationListener () {
                return new MyGdxGame(new PlatformSpecificLogic() {
					GoogleAppsFacade gap = new GoogleAppsHttp();
					
					public void writeResult(int x) {
					}
					
					public GoogleAppsFacade getGoogleApps() {
						return gap;
					}
				});
        }
}