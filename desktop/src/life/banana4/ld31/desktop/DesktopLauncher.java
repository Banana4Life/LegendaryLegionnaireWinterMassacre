package life.banana4.ld31.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import life.banana4.ld31.Ld31;

public class DesktopLauncher {

	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Ld31.SCREEN_WIDTH;
		config.height = Ld31.SCREEN_HEIGHT;
		new LwjglApplication(new Ld31(), config);
	}
}
